package ru.spbau.svidchenko.hashmap;

import Java.lang.Math;

/**
 * Resizable hashmap with lists inside without any optimizations.
 *      File contains Hashmap class.
 */
public class Hashmap {
    private final int INIT_SIZE = 127;
    
    private int count;
    private List[] elements;
    private int mod;
    
    /**Constructor for a Hashmap*/
    public Hashmap() {
        clear();
    }
    
    /**Return count of elements in Hashmap*/
    public int size() {
        return count;
    }
    
    /**Return true if contains any element with such key*/
    public boolean contains(String key) {
        return (elements[key.hashCode() % mod].find(key) != null);
    }
    
    /**Return null if don't contains any element with such key or value of such element otherwise */ 
    public String get(String key) {
        return elements[key.hashCode() % mod].find(key);
    }
    
    /**Return previous value of element with such key or null if don't contains such element*/
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
    
    /**Return null if don't contains any element with such key or value of such element otherwise*/         
    public String remove(String key) {
        String result = elements[Math.abs(key.hashCode()) % mod].findAndRemove(key);
        if (result == null) {
            count--;
        }
        return result;
    }  
    
    /**remove all elements from hashmap and resize it*/
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
        mod *= 2;
        final List[] newElements = new List[mod];
        for (int i = 0; i < elements.length; i++) {
            newElements[elements[i].hashCode() % mod] = elements[i];
        }
        for (int i = 0; i < newElements.length; i++) {
            if (newElements[i] == null) {
                newElements[i] = new List();
            }
        }
    }
}


