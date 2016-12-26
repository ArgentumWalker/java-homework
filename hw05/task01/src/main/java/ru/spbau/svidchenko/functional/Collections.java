package ru.spbau.svidchenko.functional;

import java.util.Collection;
import java.util.LinkedList;

/** Class for collection methods with functional style interfaces usage */
public final class Collections {

    /** Take f: a -> b and [a], returns [f.apply(a)] */
    static public <T, U> Collection<U> map(Function1<? super T, ? extends U> f, Collection<T> a) {
        LinkedList<U> result = new LinkedList<U>();
        for (T elem : a) {
            result.addLast(f.apply(elem));
        }
        return result;
    }

    /** Take predicate p and [a], returns [a : p.apply(a)] with elements */
    static public <T> Collection<T> filter(Predicate<? super T> p, Collection<T> a) {
        LinkedList<T> result = new LinkedList<T>();
        for (T elem : a) {
            if (p.apply(elem)) {
                result.addLast(elem);
            }
        }
        return result;
    }

    /** Take all elements with p.apply(a) until !p.apply(a) */
    static public <T> Collection<T> takeWhile(Predicate<? super T> p, Collection<T> a) {
        LinkedList<T> result = new LinkedList<T>();
        for (T elem : a) {
            if (p.apply(elem)) {
                result.addLast(elem);
            } else {
                return result;
            }
        }
        return result;
    }

    /** Like takeWhile, but with Predicate.not() */
    static public <T> Collection<T> takeUnless(Predicate<? super T> p, Collection<T> a) {
        return takeWhile(p.not(), a);
    }

    /** See Haskell::foldr */
    static public <T, U> U foldr(Function2<? super T, ? super U, ? extends U> f, U init,  Collection<T> a) {
        LinkedList<T> tmp = new LinkedList<T>();
        for (T elem : a) {
            tmp.addFirst(elem);
        }
        return foldl(f, init, tmp);
    }

    /** See Haskell::foldl */
    static public <T, U> U foldl(Function2<? super T, ? super U, ? extends U> f, U init, Collection<T> a) {
        U result = init;
        for (T elem : a) {
            result = f.apply(elem, result);
        }
        return result;
    }
}
