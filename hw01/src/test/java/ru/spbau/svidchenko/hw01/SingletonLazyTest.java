package ru.spbau.svidchenko.hw01;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SingletonLazyTest {
    private int callsCounter = 0;

    @Test
    public void get_OnlyOneGet_ResultIs100() {
        assertEquals((Integer)100,
                LazyFactory.getSingletonLazy(() -> 100).get());
    }

    @Test
    public void get_MuchThreads_ResultsAreEquals() {
        callsCounter = 0;
        Lazy<Integer> lazy = LazyFactory.getSingletonLazy(() -> {callsCounter++; return new Integer(10000);});
        ArrayList<Object> result;
        result = LazyFactoryTest.startWithManyThreads(lazy);
        Object b = result.get(0);
        for (Object a : result) {
            assertTrue(a == b);
        }
        assertEquals(1, callsCounter);
    }

    @Test
    public void get_MuchThreadsAndNull_ResultsAreNulls() {
        callsCounter = 0;
        Lazy<Integer> lazy = LazyFactory.getSingletonLazy(() -> {callsCounter++; return null;});
        ArrayList<Object> result;
        result = LazyFactoryTest.startWithManyThreads(lazy);
        for (Object a : result) {
            assertTrue(a == null);
        }
        assertEquals(1, callsCounter);
    }
}