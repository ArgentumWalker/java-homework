package ru.spbau.svidchenko.hw02.task01;

/**
 * Resizable hashmap with lists inside without any optimizations.
 *      File contains Hashmap class only.
 *      (c) by Oleg Svidchenko
 */

public class Hashmap {
    private final int INIT_SIZE = 127;

    private int count;
    private List[] elements;
    private int mod;

    /**
     *Constructor for a Hashmap
     *Calls Hashmap::clear() inside
     */
    public Hashmap() {
        clear();
    }

    /**
     *@return count of elements in Hashmap
     */
    public int size() {
        return count;
    }

    /**
     *@param key is the key of pair you'r looking for
     *@return true if contains any element with such key
     */
    public boolean contains(String key) {
        return (elements[key.hashCode() % mod].find(key) != null);
    }

    /**
     *@param key is the key of element you'r looking for
     *@return null if don't contains any element with such key or value of such element otherwise
     */
    public String get(String key) {
        return elements[key.hashCode() % mod].find(key);
    }

    /**
     *@param key is the key of element you would like to insert
     *@param value is the value of element you would like to insert
     *@return previous value of element with such key or null if don't contains such element
     */
    public String put(String key, String value) {
        String result = elements[key.hashCode() % mod]
                .findAndReplace(key, value);
        if (result == null) {
            count++;
            if (count > mod) {
                sizeup();
            }
        }
        return result;
    }

    /**
     *@param key is the key of element you'r want to remove
     *@return null if don't contains any element with such key or value of such element otherwise
     */
    public String remove(String key) {
        String result = elements[key.hashCode() % mod].findAndRemove(key);
        if (result != null) {
            count--;
        }
        return result;
    }

    /**
     *remove all elements from hashmap and resize it
     */
    public void clear() {
        int i;
        count = 0;
        elements = new List[INIT_SIZE];
        mod = INIT_SIZE;
        for (i = 0; i < elements.length; i++) {
            elements[i] = new List ();
        }
    }

    private void sizeup() {
        int i;
        mod *= 2;
        final List[] newElements = new List[mod];
        for (i = 0; i < elements.length; i++) {
            newElements[elements[i].hashCode() % mod] = elements[i];
        }
        for (i = 0; i < newElements.length; i++) {
            if (newElements[i] == null) {
                newElements[i] = new List ();
            }
        }
    }
}

