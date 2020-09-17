package com.github.watermoonlx.sqlanalyzer.mysql.listener;

import com.github.watermoonlx.sqlanalyzer.common.StringUtils;
import com.github.watermoonlx.sqlanalyzer.mysql.parser.MySqlParser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.antlr.v4.runtime.TokenStream;

@Getter
@EqualsAndHashCode
public class TableItem {

    private static final String UNKNOWN = "unknown";

    private String tableName;
    private String alias;

    private TableItem() {
    }

    public static TableItem from(MySqlParser.AtomTableItemContext context, TokenStream tokens) {
        TableItem tableItem = new TableItem();
        tableItem.tableName = extractTableName(context.tableName(), tokens);
        if (context.alias != null) {
            tableItem.alias = tokens.getText(context.alias);
        }
        return tableItem;
    }

    public static TableItem from(MySqlParser.ColumnCreateTableContext context, TokenStream tokens) {
        TableItem tableItem = new TableItem();
        tableItem.tableName = extractTableName(context.tableName(), tokens);
        return tableItem;
    }

    private static String extractTableName(MySqlParser.TableNameContext context, TokenStream tokens) {
        if (context != null && context.fullId() != null) {
            int size = context.fullId().uid().size();
            MySqlParser.UidContext uid = context.fullId().uid(size - 1);
            String tableName = StringUtils.trimBackquote(tokens.getText(uid));
            return tableName;
        }
        return UNKNOWN;
    }

}
