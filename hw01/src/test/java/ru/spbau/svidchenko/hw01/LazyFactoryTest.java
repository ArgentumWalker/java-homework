package ru.spbau.svidchenko.hw01;

import org.junit.Test;

import java.util.ArrayList;
import java.util.function.Supplier;

import static org.junit.Assert.*;


public class LazyFactoryTest {

    @Test
    public void getters_TryToGetLazies_GettersNotFail() throws Exception {
        Supplier<Integer> sup = (() -> 120);
        LazyFactory.getAtomicLazy(sup);
        LazyFactory.getSingletonLazy(sup);
        LazyFactory.getUnsafeLazy(sup);
    }

    public static ArrayList<Object> startWithManyThreads(Lazy<? extends Object> lazy) {
        ArrayList<Object> result = new ArrayList<>();
        ArrayList<Thread> threads = new ArrayList<>();
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
        return result;
    }
}