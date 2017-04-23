package ru.spbau.svidchenko.hw04.server;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class MultithreadAcceptorReaderTest {
    private MultithreadAcceptorReader acceptorReader;
    private Socket socket;
    private DataOutputStream outputStream;
    private PipedInputStream baseInput;
    private PipedOutputStream baseOutput;
    private ThreadPoolExecutor executor;

    @Before
    public void before() throws Exception {
        baseInput = new PipedInputStream();
        baseOutput = new PipedOutputStream(baseInput);
        outputStream = new DataOutputStream(baseOutput);
        socket = mock(Socket.class);
        when(socket.getInputStream()).thenReturn(baseInput);
        when(socket.getOutputStream()).thenReturn(null);
        executor = mock(ThreadPoolExecutor.class);
        acceptorReader = new MultithreadAcceptorReader(socket, executor);
        new Thread(acceptorReader).start();
    }

    @Test
    public void ListTaskTest_CallListTask_Success() throws Exception {
        outputStream.writeInt(1);
        outputStream.writeUTF("String");
        TimeUnit.MILLISECONDS.sleep(100);
        verify(socket).getInputStream();
        verify(executor).execute(any());
    }

    @Test
    public void GetTaskTest_CallGetTask_Success() throws Exception {
        outputStream.writeInt(2);
        outputStream.writeUTF("String");
        TimeUnit.MILLISECONDS.sleep(100);
        verify(socket).getInputStream();
        verify(executor).execute(any());
    }
}