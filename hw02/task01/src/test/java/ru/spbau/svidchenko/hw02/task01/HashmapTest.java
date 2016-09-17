package ru.spbau.svidchenko.hw02.task01;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Some tests for Hashmap class
 */
public class HashmapTest {
    @Test
    public void size() throws Exception {
        Hashmap map = new Hashmap();
        for (int i = 0; i < 100; i++) {
            if (map.size() != i) {
                throw new Exception("Size (or put) did not work");
            }
            map.put(Integer.toString(i), Integer.toString(7 * i % 100));
        }
        map.put("0", "0");
        if (map.size() != 100) {
            throw new Exception("Size (or put) did not work");
        }
    }

    @Test
    public void contains() throws Exception {
        Hashmap map = new Hashmap();
        for (int i = 0; i < 100; i++) {
            map.put(Integer.toString(i), Integer.toString(7 * i % 100));
        }
        for (int i = 0; i < 100; i++) {
            if (!map.contains(Integer.toString(i))) {
                throw new Exception("Contains (or put) did not work");
            }
        }
        if (map.contains("a")) {
            throw new Exception("Contains (or put) did not work");
        }
    }

    @Test
    public void get() throws Exception {
        Hashmap map = new Hashmap();
        for (int i = 0; i < 100; i++) {
            map.put(Integer.toString(i), Integer.toString(7 * i % 100));
        }
        for (int i = 0; i < 100; i++) {
            if (!map.get(Integer.toString(i)).equals(Integer.toString(7 * i % 100))) {
                throw new Exception("Get (or put) did not work");
            }
        }
        if (!(map.get("a") == null)) {
            throw new Exception("Get (or put) did not work");
        }
    }

    @Test
    public void put() throws Exception {
        Hashmap map = new Hashmap();
        for (int i = 0; i < 100; i++) {
            map.put(Integer.toString(i), Integer.toString(i));
        }
        if (map.size() != 100) {
            throw new Exception("Put did not work");
        }
    }

    @Test
    public void remove() throws Exception {
        Hashmap map = new Hashmap();
        for (int i = 0; i < 100; i++) {
            map.put(Integer.toString(i), Integer.toString(i));
        }
        for (int i = 0; i < 101; i++) {
            map.remove(Integer.toString(i));
        }
        if (map.size() != 0) {
            throw new Exception("Remove did not work");
        }
    }

    @Test
    public void clear() throws Exception {
        Hashmap map = new Hashmap();
        for (int i = 0; i < 100; i++) {
            map.put(Integer.toString(i), Integer.toString(i));
        }
        map.clear();
        if (map.size() != 0) {
            throw new Exception("Clear did not work");
        }
    }
}