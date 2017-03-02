package ru.spbau.svidchenko.hw01;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.function.Supplier;

/**
 * Fast thread-safe implementation, but supplier can be called not once
 */
class AtomicLazy<T> implements Lazy<T> {
    private Supplier<T> supplier;
    private volatile Object result;
    private static final AtomicReferenceFieldUpdater<AtomicLazy, Object> resultUpdater =
            AtomicReferenceFieldUpdater.newUpdater(AtomicLazy.class, Object.class, "result");

    AtomicLazy(Supplier<T> supplier) {
        this.supplier = supplier;
        result = NULL;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get() {
        if (result == NULL) {
            Supplier<T> tmp = supplier;
            if (tmp != null) {
                resultUpdater.compareAndSet(this, (T) NULL, tmp.get());
                supplier = null;
            }
        }
        return (T)result;
    }
}
