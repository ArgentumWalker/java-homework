package ru.spbau.svidchenko.hw04.server;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Read and parse task from socket, then create tasks
 * Input format: [taskID:int][path:String]
 * Known task ID's:
 * 1 - List task
 * 2 - Get task
 */
public class MultithreadAcceptorSpeaker implements Runnable {
    private static final int LIST_TASK = 1;
    private static final int GET_TASK = 2;

    private ThreadPoolExecutor taskThreadPool;
    private Selector readSelector;
    private Selector writeSelector;
    private boolean interrupted = false;

    public MultithreadAcceptorSpeaker(ThreadPoolExecutor taskThreadPool) throws IOException {
        this.taskThreadPool = taskThreadPool;
        readSelector = Selector.open();
        writeSelector = Selector.open();
    }

    public void addChannel(SocketChannel c) throws IOException {
        c.configureBlocking(false);
        c.register(readSelector, SelectionKey.OP_READ).attach(new InputBuffer());
    }

    public void interrupt() {
        interrupted = true;
    }

    @Override
    public void run() {
        while (!interrupted) {
            try {
                if (readSelector.selectNow() > 0) {
                    Iterator<SelectionKey> it = readSelector.selectedKeys().iterator();
                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        InputBuffer inp = (InputBuffer)key.attachment();
                        while (inp == null) {
                            Thread.yield();
                            inp = (InputBuffer)key.attachment();
                        }
                        int readedBytes = 1;
                        while (readedBytes != 0) {
                            readedBytes = 0;
                            if (inp.task == null) {
                                readedBytes += ((SocketChannel) key.channel()).read(inp.headerBuffer);
                                if (inp.headerBuffer.position() == inp.headerBuffer.limit()) {
                                    inp.headerBuffer.flip();
                                    inp.task = inp.headerBuffer.getInt();
                                    inp.headerBuffer.clear();
                                }
                            } else if (inp.pathLength == null) {
                                readedBytes += ((SocketChannel) key.channel()).read(inp.headerBuffer);
                                if (inp.headerBuffer.position() == inp.headerBuffer.limit()) {
                                    inp.headerBuffer.flip();
                                    inp.pathLength = inp.headerBuffer.getInt();
                                    inp.pathBuffer = ByteBuffer.allocate(inp.pathLength);
                                    inp.headerBuffer = null;
                                }
                            } else if (inp.path == null) {
                                readedBytes += ((SocketChannel) key.channel()).read(inp.pathBuffer);
                                if (inp.pathBuffer.position() == inp.pathBuffer.limit()) {
                                    inp.pathBuffer.flip();
                                    inp.path = new String(inp.pathBuffer.array());
                                    inp.pathBuffer = null;
                                    Path path = Paths.get(inp.path);
                                    switch (inp.task) {
                                        case (LIST_TASK): {
                                            taskThreadPool.execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        ByteArrayOutputStream output = new ByteArrayOutputStream();
                                                        new ListTaskResolver(output, path).run();
                                                        ByteBuffer buffer = ByteBuffer.wrap(output.toByteArray());
                                                        key.channel().register(writeSelector, SelectionKey.OP_WRITE).attach(buffer);
                                                    } catch (Throwable e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                            break;
                                        }
                                        case (GET_TASK): {
                                            taskThreadPool.execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        ByteArrayOutputStream output = new ByteArrayOutputStream();
                                                        new GetTaskResolver(output, path).run();
                                                        ByteBuffer buffer = ByteBuffer.wrap(output.toByteArray());
                                                        key.channel().register(writeSelector, SelectionKey.OP_WRITE).attach(buffer);
                                                    } catch (Throwable e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                            break;
                                        }
                                    }
                                    key.cancel();
                                }
                            }
                        }
                        it.remove();
                    }
                }
                if (writeSelector.selectNow() > 0) {
                    Iterator<SelectionKey> it = writeSelector.selectedKeys().iterator();
                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        ByteBuffer buff = (ByteBuffer)key.attachment();
                        while (buff == null) {
                            Thread.yield();
                            buff = (ByteBuffer)key.attachment();
                        }
                        while (buff.hasRemaining()) {
                            ((SocketChannel)key.channel()).write(buff);
                        }
                        it.remove();
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (Throwable e) {
                e.printStackTrace();
            }
        }
        try {
            readSelector.close();
            writeSelector.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class InputBuffer {
        public ByteBuffer headerBuffer = ByteBuffer.allocate(Integer.BYTES);
        public ByteBuffer pathBuffer = null;
        public Integer task = null;
        public Integer pathLength = null;
        public String path = null;
    }
}
