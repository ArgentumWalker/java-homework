package ru.spbau.svidchenko.hashmap;

public class List {
    private Node head;
    private Node tail;
    
    public List() {
        head = tail = null;
    }
    
    public void push_back(String key, String value) {
        Node n = new Node(key, value);
        if (tail != null) {
            tail.setNext(n);
            n.setPrevious(tail);
        } else {
            tail = head = n;
        }
    }
    
    public String find(String key) {
        Node pos = findShadow(key);
        if (pos == null) {
            return null;
        }
        return pos.getValue();
    }
    
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
    
    public String findAndReplace(String key, String value) {
        Node pos = findShadow(key);
        String result = null;
        if (pos != null) {
            result = pos.getValue();
            pos.setValue(value);
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
    
    private Node findShadow(String key) {
        Node pos = head;
        while (pos != null && !(pos.key.equals(key))) {
            pos = pos.getNext();
        }
        return pos;
    }

    private class Node {
        private Node next;
        private Node prev;
        private String key;
        private String value;
    
        public Node(String key, String value) {
            this.key = new String(key);
            this.value = new String(value);
            next = prev = null;
        }
        
        public Node getNext() {
            return next;
        }
        
        public void setNext(Node next) {
            this.next = next;
        }
        
        public Node getPrevious() {
            return prev;
        }
        
        public void setPrevious(Node prev) {
            this.prev = prev;
        }
        
        public String getKey() {
            return key;
        }
        
        public String getValue() {
            return value;
        }
        
        public void setValue(String value) {
            this.value = new String(value);
        }
    }
}

