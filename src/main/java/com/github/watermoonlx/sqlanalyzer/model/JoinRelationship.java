package com.github.watermoonlx.sqlanalyzer.model;

import com.github.watermoonlx.sqlanalyzer.common.StringUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class JoinRelationship {
    private final String leftTable;
    private final String leftColumn;
    private final String rightTable;
    private final String rightColumn;

    public JoinRelationship(String leftTable,
            String leftColumn,
            String rightTable,
            String rightColumn) {
        this.leftTable = StringUtils.trimBackquote(leftTable.toLowerCase());
        this.leftColumn = StringUtils.trimBackquote(leftColumn.toLowerCase());
        this.rightTable = StringUtils.trimBackquote(rightTable.toLowerCase());
        this.rightColumn = StringUtils.trimBackquote(rightColumn.toLowerCase());
    }
}
