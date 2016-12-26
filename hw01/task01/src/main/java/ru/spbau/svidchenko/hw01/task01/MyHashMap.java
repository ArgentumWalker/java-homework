package ru.spbau.svidchenko.hw01.task01;

/**
 * Simple realization of HashMap based on lists
 */
public class MyHashMap {
    private static final int INIT_SIZE = 127;
    private List[] elements;
    private int elementCount;

    /**
     * Simple constructor, hat initialize HashMap
     */
    public MyHashMap() {
        clear();
    }

    /**
     * Put value with key to HashMap
     * @return previous value with such key
     */
    public String put(String key, String value) {
        String s = elements[hash(key)].add(key, value);
        if (s == null) {
            elementCount++;
            if (elementCount == elements.length) {
                resize();
            }
        }
        return s;
    }

    /**
     * Check if hashmap contains value with such key
     */
    public boolean contains(String key) {
        return elements[hash(key)].content(key) != null;
    }

    /**
     * Return value with such key
     */
    public String get(String key) {
        return elements[hash(key)].content(key);
    }

    /**
     * Remove value with such key from hashmap
     * @return removed value
     */
    public String remove(String key) {
        String s = elements[hash(key)].remove(key);
        if (s != null) {
            elementCount--;
        }
        return s;
    }

    /**
     * Return count of elements in hashmap
     */
    public int size() {
        return elementCount;
    }

    /**
     * Remove all elements from hashmap
     */
    public void clear() {
        elementCount = 0;
        elements = new List[INIT_SIZE];
        for (int i = 0; i < INIT_SIZE; i++) {
            elements[i] = new List();
        }
    }

    private int hash(String s) {
        return Math.abs(s.hashCode()) % elements.length;
    }

    private void resize() {
        List[] oldElements = elements;
        elements = new List[2 * oldElements.length + 1];
        for (int i = 0; i < elements.length; i++) {
            elements[i] = new List();
        }
        for (int i = 0; i < oldElements.length; i++) {
            List.Iterator it = oldElements[i].getIterator();
            while (!it.isEmpty()) {
                elements[hash(it.key())].add(it.key(), it.value());
                it.next();
            }
        }
    }

}
