package ru.spbau.svidchenko.hw11;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class ThreadFinishedRuleTests {
    private Runnable simpleRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                TimeUnit.MILLISECONDS.sleep(1000);

            }
            catch (InterruptedException e) {
                //Its ok
            }
        }
    };
    private Runnable exceptionRunnable = new Runnable() {
        @Override
        public void run() {
            int a = 1 / 0;
        }
    };
    private Runnable infinityRunnable = new Runnable() {
        @Override
        public void run() {
            while (true);
        }
    };

    @Before
    public void Constructor_ConstructEmptyRule_NotFail() {
        ThreadFinishedRule threadRule;
        threadRule = new ThreadFinishedRule();
    }

    @Test
    public void Apply_EmptyThreadList_NoExceptions() throws Throwable {
        new ThreadFinishedRule().apply(
                new Statement() {
                    @Override
                    public void evaluate() throws Throwable {}
                }
        , null).evaluate();
    }

    @Test
    public void Apply_JoinedThread_NoExceptions() throws Throwable {
        ThreadFinishedRule rule = new ThreadFinishedRule();
        rule.apply(
                new Statement() {
                    @Override
                    public void evaluate() throws Throwable {
                        Thread t = new Thread(simpleRunnable);
                        rule.addThread(t);
                        t.start();
                        t.join();
                    }
                }
                , null).evaluate();
    }

    @Test (expected = ThreadAliveException.class)
    public void Apply_InfinityThread_ThreadAliveException() throws Throwable {
        ThreadFinishedRule rule = new ThreadFinishedRule();
        rule.apply(
                new Statement() {
                    @Override
                    public void evaluate() throws Throwable {
                        Thread t = new Thread(infinityRunnable);
                        rule.addThread(t);
                        t.start();
                    }
                }
                , null).evaluate();
    }

    @Test (expected = ThreadUncaughtExceptionException.class)
    public void Apply_InterruptedThread_ThreadUncaughtExceptionException() throws Throwable {
        ThreadFinishedRule rule = new ThreadFinishedRule();
        rule.apply(
                new Statement() {
                    @Override
                    public void evaluate() throws Throwable {
                        Thread t = new Thread(exceptionRunnable);
                        rule.addThread(t);
                        t.start();
                        t.join();
                    }
                }
                , null).evaluate();
    }
}