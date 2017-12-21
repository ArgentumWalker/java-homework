package ru.spbau.svidchenko.hw06.annotation;

public class TestConstants {

    /**
     * Means that we shouldn't ignore test
     */
    public static final String NULL_STRING = "";

    /**
     * Means that we shouldn't expect any exception
     */
    public static final Class<? extends Throwable> NULL_EXCEPTION = NotAnException.class;
}
