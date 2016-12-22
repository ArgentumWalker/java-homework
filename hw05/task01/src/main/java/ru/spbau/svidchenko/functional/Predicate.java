package ru.spbau.svidchenko.functional;

/**Subinterface for Function1< T, Boolean >*/
public interface Predicate<T> extends Function1<T, Boolean> {

    /** Return (this || pred) predicate */
    default Predicate<T> or(Predicate<T> pred) {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T x) {
                return Predicate.this.apply(x) || pred.apply(x);
            }
        };
    }

    /** Return (this && pred) predicate */
    default Predicate<T> and(Predicate<T> pred) {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T x) {
                return Predicate.this.apply(x) && pred.apply(x);
            }
        };
    }

    /** Return (not this) predicate */
    default Predicate<T> not() {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T x) {
                return !Predicate.this.apply(x);
            }
        };
    }

    /** Return predicate, that returns only true, no matter what*/
    static <T> Predicate<T> alwaysTrue() {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T x) {
                return true;
            }
        };
    }

    /** Return predicate, that returns only false, no matter what*/
    static <T> Predicate<T> alwaysFalse() {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T x) {
                return false;
            }
        };
    }


}
