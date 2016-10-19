package ru.spbau.svidchenko.hw03.task01;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Realization of Trie
 */
public class Trie {
    private TrieNode headNode;

    /** Generate empty trie*/
    public Trie() {
        headNode = new TrieNode();
    }

    /** Add string to Trie
     * @return true if already contains such string
     */
    public boolean add(String s) {
        TrieNode position = createTrieNode(s);
        boolean result = position.isEndOfString();
        position.setEndOfStringState(true);
        refreshTrieSizes(position);
        return result;
    }
    /** return true if contains such string */
    public boolean contains(String s) {
        TrieNode position = findTrieNode(s);
        return !(position == null || !position.isEndOfString());
    }

    /**
     * Remove string from Trie
     * @return true if contains such string
     */
    public boolean remove(String s) {
        TrieNode position = findTrieNode(s);
        if (position == null) {
            return false;
        }
        boolean result = position.isEndOfString();
        position.setEndOfStringState(false);
        refreshTrieSizes(position);
        return result;
    }
    /** return count of vertex in Trie */
    public int size() {
        return headNode.getTreeSize();
    }
    /** return count of strings in Trie */
    public int count() {
        return headNode.getStringsCount();
    }
    /** return count of strings with such prefix */
    public int howManyStartsWithPrefix(String s) {
        TrieNode position = findTrieNode(s);
        if (position == null) {
            return 0;
        }
        return position.getStringsCount();
    }
    /** serialize Trie to Output Stream*/
    public void serialize(OutputStream out) throws IOException{
        DataOutputStream outStream = new DataOutputStream(out);
        headNode.serialize(outStream);
    }
    /** deserealize Trie from InputStream */
    public void deserialize(InputStream in) throws IOException{
        headNode = new TrieNode(new DataInputStream(in));
    }
    public void clear() {
        headNode = new TrieNode();
    }

    private void refreshTrieSizes(TrieNode node) {
        while (node.parent() != null) {
            node.refreshSizes();
            node = node.parent();
        }
    }

    private TrieNode findTrieNode(String s) {
        TrieNode position = headNode;
        for (char c : s.toCharArray()) {
            if (position.nextAt(c) == null) {
                return null;
            }
            position = position.nextAt(c);
        }
        return position;
    }

    private TrieNode createTrieNode(String s) {
        TrieNode position = headNode;
        for (char c : s.toCharArray()) {
            if (position.nextAt(c) == null) {
                position.addChild(c, new TrieNode());
            }
            position = position.nextAt(c);
        }
        return position;
    }

    private class TrieNode {
        private TrieNode parent;
        private int stringsCount = 0;
        private int treeSize = 1;
        private boolean isEndOfString;
        private LinkedHashMap<Character, TrieNode> children;

        public TrieNode() {
            this(false, null);
        }
        public TrieNode(boolean endOfString, TrieNode parent) {
            this.parent = parent;
            isEndOfString = endOfString;
            if (isEndOfString) {
                stringsCount = 1;
            }
            children = new LinkedHashMap<Character, TrieNode>();
        }
        public TrieNode(DataInputStream in) throws IOException {
            int charCount = in.readInt();
            isEndOfString = in.readBoolean();
            for (int i = 0; i < charCount; i++) {
                char c = in.readChar();
                children.put(c, new TrieNode(in));
            }
            refreshSizes();
        }
        public boolean isEndOfString() {
            return isEndOfString;
        }
        public void setEndOfStringState(boolean eos) {
            isEndOfString = eos;
        }
        public void addChild(Character character, TrieNode child) {
            children.put(character, child);
        }
        public void refreshSizes() {
            if (isEndOfString) {
                stringsCount = 1;
            } else {
                stringsCount = 0;
            }
            treeSize = 1;
            for (TrieNode node : children.values()) {
                stringsCount += node.getStringsCount();
                treeSize += node.getTreeSize();
            }
        }
        public int getStringsCount() {
            return stringsCount;
        }
        public int getTreeSize() {
            return treeSize;
        }
        public TrieNode nextAt(char character) {
            return children.get(character);
        }
        public TrieNode parent() {
            return parent;
        }
        public void serialize(DataOutputStream out) throws IOException{
            out.write(children.size());
            out.writeChar('\n');
            out.writeBoolean(isEndOfString);
            out.writeChar('\n');
            for (Character c : children.keySet()) {
                out.writeChar(c);
                out.writeChar('\n');
                children.get(c).serialize(out);
            }
        }
    }
}
