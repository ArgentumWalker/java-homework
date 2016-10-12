package ru.spbau.svidchnko.hw04.task02;

/**
 * Set based on binary tree. Don't implements any interface;
 */
public class TreeSet<T extends Comparable<T>> {
    private TreeSetNode<T> root;

    /** simple constructor of empty set*/
    public TreeSet() {
        root =  null;
    }

    /** return size of set (number of contained elements*/
    public int size() {
        if (root == null) {
            return 0;
        } else {
            return root.getSize();
        }
    }

    /**
     * Add element to set
     * @return false if already contains such element and true otherwise
     */
    public boolean add(T element) {
        if (root == null) {
            root = new TreeSetNode<T>(element, null);
            return true;
        }
        TreeSetNode<T> previousPosition = null;
        TreeSetNode<T> position = root;
        while (position != null && !position.getValue().equals(element)) {
            previousPosition = position;
            if (element.compareTo(position.getValue()) < 0) {
                position = position.getLeft();
            } else {
                position = position.getRight();
            }
        }
        if (position == null) {
            position = new TreeSetNode<T>(element, previousPosition);
            if (previousPosition != null) {
                if (element.compareTo(previousPosition.getValue()) < 0) {
                    previousPosition.setLeft(position);
                } else {
                    previousPosition.setRight(position);
                }
            }
            while (position != null) {
                position.refreshSize();
                position = position.getParent();
            }
            return true;
        }
        return false;
    }

    /** Return true if contains such element and false otherwise */
    public boolean contains(T element) {
        TreeSetNode<T> position = root;
        while (position != null && !position.getValue().equals(element)) {
            if (element.compareTo(position.getValue()) < 0) {
                position = position.getLeft();
            } else {
                position = position.getRight();
            }
        }
        if (position != null) {
            return true;
        }
        return false;
    }

    private class TreeSetNode<T> {
        private TreeSetNode<T> parent;
        private TreeSetNode<T> left;
        private TreeSetNode<T> right;
        private T content;
        private int size;

        public TreeSetNode(T value, TreeSetNode<T> parent) {
            content = value;
            this.parent = parent;
            left = null;
            right = null;
            size = 1;
        }

        public int getSize() {
            return size;
        }

        public T getValue() {
            return content;
        }

        public TreeSetNode<T> getParent() {
            return parent;
        }

        public TreeSetNode<T> getLeft() {
            return left;
        }

        public TreeSetNode<T> getRight() {
            return right;
        }

        public void setLeft(TreeSetNode<T> left) {
            this.left = left;
        }

        public void setRight(TreeSetNode<T> right) {
            this.right = right;
        }

        public void refreshSize() {
            size = 1;
            if (left != null) {
                size += left.getSize();
            }
            if (right != null) {
                size += right.getSize();
            }
        }
    }
}
