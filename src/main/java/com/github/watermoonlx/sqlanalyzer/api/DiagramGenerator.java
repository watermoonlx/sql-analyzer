package com.github.watermoonlx.sqlanalyzer.api;

import com.github.watermoonlx.sqlanalyzer.model.AnalyzeResult;

import java.io.IOException;
import java.io.OutputStream;

public interface DiagramGenerator {
    void generate(AnalyzeResult result, OutputStream stream) throws IOException;
}
