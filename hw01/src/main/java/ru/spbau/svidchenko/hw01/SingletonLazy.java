package ru.spbau.svidchenko.hw01;

import java.util.function.Supplier;

/**
 * Slow thread-safe Lazy realization with once-called supplier guaranty
 */
public class SingletonLazy<T> implements Lazy<T> {
    private Supplier<T> supplier;
    private T result = (T)NULL;

    SingletonLazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get() {
        synchronized (this) {
            if (result == NULL) {
                result = supplier.get();
            }
            return result;
        }
    }
}
