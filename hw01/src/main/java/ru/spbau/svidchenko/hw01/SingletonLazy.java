package ru.spbau.svidchenko.hw01;

import java.util.function.Supplier;

/**
 * Slow thread-safe Lazy realization with once-called supplier guaranty
 */
public class SingletonLazy<T> implements Lazy<T> {
    private Supplier<T> supplier;
    private volatile T result = (T)NULL;

    SingletonLazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get() {
        if (result == NULL) {
            synchronized (this) {
                if (result == NULL) {
                    result = supplier.get();
                    supplier = null;
                }
            }
        }
        return result;
    }
}
