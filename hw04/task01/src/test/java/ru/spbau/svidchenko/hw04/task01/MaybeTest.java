package ru.spbau.svidchenko.hw04.task01;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Function;

import static org.junit.Assert.*;

public class MaybeTest {
    @Test
    public void JustAndGetTest_GetJustX_ReturnX() throws Exception {
        Maybe<Integer> tmp = Maybe.just(42);
        assertEquals(tmp.get(), (Integer)42);
    }

    @Test (expected = MaybeNothingException.class)
    public void NothingAndGetTest_GetNothing_ThrowsException() throws Exception {
        Maybe<Integer> tmp = Maybe.nothing();
        tmp.get();
    }

    @Test
    public void IsPresentTest_IsPresentJustX_ReturnTrue() throws Exception {
        Maybe<Integer> tmp = Maybe.just(42);
        assertTrue(tmp.isPresent());
    }

    @Test
    public void IsPresentTest_IsPresentNothing_ReturnFalse() throws Exception {
        Maybe<Integer> tmp = Maybe.nothing();
        assertFalse(tmp.isPresent());
    }

    @Test
    public void MapTest_MapNothing_ReturnNothing() throws Exception {
        Maybe<Integer> tmp = Maybe.nothing();
        assertFalse(tmp.map(n -> n + 1.0).isPresent());
    }

    @Test
    public void MapTest_MapJustX_ReturnJustMapperX() throws Exception {
        Maybe<Integer> tmp = Maybe.just(42);
        assertTrue(tmp.map(n -> n + 1.0).isPresent());
    }

    @Test
    public void MapTest_MapperReturnNull_ReturnNothing() throws Exception {
        Maybe<Integer> tmp = Maybe.just(42);
        assertFalse(tmp.map(n -> null).isPresent());
    }
}