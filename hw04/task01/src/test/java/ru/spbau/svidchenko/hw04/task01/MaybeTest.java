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
        if (tmp.get() != 42) {
            fail();
        }
    }

    @Test
    public void NothingAndGetTest_GetNothing_ThrowsException() throws Exception {
        Maybe<Integer> tmp = Maybe.nothing();
        try {
            tmp.get();
            fail();
        }
        catch (MaybeNothingException e) {
            //Do nothing. It's OK;
        }
    }

    @Test
    public void IsPresentTest_IsPresentJustX_ReturnTrue() throws Exception {
        Maybe<Integer> tmp = Maybe.just(42);
        if (!tmp.isPresent()) {
            fail();
        }
    }

    @Test
    public void IsPresentTest_IsPresentNothing_ReturnFalse() throws Exception {
        Maybe<Integer> tmp = Maybe.nothing();
        if (tmp.isPresent()) {
            fail();
        }
    }

    @Test
    public void MapTest_MapNothing_ReturnNothing() throws Exception {
        Maybe<Integer> tmp = Maybe.nothing();
        //if (tmp.map(MaybeTest::notNullMapper).isPresent()) {
        if (tmp.map(n -> n + 1.0).isPresent()) {
            fail();
        }
    }

    @Test
    public void MapTest_MapJustX_ReturnJustMapperX() throws Exception {
        Maybe<Integer> tmp = Maybe.just(42);
        if (!tmp.map(n -> n + 1.0).isPresent()) {
            fail();
        }
    }

    @Test
    public void MapTest_MapperReturnNull_ReturnNothing() throws Exception {
        Maybe<Integer> tmp = Maybe.just(42);
        if (tmp.map(n -> null).isPresent()) {
            fail();
        }
    }

    private Double notNullMapper(Integer x) {
        return (x + 666.0);
    }

    private Double nullMapper(Integer x) {
        return null;
    }
}