package ru.spbau.svidchenko.hashmap;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for List
 */
public class ListTest {
    private List list;
    @Before
    public void Constructor_CallListConstructr_ListBuilded() {
        list = new List();
        if (list == null) {
            fail("List constructor failure");
        }
    }

    @Test
    public void Find_FindExistingElement_ElementFound() throws Exception {
        List list = new List();
        for (int i = 0; i < 1000; i++) {
            list.pushback(Integer.toString(i), Integer.toString(i));
        }
        for (int i = 0; i < 1000; i++) {
            if (list.find(Integer.toString(i)) == null) {
                fail("List::pushback or List::find failure");
            }
        }
    }

    @Test
    public void FindAndRemove_RemoveExistingElement_ElementRemoved() throws Exception {
        List list = new List();
        for (int i = 0; i < 1000; i++) {
            list.pushback(Integer.toString(i), Integer.toString(i));
        }
        for (int i = 0; i < 1000; i++) {
            if (list.findAndRemove(Integer.toString(i)) == null) {
                fail("List::pushback or List::findAndRemove");
            }
        }
    }

    @Test
    public void FindAndReplace_ReplaceOrCreateElement_NowElementExist() throws Exception {
        List list = new List();
        for (int i = 0; i < 1000; i++) {
            list.findAndReplace(Integer.toString(i), Integer.toString(i));
        }
        for (int i = 0; i < 1000; i++) {
            if (list.findAndRemove(Integer.toString(i)) == null) {
                fail("List::findAndReplace or List::findAndRemove failure");
            }
        }
    }

}
