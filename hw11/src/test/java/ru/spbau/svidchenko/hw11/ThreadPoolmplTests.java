package ru.spbau.svidchenko.hw11;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ThreadPoolmplTests {
    private ThreadPoolmpl<Integer> poolmpl1;
    private ThreadPoolmpl<Integer> poolmpl10;

    @Before
    public void Constructor_CreateThreadPool_FinishedOK() {
        poolmpl1 = new ThreadPoolmpl<>(1);
        poolmpl10 = new ThreadPoolmpl<>(10);
    }

    @Test
    public void AddTask_AddSimpleTaskAndWaitForResult_ResultIs10() throws Exception {
        ThreadPoolmpl<Integer>.LightFuture<Integer> task = poolmpl1.addTask(() -> 5 + 5);
        assertEquals(task.get(), (Integer)10);
    }

    @Test (expected = ThreadPoolmpl.LightExecutionException.class)
    public void AddTask_AddTaskWithException_Exception() throws Exception {
        ThreadPoolmpl<Integer>.LightFuture<Integer> task = poolmpl1.addTask(() -> 1 / 0);
        task.get();
    }

    @Test
    public void AddTask_AddManyTasks_FinishedOK() {
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            ThreadPoolmpl<Integer>.LightFuture<Integer> task1 = poolmpl1.addTask(() -> finalI * finalI);
            ThreadPoolmpl<Integer>.LightFuture<Integer> task10 = poolmpl10.addTask(() -> finalI * finalI);
        }
    }

    @Test
    public void ThenApply_AddTaskAndFunction_ResultsIs10And12() throws Exception {
        ThreadPoolmpl<Integer>.LightFuture<Integer> sTask = poolmpl1.addTask(() -> 5 + 5);
        ThreadPoolmpl<Integer>.LightFuture<Integer> fTask = sTask.thenApply((i) -> i + 2);
        assertEquals(sTask.get(), (Integer)10);
        assertEquals(fTask.get(), (Integer)12);
        sTask = poolmpl10.addTask(() -> 5 + 5);
        fTask = sTask.thenApply((i) -> i + 2);
        assertEquals(sTask.get(), (Integer)10);
        assertEquals(fTask.get(), (Integer)12);
    }

    @Test
    public void ThenApply_AddTaskAndMultipleFunctions() throws Exception {
        ThreadPoolmpl<Integer>.LightFuture<Integer> task0 = poolmpl1.addTask(() -> 5 + 5);
        ThreadPoolmpl<Integer>.LightFuture<Integer> task1 = task0.thenApply((i) -> i + 2);
        ThreadPoolmpl<Integer>.LightFuture<Integer> task2 = task0.thenApply((i) -> i + 3);
        ThreadPoolmpl<Integer>.LightFuture<Integer> task3 = task0.thenApply((i) -> i + 4);
        ThreadPoolmpl<Integer>.LightFuture<Integer> task4 = task0.thenApply((i) -> i + 5);
        assertEquals(task0.get(), (Integer)10);
        assertEquals(task1.get(), (Integer)12);
        assertEquals(task2.get(), (Integer)13);
        assertEquals(task3.get(), (Integer)14);
        assertEquals(task4.get(), (Integer)15);
        task0 = poolmpl10.addTask(() -> 5 + 5);
        task1 = task0.thenApply((i) -> i + 2);
        task2 = task0.thenApply((i) -> i + 3);
        task3 = task0.thenApply((i) -> i + 4);
        task4 = task0.thenApply((i) -> i + 5);
        assertEquals(task0.get(), (Integer)10);
        assertEquals(task1.get(), (Integer)12);
        assertEquals(task2.get(), (Integer)13);
        assertEquals(task3.get(), (Integer)14);
        assertEquals(task4.get(), (Integer)15);
    }

    @Test
    public void ThenApply_AddTaskAndFunctionChain() throws Exception {
        ThreadPoolmpl<Integer>.LightFuture<Integer> task0 = poolmpl1.addTask(() -> 5 + 5);
        ThreadPoolmpl<Integer>.LightFuture<Integer> task1 = task0.thenApply((i) -> i + 2);
        ThreadPoolmpl<Integer>.LightFuture<Integer> task2 = task1.thenApply((i) -> i + 1);
        ThreadPoolmpl<Integer>.LightFuture<Integer> task3 = task2.thenApply((i) -> i + 1);
        ThreadPoolmpl<Integer>.LightFuture<Integer> task4 = task3.thenApply((i) -> i + 1);
        assertEquals(task0.get(), (Integer)10);
        assertEquals(task1.get(), (Integer)12);
        assertEquals(task2.get(), (Integer)13);
        assertEquals(task3.get(), (Integer)14);
        assertEquals(task4.get(), (Integer)15);
        task0 = poolmpl10.addTask(() -> 5 + 5);
        task1 = task0.thenApply((i) -> i + 2);
        task2 = task1.thenApply((i) -> i + 1);
        task3 = task2.thenApply((i) -> i + 1);
        task4 = task3.thenApply((i) -> i + 1);
        assertEquals(task0.get(), (Integer)10);
        assertEquals(task1.get(), (Integer)12);
        assertEquals(task2.get(), (Integer)13);
        assertEquals(task3.get(), (Integer)14);
        assertEquals(task4.get(), (Integer)15);
    }

    @After
    public void Shutdown_ShutdownThreadPull() throws Exception {
        poolmpl1.shutdown();
        poolmpl10.shutdown();
    }

}