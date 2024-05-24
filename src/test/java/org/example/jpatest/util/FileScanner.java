package org.example.jpatest.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileScanner {
    private static final String SQL_LOG_FILE = "target/sql.log";
    public static String readSqlLog() {
        final StringBuilder content = new StringBuilder();
        try (final BufferedReader br = new BufferedReader(new FileReader(SQL_LOG_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line)
                        .append(System.lineSeparator());
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
