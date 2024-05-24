package org.example.jpatest.config;


import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;


public class ClearFileExecutionListener extends AbstractTestExecutionListener {
    private static final String SQL_LOG_FILE = "target/sql.log";

    @Override
    public void afterTestMethod(final TestContext testContext) throws Exception {
        clearLog();
    }

    private void clearLog() {
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
