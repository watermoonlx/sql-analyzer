package com.github.watermoonlx.sqlanalyzer.mysql.listener;

import com.github.watermoonlx.sqlanalyzer.common.StringUtils;
import com.github.watermoonlx.sqlanalyzer.model.AnalyzeResult;
import com.github.watermoonlx.sqlanalyzer.model.JoinRelationship;
import com.github.watermoonlx.sqlanalyzer.mysql.parser.MySqlParser;
import org.antlr.v4.runtime.TokenStream;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class JoinRelationshipExtractor extends TreeWalkerBaseListener {

    private final TokenStream tokens;
    private final HashSet<JoinRelationship> relationships = new HashSet<>();
    private final LinkedList<HashMap<String, TableItem>> tableSourceContextStack = new LinkedList<>();

    public JoinRelationshipExtractor(TokenStream tokens) {
        this.tokens = tokens;
    }

    @Override
    public void enterTableSourceBase(MySqlParser.TableSourceBaseContext ctx) {
        this.tableSourceContextStack.push(new HashMap<>());
    }

    @Override
    public void exitTableSourceBase(MySqlParser.TableSourceBaseContext ctx) {
        this.tableSourceContextStack.pop();
    }

    @Override
    public void enterTableSourceNested(MySqlParser.TableSourceNestedContext ctx) {
        this.tableSourceContextStack.push(new HashMap<>());
    }

    @Override
    public void exitTableSourceNested(MySqlParser.TableSourceNestedContext ctx) {
        this.tableSourceContextStack.pop();
    }

    /**
     * 每次进入AtomTableItem时，提取表，放入当前栈顶的字典中。
     */
    @Override
    public void enterAtomTableItem(MySqlParser.AtomTableItemContext ctx) {
        TableItem tableItem = TableItem.from(ctx, this.tokens);
        this.tableSourceContextStack.peek().put(this.getTableKey(tableItem), tableItem);
    }

    @Override
    public void exitInnerJoin(MySqlParser.InnerJoinContext ctx) {
        this.extractRelationshipByOnExpr(ctx.expression());
    }

    @Override
    public void exitStraightJoin(MySqlParser.StraightJoinContext ctx) {
        this.extractRelationshipByOnExpr(ctx.expression());
    }

    @Override
    public void exitOuterJoin(MySqlParser.OuterJoinContext ctx) {
        this.extractRelationshipByOnExpr(ctx.expression());
    }

    @Override
    public AnalyzeResult getResult() {
        return new AnalyzeResult(null, this.relationships);
    }

    private String getTableKey(TableItem tableItem) {
        return tableItem.getAlias() != null ? tableItem.getAlias() : tableItem.getTableName();
    }

    private void extractRelationshipByOnExpr(MySqlParser.ExpressionContext onExpr) {
        if (!(onExpr instanceof MySqlParser.PredicateExpressionContext)) {
            return;
        }

        MySqlParser.PredicateContext predicate = ((MySqlParser.PredicateExpressionContext) onExpr).predicate();
        if (!(predicate instanceof MySqlParser.BinaryComparasionPredicateContext)) {
            return;
        }

        MySqlParser.BinaryComparasionPredicateContext binaryPredicate = (MySqlParser.BinaryComparasionPredicateContext) predicate;
        String leftText = this.tokens.getText(binaryPredicate.left);
        String[] leftTexts = leftText.split("\\.");
        String rightText = this.tokens.getText(binaryPredicate.right);
        String[] rightTexts = rightText.split("\\.");

        HashMap<String, TableItem> tableSourceContext = this.tableSourceContextStack.peek();
        String leftTable = tableSourceContext.get(StringUtils.trimBackquote(leftTexts[0])).getTableName();
        String leftColumn = leftTexts[1];
        String rightTable = tableSourceContext.get(StringUtils.trimBackquote(rightTexts[0])).getTableName();
        String rightColumn = rightTexts[1];

        // 忽略自己Join自己的表
        if (leftTable.equalsIgnoreCase(rightTable)) {
            return;
        }

        this.relationships.add(
                new JoinRelationship(leftTable, leftColumn, rightTable, rightColumn)
        );
    }
}
