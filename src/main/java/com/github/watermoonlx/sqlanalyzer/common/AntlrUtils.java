package com.github.watermoonlx.sqlanalyzer.common;

import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.Tree;

import java.util.Arrays;

public class AntlrUtils {
    public static void viewTree(Tree root, Parser parser) {
        TreeViewer viewer = new TreeViewer(Arrays.asList(
                parser.getRuleNames()), root);
        viewer.open();
    }
}
