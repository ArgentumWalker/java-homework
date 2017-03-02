package ru.spbau.svidchenko.hw01;

import org.junit.Test;

import static org.junit.Assert.*;


public class UnsafeLazyTest {

    @Test
    public void get_OnlyOneGet_ResultIs100() {
        assertEquals((Integer)100,
                LazyFactory.getUnsafeLazy(() -> 100).get());
    }
}