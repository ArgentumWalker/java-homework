package ru.spbau.svidchenko.functional;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class Function1Test {
    Function1<Integer, Integer> f, g;
    @Before
    public void defineFunctions() {
        f = new Function1<Integer, Integer>() {
            public Integer apply(Integer x) {
                return x + 2;
            }
        };
        g = new Function1<Integer, Integer>() {
            public Integer apply(Integer x) {
                return x * 2;
            }
        };
    }

    @Test
    public void ComposeTest_omeSimpleTests_CorrectAnswers() throws Exception {
        assertTrue(f.compose(g).apply(0) == 4);
        assertTrue(f.compose(g).apply(2) == 8);
        assertTrue(g.compose(f).apply(2) == 6);
    }

}