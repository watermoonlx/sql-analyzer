package com.github.watermoonlx.sqlanalyzer.mysql;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Utils {
    private static final String SQL_PATH = "src/test/resources/";

    public static String readDdlSql() {
        File file = new File(SQL_PATH + "ddl.sql");
        String absolutePath = file.getAbsolutePath();
        try {
            List<String> lines = Files.readAllLines(Paths.get(absolutePath));
            return String.join("\n", lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String readDmlSql() {
        File file = new File(SQL_PATH + "dml.sql");
        String absolutePath = file.getAbsolutePath();
        try {
            List<String> lines = Files.readAllLines(Paths.get(absolutePath));
            return String.join("\n", lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
