package ru.spbau.svidchenko.hw01;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class AtomicLazyTest {

    @Test
    public void get_OnlyOneGet_ResultIs100() {
        assertEquals((Integer)100,
                LazyFactory.getAtomicLazy(() -> 100).get());
    }

    @Test
    public void get_MuchThreads_ResultsAreEquals() {
        Lazy<Integer> lazy = LazyFactory.getAtomicLazy(() -> new Integer(10000));
        ArrayList<Object> result;
        result = LazyFactoryTest.startWithManyThreads(lazy);
        Object b = result.get(0);
        for (Object a : result) {
            assertTrue(a == b);
        }
    }

    @Test
    public void get_MuchThreadsAndNull_ResultsAreNulls() {
        Lazy<Integer> lazy = LazyFactory.getAtomicLazy(() -> null);
        ArrayList<Object> result;
        result = LazyFactoryTest.startWithManyThreads(lazy);
        for (Object a : result) {
            assertTrue(a == null);
        }
    }
}