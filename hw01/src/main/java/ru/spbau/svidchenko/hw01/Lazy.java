package ru.spbau.svidchenko.hw01;

/**
 * Interface for lazy calculation
 */
public interface Lazy<T> {

    Object NULL = new Object();

    /**
     * Should return the result of calculation
     */
    T get();
}
