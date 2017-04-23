package ru.spbau.svidchenko.hw04.server;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetTaskResolverTest {
    @Rule
    public TemporaryFolder fldr = new TemporaryFolder();

    private byte[] file2Content = {6, 6, 6, 42, 13, 17, -1, -3};
    private File file1;
    private File file2;
    private PipedInputStream baseInputStream;
    private PipedOutputStream baseOutputStream;
    private DataInputStream input;
    private DataOutputStream output;
    private Socket socket;

    @Before
    public void beforeTests() throws Exception {
        file1 = fldr.newFile();
        file2 = fldr.newFile();
        FileOutputStream fileOut = new FileOutputStream(file2);
        fileOut.write(file2Content);
        fileOut.close();
        baseInputStream = new PipedInputStream();
        baseOutputStream = new PipedOutputStream(baseInputStream);
        input = new DataInputStream(baseInputStream);
        output = new DataOutputStream(baseOutputStream);
        socket = mock(Socket.class);
        when(socket.getOutputStream()).thenReturn(output);
        when(socket.getInputStream()).thenReturn(input);
    }

    @Test
    public void Get_EmptyFile_Size0() throws Exception {
        GetTaskResolver resolver = new GetTaskResolver(socket, Paths.get(file1.getAbsolutePath()));
        new Thread(resolver).start();
        long size = input.readLong();
        assertEquals(0, size);
    }

    @Test
    public void Get_NotExistedFile_Size0() throws Exception {
        GetTaskResolver resolver = new GetTaskResolver(socket, Paths.get(file1.getAbsolutePath() + "123abc"));
        new Thread(resolver).start();
        long size = input.readLong();
        assertEquals(0, size);
    }

    @Test
    public void Get_NotEmptyFile_CorrectContent() throws Exception {
        GetTaskResolver resolver = new GetTaskResolver(socket, Paths.get(file2.getAbsolutePath()));
        new Thread(resolver).start();
        long size = input.readLong();
        assertEquals(Files.size(Paths.get(file2.getAbsolutePath())), size);
        byte[] bytes = new byte[(int)size];
        input.readFully(bytes);
        assertArrayEquals(file2Content, bytes);
    }
}