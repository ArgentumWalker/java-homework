package ru.spbau.svidchenko.hw01;

import java.util.function.Supplier;

/**
 * Slow thread-safe Lazy realization with once-called supplier guaranty
 */
public class SingletonLazy<T> implements Lazy<T> {
    private Supplier<T> sup;
    private T result = null;

    SingletonLazy(Supplier<T> supplier) {
        sup = supplier;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get() {
        synchronized (sup) {
            if (result == null) {
                result = sup.get();
                if (result == null) {
                    result = (T)NULL;
                }
            }
            if (result == NULL) {
                return null;
            }
            return result;
        }
    }
}
