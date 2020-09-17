package com.github.watermoonlx.sqlanalyzer.mysql;

import com.github.watermoonlx.sqlanalyzer.api.SqlAnalyzer;
import com.github.watermoonlx.sqlanalyzer.common.CaseChangingCharStream;
import com.github.watermoonlx.sqlanalyzer.model.AnalyzeResult;
import com.github.watermoonlx.sqlanalyzer.mysql.listener.JoinRelationshipExtractor;
import com.github.watermoonlx.sqlanalyzer.mysql.listener.TableExtractor;
import com.github.watermoonlx.sqlanalyzer.mysql.listener.TreeWalkerBaseListener;
import com.github.watermoonlx.sqlanalyzer.mysql.parser.MySqlLexer;
import com.github.watermoonlx.sqlanalyzer.mysql.parser.MySqlParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class MySqlAnalyzer implements SqlAnalyzer {

    private final ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
    private ArrayList<TreeWalkerBaseListener> listeners = new ArrayList<>();
    private TokenStream tokens;
    private MySqlParser.RootContext parseTreeRoot;

    @SuppressWarnings("unchecked")
    @Override
    public AnalyzeResult analyze(String sql) {
        try {
            this.parse(sql);
            this.initListeners();
            AnalyzeResult finalResult = new AnalyzeResult();

            CompletableFuture<Void>[] futures = this.listeners.stream().map(listener -> CompletableFuture.runAsync(
                    () -> this.parseTreeWalker.walk(listener, this.parseTreeRoot))
            ).toArray(CompletableFuture[]::new);
            CompletableFuture.allOf(futures).join();
            for (TreeWalkerBaseListener listener : this.listeners) {
                finalResult = finalResult.combine(listener.getResult());
            }
            return finalResult;
        } finally {
            this.listeners = null;
            this.tokens = null;
            this.parseTreeRoot = null;
        }
    }

    private void parse(String sql) {
        CodePointCharStream input = CharStreams.fromString(sql);
        CaseChangingCharStream upperInput = new CaseChangingCharStream(input, true);

        MySqlLexer lexer = new MySqlLexer(upperInput);
        this.tokens = new CommonTokenStream(lexer);
        MySqlParser parser = new MySqlParser(this.tokens);
        this.parseTreeRoot = parser.root();
    }

    private void initListeners() {
        this.listeners = new ArrayList<>();
        this.listeners.add(new TableExtractor(tokens));
        this.listeners.add(new JoinRelationshipExtractor(tokens));
    }
}
