package ru.spbau.svidchenko.functional;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class Function2Test {
    private Function2<Integer, Integer, Integer> multF;
    private Function1<Integer, Integer> succF;

    @Before
    public void initFunctions() {
        multF = (x, y) -> x * y;
        succF = (x) -> x + 1;
    }

    @Test
    public void ComposeTest_SimpleComposeTest_CorrectAnswer() throws Exception {
        assertEquals(multF.compose(succF).apply(1, 2), (Integer)3);
    }

    @Test
    public void BindTest_SimpleBindTest() throws Exception {
        assertEquals(multF.bind1(2).apply(3), (Integer)6);
        assertEquals(multF.bind2(2).apply(3), (Integer)6);
    }

    @Test
    public void CurryTest_SimppleCurrryTest_CorrectAnswer() throws Exception {
        assertEquals(multF.curry().apply(2).apply(3), (Integer)6);
    }

}