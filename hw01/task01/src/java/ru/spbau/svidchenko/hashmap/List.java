package ru.spbau.svidchenko.hw02.task01;

/**
 *      Just simple two-way List
 *      File contains List class and inner Node class.
 *      (c) by Oleg Svidchenko
 */

public class List {
    private Node head;
    private Node tail;

    /**
     * Simple constructor for empty List
     */
    public List() {
        head = tail = null;
    }

    /**
     * Put element to the end of List
     * @param key is key of element
     * @param value is value of element
     */
    public void pushback(String key, String value) {
        Node n = new Node(key, value);
        if (tail != null) {
            tail.setNext(n);
            n.setPrevious(tail);
            tail = n;
        } else {
            tail = head = n;
        }
    }

    /**
     * Find element in List
     * @param key is key of element you'r looking for
     * @return value of element with same key or null if element doesn't exist
     */
    public String find(String key) {
        Node pos = findShadow(key);
        if (pos == null) {
            return null;
        }
        return pos.getValue();
    }

    /**
     * Find element in List, then remove it
     * @param key is key of element you'r want to remove
     * @return value of removed element or null if element doesn't exist
     */
    public String findAndRemove(String key) {
        Node pos = findShadow(key);
        String result = null;
        if (pos != null) {
            result = pos.getValue();
            if (pos.getPrevious() != null) {
                pos.getPrevious().setNext(pos.getNext());
            }
            if (pos.getNext() != null) {
                pos.getNext().setPrevious(pos.getPrevious());
            }
        }
        return result;
    }

    /**
     * Find element in List and replace it's value
     * @param key is key of element you'r looking for
     * @param value is new value of element
     * @return old value of element with same key or null if element doesn't exist
     */
    public String findAndReplace(String key, String value) {
        Node pos = findShadow(key);
        String result = null;
        if (pos != null) {
            result = pos.getValue();
            pos.setValue(value);
        } else {
            pushback(key, value);
        }
        return result;
    }

    @Override
    public int hashCode() {
        if (head == null) {
            return 0;
        }
        return head.key.hashCode();
    }

    /**
     * Find element
     * @param key is key of element you'r looking for
     * @return node of list, which contains element or null if element doesn't exist
     */
    private Node findShadow(String key) {
        Node pos = head;
        while (pos != null && !(pos.getKey().equals(key))) {
            pos = pos.getNext();
        }
        return pos;
    }

    /**
     * Node of list
     */
    private class Node {
        private Node next;
        private Node prev;
        private String key;
        private String value;

        /**
         * Simple constructor.
         * @param key is key of element
         * @param value is value of element
         */
        public Node(String key, String value) {
            this.key = new String(key);
            this.value = new String(value);
            next = prev = null;
        }

        /**
         * @return next Node in List
         */
        public Node getNext() {
            return next;
        }

        /**
         * @param next is new next Node in List
         */
        public void setNext(Node next) {
            this.next = next;
        }

        /**
         * @return previous Node in List
         */
        public Node getPrevious() {
            return prev;
        }

        /**
         * @param prev is new previous Node in List
         */
        public void setPrevious(Node prev) {
            this.prev = prev;
        }

        /**
         * @return key of element
         */
        public String getKey() {
            return key;
        }

        /**
         * @return value of element
         */
        public String getValue() {
            return value;
        }

        /**
         * @param value is new value of element
         */
        public void setValue(String value) {
            this.value = new String(value);
        }
    }
}
