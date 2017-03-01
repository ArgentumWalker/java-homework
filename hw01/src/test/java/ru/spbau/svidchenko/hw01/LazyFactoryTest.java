package ru.spbau.svidchenko.hw01;

import org.junit.Test;

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
}