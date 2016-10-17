package ru.spbau.svidchenko.functional;

/**
 * Function of 2 arguments in Functional Style
 */
public interface Function2<T, U, P> {
    /** Apply function to arguments */
    P apply(T x, U y);

    /**
     * Compose with function of 1 argument (g)
     * @return g . this - function of 2 arguments
     */
    default <Q> Function2<T, U, Q> compose(Function1<? super P, Q> g) {
        return new Function2<T, U, Q> () {
            @Override
            public Q apply(T x, U y) {
                return g.apply(Function2.this.apply(x, y));
            }
        };
    }

    /**return function of one argument x, equivalent of this(x, y)*/
    default Function1<U, P> bind1(final T x) {
        return new Function1<U, P> () {
            @Override
            public P apply(U y) {
                return Function2.this.apply(x, y);
            }
        };
    }

    /**return function of one argument y, equivalent of this(x, y)*/
    default Function1<T, P> bind2(final U y) {
        return new Function1<T, P> () {
            @Override
            public P apply(T x) {
                return Function2.this.apply(x, y);
            }
        };
    }

    /**
     * ruturn function of one argument, which returns function of one argument
     * Equivalent of this.bind1(s).apply(y) with any x, y
     */
    default Function1<T, Function1<U, P>> curry() {
        return new Function1<T, Function1<U, P>>() {
            @Override
            public Function1<U, P> apply(T x) {
                return new Function1<U, P>() {
                    @Override
                    public P apply(U y) {
                        return Function2.this.apply(x, y);
                    }
                };
            }
        };
    }
}
