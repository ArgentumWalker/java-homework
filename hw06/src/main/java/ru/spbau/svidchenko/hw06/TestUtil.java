package ru.spbau.svidchenko.hw06;

import ru.spbau.svidchenko.hw06.annotation.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Util for run tests form classes
 */
public class TestUtil {
    private ExecutorService threadPool;
    private final static  int THREADS_COUNT = 8;

    public TestUtil() {
        threadPool = Executors.newFixedThreadPool(THREADS_COUNT);
    }

    public TestSummary testClasses(List<Class<?>> classes) {
        ArrayList<ClassTestSummary> classTestResults = new ArrayList<ClassTestSummary>();
        for (final Class<?> cl : classes) {
            final ClassTestSummary classTestSummary = new ClassTestSummary();
            classTestResults.add(classTestSummary);
            classTestSummary.className = cl.getName();
            classTestSummary.aClass = cl;
            Method[] methods = cl.getDeclaredMethods();
            final ArrayList<Method> beforeTest = new ArrayList<Method>();
            final ArrayList<Method> afterTest = new ArrayList<Method>();
            ArrayList<TestResult> results = new ArrayList<TestResult>();
            ArrayList<Future<?>> futures = new ArrayList<Future<?>>();
            classTestSummary.methodResults = results;
            boolean globalFail = false;
            try {
                for (Method m : methods) {
                    m.setAccessible(true);
                    if (m.getAnnotation(BeforeClass.class) != null) {
                        m.invoke(null);
                    }
                    if (m.getAnnotation(Before.class) != null) {
                        beforeTest.add(m);
                    }
                    if (m.getAnnotation(After.class) != null) {
                        afterTest.add(m);
                    }
                }
            } catch (Throwable e) {
                globalFail = true;
                classTestSummary.failedWith = e.getCause();
            }
            for (final Method m : methods) {
                final Test testAnnotation = m.getAnnotation(Test.class);
                if (testAnnotation != null) {
                    final TestResult testResult = new TestResult();
                    testResult.method = m;
                    testResult.testName = m.getName();
                    results.add(testResult);
                    classTestSummary.testCount++;
                    if (!testAnnotation.ignore().equals("")) {
                        testResult.ignored = true;
                        testResult.message = testAnnotation.ignore();
                        classTestSummary.ignored++;
                        continue;
                    }
                    if (globalFail) {
                        testResult.failedWith = classTestSummary.failedWith;
                        testResult.message = "Failed with " + testResult.failedWith;
                        continue;
                    }
                    Future<?> future = threadPool.submit(() -> {
                        try {
                            Object o = cl.newInstance();
                            for (Method m1 : beforeTest) {
                                m1.invoke(o);
                            }
                            try {
                                m.setAccessible(true);
                                m.invoke(o);
                                if (!testAnnotation.expected().equals(TestConstants.NULL_EXCEPTION)) {
                                    testResult.passed = false;
                                    testResult.message = "Expected " + testAnnotation.expected().getName() + " but don't get one";
                                } else {
                                    testResult.passed = true;
                                    testResult.message = "Ok";
                                    classTestSummary.score++;
                                }
                            } catch (Throwable e) {
                                if (!testAnnotation.expected().equals(TestConstants.NULL_EXCEPTION)) {
                                    if (testAnnotation.expected().isInstance(e.getCause())) {
                                        testResult.passed = true;
                                        testResult.message = "Ok";
                                        classTestSummary.score++;
                                    } else {
                                        throw e;
                                    }
                                } else {
                                    throw e;
                                }
                            }
                            for (Method m1 : afterTest) {
                                m1.invoke(o);
                            }
                        } catch (Throwable e) {
                            if (testResult.passed) {
                                classTestSummary.score--;
                            }
                            testResult.passed = false;
                            testResult.failedWith = e.getCause();
                            testResult.message = "Failed with " + e.getCause();
                        }
                    });
                    futures.add(future);
                }
            }
            try {
                if (!globalFail) {
                    for (Future<?> future : futures) {
                        Object o = future.get();
                    }
                    for (Method m : methods) {
                        if (m.getAnnotation(AfterClass.class) != null) {
                            m.invoke(null);
                        }
                    }
                }
            } catch (Throwable e) {
                classTestSummary.failedWith = e.getCause();
                classTestSummary.score = 0;
                for (TestResult result : results) {
                    if (!result.ignored) {
                        result.failedWith = e.getCause();
                        result.passed = false;
                        result.message = "Failed with " + e.getCause();
                    }
                }
                continue;
            }
        }
        TestSummary summary = new TestSummary();
        summary.classTests = classTestResults;
        for (ClassTestSummary res : classTestResults) {
            summary.score += res.score;
            summary.ignoredScore += res.ignored;
            summary.testCount += res.testCount;
        }
        return summary;
    }

    public final class TestResult {
        public Method method;
        public String testName;
        public Throwable failedWith;
        public boolean passed = false;
        public boolean ignored = false;
        public String message = "";
    }

    public final class ClassTestSummary {
        public String className;
        public Class<?> aClass;
        public ArrayList<TestResult> methodResults;
        public int score = 0;
        public int ignored = 0;
        public int testCount = 0;
        public Throwable failedWith = null;
    }

    public final class TestSummary {
        public int score = 0;
        public int ignoredScore = 0;
        public int testCount = 0;
        public ArrayList<ClassTestSummary> classTests;
    }
}
