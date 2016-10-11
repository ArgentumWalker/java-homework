package ru.spbau.svidchenko.hw04.task01;

import java.util.function.Function;

/**
 * class that have two states: Nothing (null) and Just X (not null)
 */
public class Maybe<T> {
    T content;

    private Maybe(T cont) {
        content = cont;
    }

    /**
     * Make Maybe contain not-null value
     * @throws MaybeJustNullException if value is null
     */
    public static <T> Maybe<T> just(T value) throws MaybeJustNullException {
        if (value == null) {
            throw new MaybeJustNullException();
        }
        return new Maybe<T>(value);
    }
    /** Make empty Maybe with nothing inside*/
    public static <T> Maybe<T> nothing() {
        return new Maybe<T>(null);
    }

    /**
     * Returns contained value
     * @throws MaybeNothingException if trying get value from Nothing
     */
    public T get() throws MaybeNothingException {
        if (content == null) {
            throw new MaybeNothingException();
        }
        return content;
    }

    /** Return False for Nothing Maybe, otherwise return True*/
    public boolean isPresent() {
        return (content != null);
    }

    /** Apply mapper to contained value. If contains Nothing returns Nothing */
    public <U> Maybe<U> map(Function<? super T, ? extends U> mapper) {
        if (content == null) {
            return Maybe.nothing();
        }
        try {
            return Maybe.just(mapper.apply(content));
        } catch (MaybeJustNullException e) {
            return Maybe.nothing();
        }
    }

}
