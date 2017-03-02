package ru.spbau.svidchenko.hw01;

import java.util.function.Supplier;

/**
 * Factory for creating some Lazy class kinds
 */
public class LazyFactory {

    private LazyFactory(){}

    /**
     * Creates thread-safe lazy with fast thread synchronization, but it can call supplier more the once
     */
    public static <T> Lazy<T> getAtomicLazy(Supplier<T> supplier) {
        return new AtomicLazy<T>(supplier);
    }

    /**
     * Creates thread-safe lazy with slow thread synchronization and supplier will be called only once
     */
    public static <T> Lazy<T> getSingletonLazy(Supplier<T> supplier) {
        return new SingletonLazy<T>(supplier);
    }

    /**
     * Creates thread-unsafe, but fast realization of Lazy class
     */
    public static <T> Lazy<T> getUnsafeLazy(Supplier<T> supplier) {
        return new UnsafeLazy<T>(supplier);
    }
}
