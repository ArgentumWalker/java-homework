package ru.spbau.svidchenko.hw03.task01;

import org.junit.Before;
import org.junit.Test;
import org.omg.CORBA.portable.OutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

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
        if (!bor.contains("42")) {
            fail();
        }
    }

    @Test
    public void ContainsTest_ContainsNotPushedString_False() throws Exception {
        bor.clear();
        bor.add("String1");
        bor.add("String2");
        bor.add("42");
        bor.add("Str");
        if (bor.contains("String")) {
            fail();
        }
    }

    @Test
    public void RemoveTest_RemoveExistString_NotFail() throws Exception {
        bor.clear();
        bor.add("String1");
        bor.add("String2");
        bor.add("42");
        bor.add("Str");
        if (!bor.remove("String1")) {
            fail();
        }
    }

    @Test
    public void RemoveTest_RemoveNotExistString_NotFail() throws Exception {
        bor.clear();
        bor.add("String1");
        bor.add("String2");
        bor.add("42");
        bor.add("Str");
        if (bor.remove("String")) {
            fail();
        }
    }

    @Test
    public void SizeTest_CountOfVertex_Return11() throws Exception {
        bor.clear();
        bor.add("String1");
        bor.add("String2");
        bor.add("42");
        bor.add("Str");
        if (bor.size() != 11) {
            fail();
        }
    }

    @Test
    public void CountTest_CountOfStrings_Return4() throws Exception {
        bor.clear();
        bor.add("String1");
        bor.add("String2");
        bor.add("String2");
        bor.add("42");
        bor.add("Str");
        if (bor.count() != 4) {
            fail();
        }
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
        bor.serialize(out);
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        bor.deserialize(in);
    }

}