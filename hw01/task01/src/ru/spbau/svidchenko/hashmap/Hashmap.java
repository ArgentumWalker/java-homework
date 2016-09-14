package ru.spbau.svidchenko.hashmap;

/**
 * Resizable hashmap with lists inside without any optimizations.
 *      File contains Hashmap class, List inner class and Node inner class.
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
    public Hashmap () {
        clear();
    }
    
    /**
     *@return count of elements in Hashmap
     */
    public int size () {
        return count;
    }
    
    /**
     *@param key is the key of pair you'r looking for
     *@return true if contains any element with such key
     */
    public boolean contains (String key) {
        return (elements[key.hashCode() % mod].find(key) != null);
    }
    
    /**
     *@param key is the key of element you'r looking for
     *@return null if don't contains any element with such key or value of such element otherwise
     */ 
    public String get (String key) {
        return elements[key.hashCode() % mod].find(key);
    }
    
    /**
     *@param key is the key of element you would like to insert
     *@param value is the value of element you would like to insert
     *@return previous value of element with such key or null if don't contains such element
     */
    public String put (String key, String value) {
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
    public String remove (String key) {
        String result = elements[key.hashCode() % mod].findAndRemove(key);
        if (result == null) {
            count--;
        }
        return result;
    }  
    
    /**
     *remove all elements from hashmap and resize it
     */
    public void clear () {
        int i;
        count = 0; 
        elements = new List[INIT_SIZE];
        mod = INIT_SIZE;
        for (i = 0; i < elements.length; i++) {
            elements[i] = new List ();
        }
    } 
    
    private void sizeup () {
        int i;
        mod *= 2;
        final List[] newElements = new List [mod];
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

class List {
    private Node head;
    private Node tail;
    
    public List() {
        head = tail = null;
    }
    
    public void push_back (String key, String value) {
        Node n = new Node (key, value);
        if (tail != null) {
            tail.next = n;
            n.prev = tail;
        } else {
            tail = head = n;
        }
    }
    
    public String find (String key) {
        Node pos = findShadow(key);
        if (pos == null) {
            return null;
        }
        return new String (pos.value);
    }
    
    public String findAndRemove (String key) {
        Node pos = findShadow(key);
        String result = null;
        if (pos != null) {
            result = new String (pos.value);
            if (pos.prev != null) {
                pos.prev.next = pos.next;
            }
            if (pos.next != null) {
                pos.next.prev = pos.prev;
            }
        }
        return result;
    }
    
    public String findAndReplace (String key, String value) {
        Node pos = findShadow(key);
        String result = null;
        if (pos != null) {
            result = new String (pos.value);
            pos.value = new String (value);
        }
        return result;
    }
    
    @Override
    public int hashCode () {
        if (head == null) {
            return 0;
        }
        return head.key.hashCode();
    }
    
    private Node findShadow (String key) {
        Node pos = head;
        while (pos != null && !(pos.key.equals(key))) {
            pos = pos.next;
        }
        return pos;
    }
}

class Node {
    public Node next;
    public Node prev;
    public String key;
    public String value;
    
    public Node (String key, String value) {
       this.key = new String (key);
       this.value = new String (value);
       next = prev = null;
    }
}
