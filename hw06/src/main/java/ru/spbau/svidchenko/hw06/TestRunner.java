package ru.spbau.svidchenko.hw06;

import java.io.IOException;

/**
 * Run tests from classes in directory or from already loaded classes
 */
public class TestRunner {
    private TestUtil testUtil;

    public TestRunner() {
        testUtil = new TestUtil();
    }

    public TestUtil.TestSummary runTests(String path) throws IOException {
        return testUtil.testClasses(FileUtil.loadClasses(path));
    }
}
