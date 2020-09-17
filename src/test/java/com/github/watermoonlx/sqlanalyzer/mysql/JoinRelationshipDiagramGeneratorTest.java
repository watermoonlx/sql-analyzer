package com.github.watermoonlx.sqlanalyzer.mysql;

import com.github.watermoonlx.sqlanalyzer.diagramgenerator.JoinRelationshipDiagramGenerator;
import com.github.watermoonlx.sqlanalyzer.model.AnalyzeResult;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class JoinRelationshipDiagramGeneratorTest {
    
    @Test
    public void testGenerate() throws IOException {
        String ddlSql = Utils.readDdlSql();
        String dmlSql = Utils.readDmlSql();

        MySqlAnalyzer analyzer = new MySqlAnalyzer();
        AnalyzeResult result = analyzer.analyze(ddlSql + "\n" + dmlSql);

        new JoinRelationshipDiagramGenerator().generate(result, "temp/完整示例.png");
    }


}
