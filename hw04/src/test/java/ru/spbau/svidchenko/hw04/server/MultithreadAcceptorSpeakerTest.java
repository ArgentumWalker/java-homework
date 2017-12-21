package ru.spbau.svidchenko.hw04.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MultithreadAcceptorSpeakerTest {
    private MultithreadAcceptorSpeaker acceptorReader;
    private ThreadPoolExecutor executor;
    private ServerSocketChannel server;
    private SocketChannel socket;

    @Before
    public void before() throws Exception {
        int port = 5000 + (int)Math.round(1000*Math.random());
        server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(port));
        executor = mock(ThreadPoolExecutor.class);
        acceptorReader = new MultithreadAcceptorSpeaker(executor);
        socket = SocketChannel.open();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket.connect(server.getLocalAddress());
                }
                catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(acceptorReader).start();
        SocketChannel socketInServer = server.accept();
        acceptorReader.addChannel(socketInServer);
    }

    @Test
    public void ListTaskTest_CallListTask_Success() throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteBuffer buffer1 = ByteBuffer.allocate(Integer.BYTES * 2);
        buffer1.putInt(1);
        buffer1.putInt((int)("String".getBytes().length));
        buffer1.flip();
        ByteBuffer buffer2 = ByteBuffer.wrap("String".getBytes());
        while (buffer1.hasRemaining()) {
            socket.write(buffer1);
        }
        while (buffer2.hasRemaining()) {
            socket.write(buffer2);
        }
        TimeUnit.MILLISECONDS.sleep(100);
        verify(executor).execute(any());
    }

    @Test
    public void GetTaskTest_CallGetTask_Success() throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteBuffer buffer1 = ByteBuffer.allocate(Integer.BYTES * 2);
        buffer1.putInt(2);
        buffer1.putInt("String".getBytes().length);
        buffer1.flip();
        ByteBuffer buffer2 = ByteBuffer.wrap("String".getBytes());
        while (buffer1.hasRemaining()) {
            socket.write(buffer1);
        }
        while (buffer2.hasRemaining()) {
            socket.write(buffer2);
        }
        TimeUnit.MILLISECONDS.sleep(100);
        verify(executor).execute(any());
    }
}