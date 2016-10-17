package ru.spbau.svidchenko.hashmap;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Some tests for Hashmap class
 */
public class HashmapTest {
    private Hashmap map;

    @Before
    public void Constructor_ConstructNewHashmap_HashmapBuilded() throws Exception {
        map = new Hashmap();
        if (map == null) {
            fail("Failed at Hashmap constructor test");
        }
    }

    @Test
    public void Size_PutElementsAndCheckSize_SizeEqualRealSize() throws Exception {
        for (int i = 0; i < 100; i++) {
            if (map.size() != i) {
                map.clear();
                fail("Failed at Hashmap size test");
            }
            map.put(Integer.toString(i), Integer.toString(7 * i % 100));
        }
        map.put("0", "0");
        if (map.size() != 100) {
            map.clear();
            fail("Failed at Hashmap size test");
        }
        map.clear();
    }

    @Test
    public void Contains_CheckExistingOfExistElements_ElementExist() throws Exception {
        for (int i = 0; i < 100; i++) {
            map.put(Integer.toString(i), Integer.toString(7 * i % 100));
        }
        for (int i = 0; i < 100; i++) {
            if (!map.contains(Integer.toString(i))) {
                map.clear();
                fail("Fail at Hashmap contains test");
            }
        }
        map.clear();
    }

    @Test
    public void Get_GetValueOfElement_CorrectAnswer() throws Exception {
        for (int i = 0; i < 100; i++) {
            map.put(Integer.toString(i), Integer.toString(7 * i % 100));
        }
        for (int i = 0; i < 100; i++) {
            if (!map.get(Integer.toString(i)).equals(Integer.toString(7 * i % 100))) {
                map.clear();
                fail("Fail at Hashmap get test");
            }
        }
        if (!(map.get("a") == null)) {
            map.clear();
            fail("Fail at Hashmap get test");
        }
        map.clear();
    }

    @Test
    public void Put_Put100Elements_HashmapAliveAndSizeEquals100() throws Exception {
        for (int i = 0; i < 100; i++) {
            map.put(Integer.toString(i), Integer.toString(i));
        }
        if (map.size() != 100) {
            map.clear();
            fail("Fail at Hashmap put test");
        }
        map.clear();
    }

    @Test
    public void Remove_RemoveExistingAndNotExistingElements_SizeEquals0AndHashmapAlive() throws Exception {
        for (int i = 0; i < 100; i++) {
            map.put(Integer.toString(i), Integer.toString(i));
        }
        for (int i = 0; i < 101; i++) {
            System.out.println(map.remove(Integer.toString(i)));
        }
        if (map.size() != 0) {
            System.out.println("Finaly: " + map.size());
            map.clear();
            fail("Fail at Hashmap remove test");
        }
        map.clear();
    }

    @Test
    public void Clear_CallClear_SizeEquals0() throws Exception {
        Hashmap map = new Hashmap();
        for (int i = 0; i < 100; i++) {
            map.put(Integer.toString(i), Integer.toString(i));
        }
        map.clear();
        if (map.size() != 0) {
            fail("Fail at Hashmap clear test");
        }
    }
}
