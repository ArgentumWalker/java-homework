package ru.spbau.svidchenko.hw02.task01;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for List
 */
public class ListTest {
    @Test
    public void pushbackAndFind() throws Exception {
        List list = new List();
        for (int i = 0; i < 1000; i++) {
            list.pushback(Integer.toString(i), Integer.toString(i));
        }
        for (int i = 0; i < 1000; i++) {
            if (list.find(Integer.toString(i)) == null) {
                throw new Exception("Something wrong with List::pushback or List::find");
            }
        }
    }

    @Test
    public void findAndRemove() throws Exception {
        List list = new List();
        for (int i = 0; i < 1000; i++) {
            list.pushback(Integer.toString(i), Integer.toString(i));
        }
        for (int i = 0; i < 1000; i++) {
            if (list.findAndRemove(Integer.toString(i)) == null) {
                throw new Exception("Something wrong with List::pushback or List::findAndRemove");
            }
        }
    }

    @Test
    public void findAndReplace() throws Exception {
        List list = new List();
        for (int i = 0; i < 1000; i++) {
            if (list.findAndReplace(Integer.toString(i), Integer.toString(i)) != null) {
                throw new Exception("Something wrong with List::findAndReplace or List::findAndRemove");
            }
        }
        for (int i = 0; i < 1000; i++) {
            if (list.findAndRemove(Integer.toString(i)) == null) {
                throw new Exception("Something wrong with List::findAndReplace or List::findAndRemove");
            }
        }
    }

}