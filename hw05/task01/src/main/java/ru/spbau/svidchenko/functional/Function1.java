package ru.spbau.svidchenko.functional;

/**
 * Function of 1 argument in Functional Style
 */
public interface Function1<T, U> {
    /**
     * Apply function to argument
     */
    U apply(T x);

    /**
     * Compose Function this with function f
     * @return g . this function
     */
    default <P> Function1<T, P> compose(Function1<? super U, P> g) {
        return new Function1<T, P> () {
            public P apply(T x) {
                return g.apply(Function1.this.apply(x));
            }
        };
    }
}
