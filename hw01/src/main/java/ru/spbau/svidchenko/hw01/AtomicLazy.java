package ru.spbau.svidchenko.hw01;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * Fast thread-safe implementation, but supplier can be called not once
 */
class AtomicLazy<T> implements Lazy<T> {
    private Supplier<T> sup;
    private AtomicReference<T> result;

    AtomicLazy(Supplier<T> supplier) {
        sup = supplier;
        result = new AtomicReference<T>(null);
    }

    public T get() {
        if (result.get() == null) {
            T res = sup.get();
            result.compareAndSet(null, res);
        }
        return result.get();
    }
}
