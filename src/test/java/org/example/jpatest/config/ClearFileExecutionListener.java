package org.example.jpatest.config;


import org.example.jpatest.util.FileScanner;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class ClearFileExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void afterTestMethod(final TestContext testContext) throws Exception {
        FileScanner.clearLog();
    }
}
