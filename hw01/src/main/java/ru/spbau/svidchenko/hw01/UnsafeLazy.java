package ru.spbau.svidchenko.hw01;

import java.util.function.Supplier;

/**
 * Unsafe Lazy realization
 */
class UnsafeLazy<T> implements Lazy<T> {
    private Supplier<T> sup;
    private T result;

    UnsafeLazy(Supplier<T> supplier) {
        sup = supplier;
        result = null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get() {
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
