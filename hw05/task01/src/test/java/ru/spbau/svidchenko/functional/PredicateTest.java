package ru.spbau.svidchenko.functional;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PredicateTest {
    private Predicate<Integer> div2;
    private Predicate<Integer> div3;

    @Before
    public void predicateInit() {
        div2 = (x) -> x % 2 == 0;
        div3 = (x) -> x % 3 == 0;
    }

    @Test
    public void OrTest_SomeSimpleTestes_CorrectAnswers() throws Exception {
        assertTrue(div3.or(div2).apply(9));
        assertTrue(div3.or(div2).apply(8));
        assertFalse(div3.or(div2).apply(7));
        assertTrue(div3.or(div2).apply(6));
    }

    @Test
    public void AndTest_SomeSimpleTestes_CorrectAnswers() throws Exception {
        assertFalse(div3.and(div2).apply(9));
        assertFalse(div3.and(div2).apply(8));
        assertFalse(div3.and(div2).apply(7));
        assertTrue(div3.and(div2).apply(6));
    }

    @Test
    public void NotTest_SomeSimpleTests_CorrectAnswers() throws Exception {
        assertFalse(div3.not().apply(9));
        assertTrue(div3.not().apply(8));
        assertTrue(div3.not().apply(7));
    }

}