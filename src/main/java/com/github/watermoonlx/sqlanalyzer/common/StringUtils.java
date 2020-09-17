package com.github.watermoonlx.sqlanalyzer.common;

public class StringUtils {
    public static String trimBackquote(String s) {
        if (s == null) {
            return null;
        }
        if (s.startsWith("`") && s.endsWith("`")) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }

    public static String trimQuote(String s) {
        if (s == null) {
            return null;
        }
        if (s.startsWith("'") && s.endsWith("'")) {
            return s.substring(1, s.length() - 1);
        }
        if (s.startsWith("\"") && s.endsWith("\"")) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }
}
