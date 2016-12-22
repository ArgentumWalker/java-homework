package ru.spbau.svidchenko.functional;

/** Function of 2 arguments in Functional Style */
public interface Function2<T, U, P> {

    /** Apply function to arguments */
    P apply(T x, U y);

    /**
     * Compose with function of 1 argument (g)
     * @return g . this - function of 2 arguments
     */
    default <Q> Function2<T, U, Q> compose(Function1<? super P, Q> g) {
        return (t, u) -> g.apply(Function2.this.apply(t, u));
    }

    /** Return function of one argument x, equivalent of this(x, y) */
    default Function1<U, P> bind1(final T x) {
        return (u) -> Function2.this.apply(x, u);
    }

    /** Return function of one argument y, equivalent of this(x, y) */
    default Function1<T, P> bind2(final U y) {
        return (t) -> Function2.this.apply(t, y);
    }

    /**
     * Return function of one argument, which returns function of one argument
     * Equivalent of this.bind1(s).apply(y) with any x, y
     */
    default Function1<T, Function1<U, P>> curry() {
        return (t) -> ((u) -> Function2.this.apply(t, u));
    }
}
