package com.github.watermoonlx.sqlanalyzer.mysql.listener;

import com.github.watermoonlx.sqlanalyzer.common.StringUtils;
import com.github.watermoonlx.sqlanalyzer.model.AnalyzeResult;
import com.github.watermoonlx.sqlanalyzer.model.Column;
import com.github.watermoonlx.sqlanalyzer.model.Table;
import com.github.watermoonlx.sqlanalyzer.mysql.parser.MySqlParser;
import lombok.Getter;
import org.antlr.v4.runtime.TokenStream;

import java.util.HashSet;

public class TableExtractor extends TreeWalkerBaseListener {

    private final TokenStream tokens;
    @Getter
    private final HashSet<Table> tables = new HashSet<>();
    private Table currentTable;

    public TableExtractor(TokenStream tokens) {
        this.tokens = tokens;
    }

    @Override
    public void enterColumnCreateTable(MySqlParser.ColumnCreateTableContext ctx) {
        TableItem tableItem = TableItem.from(ctx, this.tokens);
        this.currentTable = new Table(tableItem.getTableName());
        if (this.tables.contains(this.currentTable)) {
            this.currentTable = null;
        }
    }

    @Override
    public void exitColumnCreateTable(MySqlParser.ColumnCreateTableContext ctx) {
        if (this.currentTable != null) {
            this.tables.add(this.currentTable);
            this.currentTable = null;
        }
    }

    @Override
    public void enterColumnDeclaration(MySqlParser.ColumnDeclarationContext ctx) {
        if (this.currentTable == null) {
            return;
        }

        Column column = this.extractColumn(ctx);
        this.currentTable.addColumn(column);
    }

    /**
     * 设置主键列
     */
    @Override
    public void enterPrimaryKeyTableConstraint(MySqlParser.PrimaryKeyTableConstraintContext ctx) {
        for (MySqlParser.IndexColumnNameContext colNameCtx : ctx.indexColumnNames().indexColumnName()) {
            String colName = StringUtils.trimBackquote(this.tokens.getText(colNameCtx));
            for (Column col : this.currentTable.getColumns()) {
                if (col.getName().equalsIgnoreCase(colName)) {
                    col.setPrimaryKey(true);
                    break;
                }
            }
        }
        super.enterPrimaryKeyTableConstraint(ctx);
    }

    @Override
    public AnalyzeResult getResult() {
        AnalyzeResult result = new AnalyzeResult();
        this.tables.forEach(result::addTable);
        return result;
    }

    private Column extractColumn(MySqlParser.ColumnDeclarationContext ctx) {
        String columnName = this.tokens.getText(ctx.uid());
        String columnType = this.tokens.getText(ctx.columnDefinition().dataType());
        boolean isPrimaryKey = false;
        String comment = null;
        if (ctx.columnDefinition().columnConstraint() != null) {
            for (MySqlParser.ColumnConstraintContext constraint : ctx.columnDefinition().columnConstraint()) {
                if (constraint instanceof MySqlParser.PrimaryKeyColumnConstraintContext) {
                    isPrimaryKey = true;
                    continue;
                }
                if (constraint instanceof MySqlParser.CommentColumnConstraintContext) {
                    comment = ((MySqlParser.CommentColumnConstraintContext) constraint).STRING_LITERAL().getText();
                }
            }
        }

        return new Column(columnName, columnType, isPrimaryKey, comment);
    }
}
