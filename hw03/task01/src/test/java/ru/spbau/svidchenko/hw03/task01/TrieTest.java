package ru.spbau.svidchenko.hw03.task01;

import org.junit.Before;
import org.junit.Test;
import org.omg.CORBA.portable.OutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import static org.junit.Assert.*;

/**
 * Tests for Trie class
 */
public class TrieTest {
    Trie bor;
    @Before
    public void ConstructorTest_CallTrieConstructor_ConstructionComplete() {
        bor = new Trie();
    }

    @Test
    public void ContainsTest_ContainsPushedString_True() throws Exception {
        bor.clear();
        bor.add("String1");
        bor.add("String2");
        bor.add("42");
        bor.add("Str");
        assertTrue(bor.contains("42"));
    }

    @Test
    public void ContainsTest_ContainsNotPushedString_False() throws Exception {
        bor.clear();
        bor.add("String1");
        bor.add("String2");
        bor.add("42");
        bor.add("Str");
        assertFalse(bor.contains("String"));
    }

    @Test
    public void RemoveTest_RemoveExistString_NotFail() throws Exception {
        bor.clear();
        bor.add("String1");
        bor.add("String2");
        bor.add("42");
        bor.add("Str");
        assertTrue(bor.remove("String1"));
    }

    @Test
    public void RemoveTest_RemoveNotExistString_NotFail() throws Exception {
        bor.clear();
        bor.add("String1");
        bor.add("String2");
        bor.add("42");
        bor.add("Str");
        assertFalse(bor.remove("String"));
    }

    @Test
    public void SizeTest_CountOfVertex_Return4() throws Exception {
        bor.clear();
        bor.add("String1");
        bor.add("String2");
        bor.add("42");
        bor.add("Str");
        assertEquals(4, bor.size());
    }

    @Test
    public void HowManyStartsWithPrefixTest_CountOfStringsStartingWithStr_Return3() throws Exception {
        bor.clear();
        bor.add("String1");
        bor.add("String2");
        bor.add("String2");
        bor.add("42");
        bor.add("Str");
        if (bor.howManyStartsWithPrefix("Str") != 3) {
            fail();
        }
    }

    @Test
    public void SerializeTest_SerializeAndDeserialize_CompleteOk() throws Exception {
        bor.clear();
        bor.add("String1");
        bor.add("String2");
        bor.add("42");
        bor.add("Str");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(out);
        Trie.serialize(output, bor);
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        DataInputStream input = new DataInputStream(in);
        System.out.println(out.toString());
        Trie.deserialize(input);
    }
}