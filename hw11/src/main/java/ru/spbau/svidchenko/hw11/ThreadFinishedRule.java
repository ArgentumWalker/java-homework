package ru.spbau.svidchenko.hw11;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.ArrayList;

public class ThreadFinishedRule implements TestRule {
    private ArrayList<Thread> threads = new ArrayList<>();
    private Throwable uncaughtExceptions = null;
    private Thread.UncaughtExceptionHandler exceptionHandler = new UncaughtExceptionHandler();

    @Override
    public Statement apply(Statement base, Description description) {
        Statement result = new Statement() {
            @Override
            public void evaluate() throws Throwable {
                base.evaluate();
                for (Thread t : threads) {
                    if (t.isAlive()) {
                        throw new ThreadAliveException();
                    }
                    if (uncaughtExceptions != null) {
                        throw new ThreadUncaughtExceptionException(uncaughtExceptions);
                    }
                }
            }
        };
        return result;
    }

    public void addThread(Thread t) {
        threads.add(t);
        t.setUncaughtExceptionHandler(exceptionHandler);
    }

    private class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable throwable) {
            if (uncaughtExceptions == null) {
                uncaughtExceptions = throwable;
            } else {
                uncaughtExceptions.addSuppressed(throwable);
            }
        }
    }
}
