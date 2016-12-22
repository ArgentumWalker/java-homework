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
    public void ComposeTest_oneSimpleTests_CorrectAnswers() throws Exception {
        assertEquals(f.compose(g).apply(0), (Integer)4);
        assertEquals(f.compose(g).apply(2), (Integer)8);
        assertEquals(g.compose(f).apply(2), (Integer)6);
    }

}