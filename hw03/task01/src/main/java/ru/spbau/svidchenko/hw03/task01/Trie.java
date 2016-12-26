package ru.spbau.svidchenko.hw03.task01;


import java.io.*;
import java.util.*;

/**
 * Recursive realization of Trie
 */
public class Trie {
    private HashMap<Character, Trie> child;
    private boolean isEndOfString;
    private int countOfStrings;

    /** Simple constructor. Creates empty trie */
    public Trie() {
        countOfStrings = 0;
        isEndOfString = false;
        child = new HashMap<Character, Trie>();
    }

    /** Static method, which serialize Trie to DataOutputStream */
    public static void serialize(DataOutputStream output, Trie trie) throws IOException {
        int childCount = trie.child.entrySet().size();
        output.writeBoolean(trie.isEndOfString);
        output.writeInt(trie.countOfStrings);
        output.writeInt(childCount);
        for (Map.Entry<Character, Trie> entry : trie.child.entrySet()) {
            output.writeChar(entry.getKey());
            serialize(output, entry.getValue());
        }
    }

    /** Deserialize Trie from DataInputStream */
    public static Trie deserialize(DataInputStream input) throws IOException {
        Trie result = new Trie();
        int childCount;
        result.isEndOfString = input.readBoolean();
        result.countOfStrings = input.readInt();
        childCount = input.readInt();
        for (int i = 0; i < childCount; i++) {
            char key = input.readChar();
            Trie value = deserialize(input);
            result.child.put(key, value);
        }
        return result;
    }

    /**
     * Add string to Trie
     * @return false if sting already in and true otherwise
     */
    public boolean add(String s) {
        return __add(s, 0);
    }

    /**
     * Check if trie contains such string
     * @return true if string in and false otherwise
     */
    public boolean contains(String s) {
        //System.out.println("Contains:");
        Trie result = __find(s, 0);
        if (result == null) {
            //System.out.println("Null");
            return false;
        }
        //System.out.println("NotNull");
        return result.isEndOfString;
    }

    /**
     * Remove string from trie
     * @return false if there are no such string and true otherwise
     */
    public boolean remove(String s) {
        return __remove(s, 0);
    }

    /** String count in trie */
    public int size() {
        return countOfStrings;
    }

    /** String count in trie with such prefix */
    public int howManyStartsWithPrefix(String s) {
        Trie result = __find(s, 0);
        if (result == null) {
            return 0;
        }
        return result.countOfStrings;
    }

    /** Make Trie empty */
    public void clear() {
        child.clear();
        isEndOfString = false;
        countOfStrings = 0;
    }

    private boolean __add(String s, int pos) {
        if (s.length() <= pos) {
            if (!isEndOfString) {
                isEndOfString = true;
                countOfStrings++;
                return true;
            }
            return false;
        }
        if (child.get(s.charAt(pos)) == null) {
            Trie n = new Trie();
            child.put(s.charAt(pos), n);
        }
        boolean b = child.get(s.charAt(pos)).__add(s, pos + 1);
        if (b) {
            countOfStrings++;
        }
        return b;
    }

    private Trie __find(String s, int pos) {
        if (s.length() <= pos) {
            return this;
        }
        if (child.get(s.charAt(pos)) == null) {
            return null;
        }
        return child.get(s.charAt(pos)).__find(s, pos+1);
    }

    private boolean __remove(String s, int pos) {
        if (s.length() <= pos) {
            if (isEndOfString) {
                isEndOfString = false;
                countOfStrings--;
                return true;
            }
            return false;
        }
        if (child.get(s.charAt(pos)) == null) {
            return false;
        }
        boolean b = child.get(s.charAt(pos)).__remove(s, pos + 1);
        if (child.get(s.charAt(pos)).countOfStrings == 0) {
            child.remove(s.charAt(pos));
        }
        if (b) {
            countOfStrings--;
        }
        return b;
    }
}
