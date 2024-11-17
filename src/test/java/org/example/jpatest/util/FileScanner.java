package org.example.jpatest.util;

import java.io.*;

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
    public static void clearLog() {
        try {
            final File logFile = new File(SQL_LOG_FILE);
            if (logFile.exists()) {
                logFile.delete();
            }
            new PrintWriter(SQL_LOG_FILE).close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
