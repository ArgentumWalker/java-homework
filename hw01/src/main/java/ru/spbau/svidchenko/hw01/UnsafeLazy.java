package ru.spbau.svidchenko.hw01;

import java.util.function.Supplier;

/**
 * Unsafe Lazy realization
 */
class UnsafeLazy<T> implements Lazy<T> {
    private Supplier<T> supplier;
    private T result;

    UnsafeLazy(Supplier<T> supplier) {
        this.supplier = supplier;
        result = (T)NULL;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get() {
        if (result == NULL) {
            result = supplier.get();
        }
        return result;
    }
}
