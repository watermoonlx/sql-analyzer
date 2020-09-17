package com.github.watermoonlx.sqlanalyzer.diagramgenerator;

import com.github.watermoonlx.sqlanalyzer.api.DiagramGenerator;
import com.github.watermoonlx.sqlanalyzer.model.AnalyzeResult;
import com.github.watermoonlx.sqlanalyzer.model.Column;
import com.github.watermoonlx.sqlanalyzer.model.JoinRelationship;
import com.github.watermoonlx.sqlanalyzer.model.Table;
import lombok.NonNull;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;

public class JoinRelationshipDiagramGenerator implements DiagramGenerator {
    @Override
    public void generate(@NonNull AnalyzeResult result, @NonNull OutputStream outputStream) throws IOException {
        String diagramDescription = this.buildDiagramDescription(result);
        SourceStringReader reader = new SourceStringReader(diagramDescription);
        reader.generateImage(outputStream);
    }

    public void generate(@NonNull AnalyzeResult result, @NonNull String filePath) throws IOException {
        Path absPath = Paths.get(filePath).toAbsolutePath();
        if (!Files.exists(absPath)) {
            Files.createFile(absPath);
        }
        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath))) {
            this.generate(result, outputStream);
        }
    }

    private String buildDiagramDescription(AnalyzeResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("@startuml\n" +
                "!define table(x) class x << (T,#FFAAAA) >>\n" +
                "hide circle\n" +
                "hide methods\n\n");

        // 样式
        sb.append("skinparam class {\n" +
                "BackgroundColor White\n" +
                "HeaderBackgroundColor #F0F0F0\n" +
                "FontStyle bold\n" +
                "ArrowColor #007ACC\n" +
                "BorderColor #D6D6D6\n" +
                "}\n" +
                "skinparam defaultFontName Helvetica \n\n");

        this.buildTableDescription(result, sb);
        this.buildRelationshipDescription(result, sb);

        sb.append("@enduml\n");
        return sb.toString();
    }

    private void buildTableDescription(AnalyzeResult result, StringBuilder sb) {
        HashSet<String> tableNames = new HashSet<>();
        for (JoinRelationship relationship : result.getJoinRelationships()) {
            tableNames.add(relationship.getLeftTable());
            tableNames.add(relationship.getRightTable());
        }
        for (String tableName : tableNames) {
            Table table = result.getTables().getOrDefault(tableName, new Table(tableName));
            sb.append("table(").append(table.getName()).append(") {\n");
            for (Column col : table.getColumns()) {
                sb.append(col.isPrimaryKey() ? "# " : "+ ").append(col.getName()).append("\n");
            }
            sb.append("}\n\n");
        }
    }

    private void buildRelationshipDescription(AnalyzeResult result, StringBuilder sb) {
        for (JoinRelationship relationship : result.getJoinRelationships()) {
            sb.append(relationship.getLeftTable())
                    .append("::")
                    .append(relationship.getLeftColumn())
                    .append(" <--> ")
                    .append(relationship.getRightTable())
                    .append("::")
                    .append(relationship.getRightColumn())
                    .append("\n");
        }
    }

}
