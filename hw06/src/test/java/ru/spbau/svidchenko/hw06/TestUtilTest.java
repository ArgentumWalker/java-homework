package ru.spbau.svidchenko.hw06;

import org.junit.Before;
import org.junit.Test;
import ru.spbau.svidchenko.hw06.testclasses.*;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestUtilTest {
    private TestUtil testUtil;
    private ArrayList<Class<?>> classes;

    @Before
    public void before() throws Exception {
        testUtil = new TestUtil();
        classes = new ArrayList<>();
    }

    @Test
    public void testClasses_ComplexTest() throws Exception {
        classes.add(AllTestSituations.class);
        TestUtil.TestSummary summary = testUtil.testClasses(classes);
        assertEquals(1, summary.classTests.size());
        TestUtil.ClassTestSummary classSummary = summary.classTests.get(0);
        assertEquals(12, classSummary.testCount);
        assertEquals(3, classSummary.ignored);
        assertEquals(4, classSummary.score);

    }

    @Test
    public void testClasses_TestWithErrorAtAfter() throws Exception {
        classes.add(AfterTestException.class);
        TestUtil.TestSummary summary = testUtil.testClasses(classes);
        TestUtil.ClassTestSummary classSummary = summary.classTests.get(0);
        assertEquals(12, classSummary.testCount);
        assertEquals(3, classSummary.ignored);
        assertEquals(0, classSummary.score);
    }

    @Test
    public void testClasses_TestWithErrorAtBefore() throws Exception {
        classes.add(BeforeTestException.class);
        TestUtil.TestSummary summary = testUtil.testClasses(classes);
        TestUtil.ClassTestSummary classSummary = summary.classTests.get(0);
        assertEquals(12, classSummary.testCount);
        assertEquals(3, classSummary.ignored);
        assertEquals(0, classSummary.score);
    }

    @Test
    public void testClasses_TestWithErrorAtClassAfter() throws Exception {
        classes.add(AfterClassException.class);
        TestUtil.TestSummary summary = testUtil.testClasses(classes);
        TestUtil.ClassTestSummary classSummary = summary.classTests.get(0);
        assertEquals(12, classSummary.testCount);
        assertEquals(3, classSummary.ignored);
        assertEquals(0, classSummary.score);
    }

    @Test
    public void testClasses_TestWithErrorAtClassBefore() throws Exception {
        classes.add(BeforeClassException.class);
        TestUtil.TestSummary summary = testUtil.testClasses(classes);
        TestUtil.ClassTestSummary classSummary = summary.classTests.get(0);
        assertEquals(12, classSummary.testCount);
        assertEquals(3, classSummary.ignored);
        assertEquals(0, classSummary.score);
    }
}