package com.github.watermoonlx.sqlanalyzer.model;

import lombok.Getter;
import lombok.NonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

@Getter
public class AnalyzeResult {
    private final HashMap<String, Table> tables;
    private final HashSet<JoinRelationship> joinRelationships;

    public AnalyzeResult() {
        this.tables = new HashMap<>();
        this.joinRelationships = new HashSet<>();
    }

    public AnalyzeResult(HashMap<String, Table> tables, HashSet<JoinRelationship> joinRelationships) {
        this.tables = tables != null ? tables : new HashMap<>();
        this.joinRelationships = joinRelationships != null ? joinRelationships : new HashSet<>();
    }

    public void addTable(Table table) {
        this.tables.put(table.getName(), table);
    }

    public void addJoinRelationship(JoinRelationship relationship) {
        this.joinRelationships.add(relationship);
    }

    public AnalyzeResult combine(@NonNull AnalyzeResult other) {
        HashMap<String, Table> newTables = new HashMap<>();
        this.tables.forEach(newTables::put);
        other.tables.forEach(newTables::put);
        HashSet<JoinRelationship> newRelationships = new HashSet<>();
        newRelationships.addAll(this.joinRelationships);
        newRelationships.addAll(other.joinRelationships);
        return new AnalyzeResult(newTables, newRelationships);
    }
}