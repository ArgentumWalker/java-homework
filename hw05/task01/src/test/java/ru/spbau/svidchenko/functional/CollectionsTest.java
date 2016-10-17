package ru.spbau.svidchenko.functional;

import org.junit.Before;
import org.junit.Test;

import javax.sql.rowset.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class CollectionsTest {
    Collection<Integer> collect;
    Function1<Integer, Integer> mapper;
    Function2<Integer, Integer, Integer> fold;
    Predicate<Integer> pred1;
    Predicate<Integer> pred2;

    @Before
    public void collectionInit() {
        collect = new LinkedList<Integer>();
        for (int i = 0; i < 10; i++) {
            collect.add(i);
        }
        mapper = new Function1<Integer, Integer>() {
            public Integer apply(Integer x) {
                return x+1;
            }
        };
        pred1 = new Predicate<Integer>() {
            public Boolean apply(Integer x) {
                return x % 2 == 0;
            }
        };
        pred2 = new Predicate<Integer>() {
            public Boolean apply(Integer x) {
                return x < 5;
            }
        };
        fold = new Function2<Integer, Integer, Integer>() {
            public Integer apply(Integer x, Integer y) {
                return x + y;
            }
        };
    }
    @Test
    public void MapTest_SuccAllValues() throws Exception {
        int i = 1;
        assertTrue(Collections.map(mapper, collect).size() == 10);
        for (Integer x : Collections.map(mapper, collect)) {
            assertTrue(x == i);
            i++;
        }
    }

    @Test
    public void FilterTest_FilterByPred1_OnlyEvenNumbers() throws Exception {
        assertTrue(Collections.filter(pred1, collect).size() == 5);
        for (Integer x : Collections.filter(pred1, collect)) {
            assertTrue(x % 2 == 0);
        }
    }

    @Test
    public void TakeWhileTest_Pred2_NumbersFrom0To4() throws Exception {
        assertTrue(Collections.takeWhile(pred2, collect).size() == 5);
        for (Integer x : Collections.takeWhile(pred2, collect)) {
            assertTrue(x < 5);
        }
    }

    @Test
    public void FoldrTest_WithFoldFunction_SumOfNumbers() throws Exception {
        assertTrue(Collections.foldr(fold, 0, collect) == 45);
    }


}