package com.github.watermoonlx.sqlanalyzer.api;

import com.github.watermoonlx.sqlanalyzer.model.AnalyzeResult;

public interface SqlAnalyzer {
    AnalyzeResult analyze(String sql);
}
