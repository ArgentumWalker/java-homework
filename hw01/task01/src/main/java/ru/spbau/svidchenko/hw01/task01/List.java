package ru.spbau.svidchenko.hw01.task01;

/**
 * Package-private implementation of two-ordered list
 */
class List {
    private Node root;

    List() {
        root = null;
    }

    /**
     * Find value with such key
     */
    String content(String key) {
        Node pos = findNode(key);
        if (pos == null) {
            return null;
        }
        return pos.value;
    }

    /**
     * Add value with key
     */
    String add(String key, String value) {
        Node pos = findNode(key);
        if (pos != null) {
            String prevValue = pos.value;
            pos.value = value;
            return prevValue;
        }
        Node el = new Node();
        if (root != null) {
            el.next = root;
            root.prev = el;
        }
        el.key = key;
        el.value = value;
        root = el;
        return null;
    }

    /**
     * Remove value with such key
     * @return those value
     */
    String remove(String key) {
        Node pos = findNode(key);
        if (pos == null) {
            return null;
        }
        if (pos.prev != null) {
            pos.prev.next = pos.next;
        }
        if (pos.next != null) {
            pos.next.prev = pos.prev;
        }
        if (pos == root) {
            root = pos.next;
        }
        return pos.value;
    }

    /**
     * Return iterator through pairs (key, value)
     */
    Iterator getIterator() {
        return new Iterator();
    }

    private Node findNode(String key) {
        Node pos = root;
        while (pos != null) {
            if (pos.key.equals(key)) {
                return pos;
            }
            pos = pos.next;
        }
        return null;
    }

    /**
     * Iterator through list nodes
     */
    class Iterator {
        private Node state;

        private Iterator() {
            state = root;
        }

        /**
         * Check if iterator empty
         */
        boolean isEmpty() {
            return state == null;
        }

        /**
         * Return key of current node
         */
        String key() {
            if (state == null) {
                return null;
            }
            return state.key;
        }

        /**
         * Return value of current node
         */
        String value() {
            if (state == null) {
                return null;
            }
            return state.value;
        }

        /**
         * Go to next node
         */
        void next() {
            if (state != null) {
                state = state.next;
            }
        }
    }

    private class Node {
        String key;
        String value;
        Node next;
        Node prev;
    }
}
