package ru.spbau.svidchenko.hw11;

/**
 * This exception means, that some of threads have uncaught exceptions.
 * You can found this exceptions in cause.
 */
public class ThreadUncaughtExceptionException extends Exception {
    public ThreadUncaughtExceptionException (Throwable e) {
        initCause(e);
    }
}
