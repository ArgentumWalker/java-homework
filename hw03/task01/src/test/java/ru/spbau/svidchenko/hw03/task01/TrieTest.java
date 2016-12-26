package ru.spbau.svidchenko.hw03.task01;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import static org.junit.Assert.*;

/**
 * Tests for Trie class
 */
public class TrieTest {
    private Trie trie;

    @Before
    public void ConstructorTest_CallTrieConstructor_ConstructionComplete() {
        trie = new Trie();
    }

    @Test
    public void ContainsTest_ContainsPushedString_True() throws Exception {
        trie.clear();
        trie.add("String1");
        trie.add("String2");
        trie.add("42");
        trie.add("Str");
        assertTrue(trie.contains("42"));
    }

    @Test
    public void ContainsTest_ContainsNotPushedString_False() throws Exception {
        trie.clear();
        trie.add("String1");
        trie.add("String2");
        trie.add("42");
        trie.add("Str");
        assertFalse(trie.contains("String"));
    }

    @Test
    public void RemoveTest_RemoveExistString_NotFail() throws Exception {
        trie.clear();
        trie.add("String1");
        trie.add("String2");
        trie.add("42");
        trie.add("Str");
        assertTrue(trie.remove("String1"));
    }

    @Test
    public void RemoveTest_RemoveNotExistString_NotFail() throws Exception {
        trie.clear();
        trie.add("String1");
        trie.add("String2");
        trie.add("42");
        trie.add("Str");
        assertFalse(trie.remove("String"));
    }

    @Test
    public void SizeTest_CountOfVertex_Return4() throws Exception {
        trie.clear();
        trie.add("String1");
        trie.add("String2");
        trie.add("42");
        trie.add("Str");
        assertEquals(4, trie.size());
    }

    @Test
    public void HowManyStartsWithPrefixTest_CountOfStringsStartingWithStr_Return3() throws Exception {
        trie.clear();
        trie.add("String1");
        trie.add("String2");
        trie.add("String2");
        trie.add("42");
        trie.add("Str");
        assertEquals(3, trie.howManyStartsWithPrefix("Str"));
    }

    @Test
    public void SerializeTest_SerializeAndDeserialize_CompleteOk() throws Exception {
        trie.clear();
        trie.add("String1");
        trie.add("String2");
        trie.add("42");
        trie.add("Str");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(out);
        Trie.serialize(output, trie);
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        DataInputStream input = new DataInputStream(in);
        System.out.println(out.toString());
        Trie.deserialize(input);
        assertTrue(trie.contains("String1"));
        assertTrue(trie.contains("String2"));
        assertTrue(trie.contains("42"));
        assertTrue(trie.contains("Str"));
    }
}