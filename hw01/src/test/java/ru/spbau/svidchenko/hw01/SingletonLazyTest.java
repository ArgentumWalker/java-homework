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
        ArrayList<Thread> threads = new ArrayList<>();
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            threads.add(new Thread(() -> {
                synchronized (result) {
                    result.add(lazy.get());
                }
            }));
        }
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            try {
                t.join();
            }
            catch (InterruptedException e) {
                //Nope
            }
        }
        Integer b = result.get(0);
        for (Integer a : result) {
            assertTrue(a == b);
        }
        assertEquals(1, callsCounter);
    }

    @Test
    public void get_MuchThreadsAndNull_ResultsAreNulls() {
        callsCounter = 0;
        Lazy<Integer> lazy = LazyFactory.getSingletonLazy(() -> {callsCounter++; return null;});
        ArrayList<Thread> threads = new ArrayList<>();
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            threads.add(new Thread(() -> {
                synchronized (result) {
                    result.add(lazy.get());
                }
            }));
        }
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            try {
                t.join();
            }
            catch (InterruptedException e) {
                //Nope
            }
        }
        for (Integer a : result) {
            assertTrue(a == null);
        }
        assertEquals(1, callsCounter);
    }
}