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
        assertEquals(8, map.size());
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
        assertEquals(8, map.size());
    }

    @Test
    public void PutReturn_PutSomeStrings_ValuesEquals() {
        MyHashMap map = new MyHashMap();
        assertEquals(map.put("A", "A"), null);
        assertEquals(map.put("A", "B"), "A");
    }

    @Test
    public void Remove_RemoveSomeStrings_ReturnValues() {
        MyHashMap map = new MyHashMap();
        map.put("A", "A");
        map.put("B", "A");
        map.put("A", "B");
        assertEquals(null, map.remove("D"));
        assertEquals("A", map.remove("B"));
        assertEquals("B", map.remove("A"));
        assertEquals(null, map.remove("A"));
        assertEquals(0, map.size());
    }

    @Test
    public void Get_GetSomeStrings_OK() {
        MyHashMap map = new MyHashMap();
        map.put("A", "A");
        map.put("B", "A");
        map.put("A", "B");
        assertEquals(null, map.get("D"));
        assertEquals("B", map.get("A"));
        assertEquals("A", map.get("B"));
    }

}