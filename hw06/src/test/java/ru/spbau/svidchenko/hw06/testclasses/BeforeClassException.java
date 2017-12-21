package ru.spbau.svidchenko.hw06.testclasses;

import ru.spbau.svidchenko.hw06.annotation.BeforeClass;
import ru.spbau.svidchenko.hw06.annotation.Test;

public class BeforeClassException extends AllTestSituations{
    @BeforeClass
    public void before() throws Exception {
        throw new Exception();
    }

    @Test
    public void emptyTest() throws Exception {

    }

    @Test(expected = Exception.class)
    public void emptyTestWithExpectedException() throws Exception {

    }

    @Test(expected = Throwable.class)
    public void emptyTestWithExpectedThrowable() throws Exception {

    }

    @Test
    public void exceptionTest() throws Exception {
        throw new Exception();
    }

    @Test(expected = Exception.class)
    public void exceptionTestWithExpectedException() throws Exception {
        throw new Exception();
    }

    @Test(expected = Throwable.class)
    public void exceptionTestWithExpectedThrowable() throws Exception {
        throw new Exception();
    }

    @Test
    public void throwableTest() throws Throwable {
        throw new Throwable();
    }

    @Test(expected = Exception.class)
    public void throwableTestWithExpectedException() throws Throwable {
        throw new Throwable();
    }

    @Test(expected = Throwable.class)
    public void throwableTestWithExpectedThrowable() throws Throwable {
        throw new Throwable();
    }

    @Test(ignore = "test")
    public void ignoredTest() throws Exception {

    }

    @Test(expected = Exception.class, ignore = "test")
    public void ignoredTestWithExpectedException() throws Exception {

    }

    @Test(expected = Throwable.class, ignore = "test")
    public void ignoredTestWithExpectedThrowable() throws Exception {

    }
}
