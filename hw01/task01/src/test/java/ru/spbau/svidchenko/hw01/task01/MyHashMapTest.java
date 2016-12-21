package ru.spbau.svidchenko.hw01.task01;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests
 * Мне пришлось создавать в каждом тесте новую мапу из-за многопоточности
 */
public class MyHashMapTest {

    @Before
    public void Constructor_CreateHashmap_NoFails() throws Exception {
        MyHashMap map = new MyHashMap();
        if (map.size() != 0) {
            fail();
        }
    }

    @Test
    public void Put_PutDifferentStrings_CountIs8() {
        MyHashMap map = new MyHashMap();
        map.put("A", "A");
        map.put("B", "A");
        map.put("C", "A");
        map.put("D", "A");
        map.put("E", "A");
        map.put("F", "A");
        map.put("G", "A");
        map.put("H", "A");
        if (map.size() != 8) {
            fail();
        }
    }

    @Test
    public void Put_PutEqualStrings_CountIs8() {
        MyHashMap map = new MyHashMap();
        map.put("A", "A");
        map.put("B", "A");
        map.put("C", "A");
        map.put("D", "A");
        map.put("E", "A");
        map.put("F", "A");
        map.put("G", "A");
        map.put("H", "A");
        map.put("A", "A");
        map.put("B", "A");
        map.put("C", "A");
        map.put("D", "A");
        map.put("E", "A");
        map.put("F", "A");
        map.put("G", "A");
        map.put("H", "A");
        if (map.size() != 8) {
            fail();
        }
    }

    @Test
    public void PutReturn_PutSomeStrings_ValuesEquals() {
        MyHashMap map = new MyHashMap();
        if (map.put("A", "A") != null) {
            fail();
        }
        String s = map.put("A", "B");
        if (s == null || !s.equals("A")) {
            fail();
        }
    }

    @Test
    public void Remove_RemoveSomeStrings_ReturnValues() {
        MyHashMap map = new MyHashMap();
        map.put("A", "A");
        map.put("B", "A");
        map.put("A", "B");
        if (map.remove("D") != null) {
            fail();
        }
        if (!map.remove("A").equals("B")) {
            fail();
        }
        if (!map.remove("B").equals("A")) {
            fail();
        }
        if (map.remove("A") != null) {
            fail();
        }
        if (map.size() != 0) {
            fail();
        }
    }

    @Test
    public void Get_GetSomeStrings_OK() {
        MyHashMap map = new MyHashMap();
        map.put("A", "A");
        map.put("B", "A");
        map.put("A", "B");
        if (map.get("D") != null) {
            fail();
        }
        if (!map.get("A").equals("B")) {
            System.out.println(map.get("A"));
            fail();
        }
        if (!map.get("B").equals("A")) {
            fail();
        }
    }

}