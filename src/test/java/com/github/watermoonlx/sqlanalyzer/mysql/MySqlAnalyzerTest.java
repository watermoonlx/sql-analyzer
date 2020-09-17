package com.github.watermoonlx.sqlanalyzer.mysql;

import com.github.watermoonlx.sqlanalyzer.model.AnalyzeResult;
import com.github.watermoonlx.sqlanalyzer.model.Column;
import com.github.watermoonlx.sqlanalyzer.model.JoinRelationship;
import com.github.watermoonlx.sqlanalyzer.model.Table;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class MySqlAnalyzerTest {

    @Test
    public void testExtractTable() {
        String sql = "CREATE TABLE `order` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单id',\n" +
                "  `order_no` bigint(20) DEFAULT NULL COMMENT '订单号',\n" +
                "  `user_id` int(11) DEFAULT NULL COMMENT '用户id',\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  UNIQUE KEY `order_no_index` (`order_no`) USING BTREE\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=118 DEFAULT CHARSET=utf8;";

        MySqlAnalyzer analyzer = new MySqlAnalyzer();
        AnalyzeResult result = analyzer.analyze(sql);
        Table[] tables = result.getTables().values().toArray(new Table[1]);

        Assertions.assertEquals(1, tables.length);
        Assertions.assertEquals("order", tables[0].getName());

        ArrayList<Column> columns = tables[0].getColumns();
        Assertions.assertEquals(3, columns.size());

        Assertions.assertEquals("id", columns.get(0).getName());
        Assertions.assertEquals("int(11)", columns.get(0).getType());
        Assertions.assertEquals("订单id", columns.get(0).getComment());

        Assertions.assertEquals("order_no", columns.get(1).getName());
        Assertions.assertEquals("bigint(20)", columns.get(1).getType());
        Assertions.assertEquals("订单号", columns.get(1).getComment());

        Assertions.assertEquals("user_id", columns.get(2).getName());
        Assertions.assertEquals("int(11)", columns.get(2).getType());
        Assertions.assertEquals("用户id", columns.get(2).getComment());
    }

    @Test
    public void testAnalyze() {
        MySqlAnalyzer analyzer = new MySqlAnalyzer();

        String sql = "SELECT * " +
                "FROM table1 AS t1 " +
                "INNER JOIN table2 AS t2 ON t1.field=t2.field " +
                "WHERE t1.id>3";

        AnalyzeResult result = analyzer.analyze(sql);
        JoinRelationship[] relationships = result.getJoinRelationships().toArray(new JoinRelationship[1]);

        Assertions.assertEquals(1, relationships.length);

        Assertions.assertEquals("table1", relationships[0].getLeftTable());
        Assertions.assertEquals("field", relationships[0].getLeftColumn());
        Assertions.assertEquals("table2", relationships[0].getRightTable());
        Assertions.assertEquals("field", relationships[0].getRightColumn());
    }
}
