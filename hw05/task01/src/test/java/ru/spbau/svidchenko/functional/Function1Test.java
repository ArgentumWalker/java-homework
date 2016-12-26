package ru.spbau.svidchenko.functional;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class Function1Test {
    private Function1<Integer, Integer> f;
    private Function1<Integer, Integer> g;

    @Before
    public void defineFunctions() {
        f = (x) -> x + 2;
        g = (x) -> x * 2;
    }

    @Test
    public void ComposeTest_oneSimpleTests_CorrectAnswers() throws Exception {
        assertEquals(f.compose(g).apply(0), (Integer)4);
        assertEquals(f.compose(g).apply(2), (Integer)8);
        assertEquals(g.compose(f).apply(2), (Integer)6);
    }

}