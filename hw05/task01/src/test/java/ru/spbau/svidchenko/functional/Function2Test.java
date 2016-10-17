package ru.spbau.svidchenko.functional;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class Function2Test {
    Function2<Integer, Integer, Integer> multF;
    Function1<Integer, Integer> succF;

    @Before
    public void initFunctions() {
        multF = new Function2<Integer, Integer, Integer>() {
            public Integer apply(Integer x, Integer y) {
                return x * y;
            }
        };
        succF = new Function1<Integer, Integer>() {
            public Integer apply(Integer x) {
                return x + 1;
            }
        };
    }
    @Test
    public void ComposeTest_SimpleComposeTest_CorrectAnswer() throws Exception {
        assertTrue(multF.compose(succF).apply(1, 2) == 3);
    }

    @Test
    public void BindTest_SimpleBindTest() throws Exception {
        assertTrue(multF.bind1(2).apply(3) == 6);
        assertTrue(multF.bind2(2).apply(3) == 6);
    }

    @Test
    public void CurryTest_SimppleCurrryTest_CorrectAnswer() throws Exception {
        assertTrue(multF.curry().apply(2).apply(3) == 6);
    }

}