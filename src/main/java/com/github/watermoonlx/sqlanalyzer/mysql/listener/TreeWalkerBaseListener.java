package com.github.watermoonlx.sqlanalyzer.mysql.listener;

import com.github.watermoonlx.sqlanalyzer.model.AnalyzeResult;
import com.github.watermoonlx.sqlanalyzer.mysql.parser.MySqlParserBaseListener;

public abstract class TreeWalkerBaseListener extends MySqlParserBaseListener {
    public abstract AnalyzeResult getResult();
}
