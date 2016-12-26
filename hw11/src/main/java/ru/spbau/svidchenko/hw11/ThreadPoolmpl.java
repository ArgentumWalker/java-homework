package ru.spbau.svidchenko.hw11;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.Supplier;
import java.util.function.Function;

/**
 * Multi-thread task solver.
 */
public class ThreadPoolmpl<E> {
    private final LinkedList<LightFuture<? extends E>> tasks = new LinkedList<>();
    private ArrayList<Thread> threads;

    /**
     * Create new Thread Pool
     * @param n is count of threads in pool
     */
    public ThreadPoolmpl(int n) {
        threads = new ArrayList<>();
        TaskResolver tr = new TaskResolver();
        for (int i = 0; i < n; i++) {
            threads.add(new Thread(tr));
        }
        threads.forEach(Thread::start);
    }

    /**
     * Add task to ThreadPool
     * @return LightFuture object, which will contain result of task execute
     */
    public <T extends E> LightFuture<T> addTask(Supplier<? extends T> sup) {
        LightFuture<T> task = new LightFuture<>(sup);
        synchronized (tasks) {
            tasks.addLast(task);
            tasks.notify();
        }
        return task;
    }

    /**
     * Shutdown Thread Pool, interrupt all threads inside
     */
    public void shutdown() throws InterruptedException{
        boolean interrupted = false;
        for (Thread t : threads) {
            t.interrupt();
        }
        for (Thread t : threads) {
            try {
                t.join();
            }
            catch (InterruptedException e) {
                interrupted = true;
            }
        }

        if (interrupted) {
            throw new InterruptedException();
        }
    }

    private class TaskResolver implements Runnable {

        @Override
        public void run() {
            LightFuture<? extends E> task = null;
            boolean interrupted = false;
            while (!interrupted) {
                synchronized (tasks) {
                    try {
                        while (tasks.isEmpty()) {
                            tasks.wait();
                        }
                        task = tasks.removeFirst();
                    }
                    catch (Exception e) {
                        interrupted = true;
                    }
                }
                if (!interrupted) {
                    task.run();
                }
            }
        }
    }

    /**
     * This exception means, that task throws exception when executing.
     * Check cause for more information
     */
    public static class LightExecutionException extends Exception {
        private LightExecutionException(Throwable t) {
            initCause(t);
        }
    }

    /**
     * Class, that contains information about ThreadPool task
     */
    public class LightFuture<T extends E> {
        private Supplier<? extends T> supp;
        private volatile boolean isReady;
        private final Object waitersNotifyer = new Object();
        private final LinkedList<LightFuture<? extends E>> thenApply = new LinkedList<>();
        private T result;
        private Throwable runException = null;

        private LightFuture(Supplier<? extends T> s) {
            supp = s;
        }

        private void run(){
            T tmpResult = null;
            try {
                tmpResult = supp.get();
            }
            catch (Throwable t) {
                runException = t;
            }
            synchronized (waitersNotifyer) {
                result = tmpResult;
                isReady = true;
                for (LightFuture<? extends E> task : thenApply) {
                    synchronized (tasks) {
                        tasks.addLast(task);
                        tasks.notify();
                    }
                }
                waitersNotifyer.notifyAll();
            }
        }

        /**
         * Checks, if task already solved
         */
        public boolean isReady() {
            return isReady;
        }

        /**
         * Wait until task solved, then return result
         * @throws InterruptedException
         * @throws LightExecutionException
         */
        public T get() throws InterruptedException, LightExecutionException {
            synchronized (waitersNotifyer) {
                while (!isReady) {
                    waitersNotifyer.wait();
                }
            }
            if (runException != null) {
                throw new LightExecutionException(runException);
            }
            return result;
        }

        /**
         * When execution ends, add new task to ThreadPool, which apply function to result
         */
        public <U extends E> LightFuture<U> thenApply(Function<? super T, U> f) {
            Supplier<U> supp = () -> {
                try {
                    return f.apply(get());
                }
                catch (InterruptedException | LightExecutionException e) {
                    return null;
                }
            };
            LightFuture<U> task;
            synchronized (waitersNotifyer) {
                if (isReady) {
                    task = addTask(supp);
                } else {
                    task = new LightFuture<>(supp);
                    thenApply.addLast(task);
                }
            }
            return task;
        }
    }
}
