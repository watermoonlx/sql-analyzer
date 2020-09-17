package com.github.watermoonlx.sqlanalyzer.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;

/**
 * 代表一张表
 */
@EqualsAndHashCode(of = {"name"})
public class Table {
    @Getter
    private final String name;
    @Getter
    private final ArrayList<Column> columns = new ArrayList<>();

    public Table(@NonNull String name) {
        this.name = name.toLowerCase();
    }

    public void addColumn(Column column) {
        this.columns.add(column);
    }
}
