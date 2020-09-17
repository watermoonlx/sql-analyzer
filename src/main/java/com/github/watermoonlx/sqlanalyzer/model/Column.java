package com.github.watermoonlx.sqlanalyzer.model;

import com.github.watermoonlx.sqlanalyzer.common.StringUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 代表表中的一列
 */
@EqualsAndHashCode
public class Column {
    @Getter
    private final String name;
    @Getter
    private final String type;
    @Getter
    @Setter
    private boolean isPrimaryKey;
    @Getter
    private final String comment;

    public Column(String name, String type) {
        this(name, type, false, null);
    }

    public Column(String name, String type, boolean isPrimaryKey) {
        this(name, type, isPrimaryKey, null);
    }

    public Column(String name, String type, boolean isPrimaryKey, String comment) {
        this.name = StringUtils.trimBackquote(name.toLowerCase());
        this.type = type.toLowerCase();
        this.isPrimaryKey = isPrimaryKey;
        this.comment = StringUtils.trimQuote(comment);
    }
}
