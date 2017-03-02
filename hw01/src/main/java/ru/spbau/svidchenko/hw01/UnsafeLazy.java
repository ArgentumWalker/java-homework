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
        Supplier<T> supplier = this.supplier;
        if (supplier != null && result == NULL) {
            result = supplier.get();
            this.supplier = null;
        }
        return result;
    }
}
