package ru.spbau.svidchnko.hw04.task02;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class TreeSetTest {
    TreeSet<Integer> set;

    @Before
    public void ConstructorAndAddTest_ConstructSimpleTree_ConstructionComplete() throws Exception {
        set = new TreeSet<Integer>();
        for (int i = 0; i < 20; i++) {
            assertTrue(set.add(i));
        }
    }

    @Test
    public void SizeTest_GettingSizeOfTree_CorrectAnswer() throws Exception {
        assertEquals(Integer.toString(set.size()),set.size(), 20);
    }

    @Test
    public void ContainsTest_ContainsExistValues_ReturnTrue() throws Exception {
        for (int i = 0; i < 20; i++) {
            assertTrue(set.contains(i));
        }
    }

    @Test
    public void ContainsTest_ContainsNotExistValues_ReturnFalse() throws Exception {
        for (int i = -20; i < 0; i++) {
            assertFalse(set.contains(i));
        }
        for (int i = 20; i < 40; i++) {
            assertFalse(set.contains(i));
        }
    }
}