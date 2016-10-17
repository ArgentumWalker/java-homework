package ru.spbau.svidchenko.functional;

/**Subinterface for Function1< T, Boolean >*/
public interface Predicate<T> extends Function1<T, Boolean> {
    /** return (this || pred) predicate */
    default Predicate<T> or(Predicate<T> pred) {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T x) {
                return Predicate.this.apply(x) || pred.apply(x);
            }
        };
    }
    /** return (this && pred) predicate */
    default Predicate<T> and(Predicate<T> pred) {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T x) {
                return Predicate.this.apply(x) && pred.apply(x);
            }
        };
    }

    /** return (not this) predicate */
    default Predicate<T> not() {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T x) {
                return !Predicate.this.apply(x);
            }
        };
    }

    /** return predicate, that returns only true, no matter what*/
    static <T> Predicate<T> ALWAYS_TRUE() {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T x) {
                return true;
            }
        };
    }

    /** return predicate, that returns only false, no matter what*/
    static <T> Predicate<T> ALWAYS_FALSE() {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T x) {
                return false;
            }
        };
    }


}
