package ru.spbau.svidchnko.hw04.task02;

/**
 * Created by ArgentumWalker on 12.10.16.
 */
public class TreeSet<T extends Comparable<T>> {
    private TreeSetNode<T> root;

    public TreeSet() {
        root =  null;
    }

    public int size() {
        if (root == null) {
            return 0;
        } else {
            return root.getSize();
        }
    }

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
