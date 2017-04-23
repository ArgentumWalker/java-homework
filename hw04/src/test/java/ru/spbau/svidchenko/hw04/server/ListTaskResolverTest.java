package ru.spbau.svidchenko.hw04.server;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ListTaskResolverTest {
    @Rule
    public TemporaryFolder fldr = new TemporaryFolder();

    private File dir1;
    private File dir2;
    private File dir2file1;
    private File dir2dir1;
    private File dir2file2;
    private PipedInputStream baseInputStream;
    private PipedOutputStream baseOutputStream;
    private DataInputStream input;
    private DataOutputStream output;
    private HashMap<String, Boolean> answers = new HashMap<>();
    private Socket socket;

    @Before
    public void beforeTests() throws Exception {
        dir1 = fldr.newFolder();
        dir2 = fldr.newFolder();
        dir2dir1 = Files.createDirectory(Paths.get(dir2.getAbsolutePath(), "dir1")).toFile();
        answers.put("dir1", true);
        dir2file1 = Files.createFile(Paths.get(dir2.getAbsolutePath(), "file1")).toFile();
        answers.put("file1", false);
        dir2file2 = Files.createFile(Paths.get(dir2.getAbsolutePath(), "file2")).toFile();
        answers.put("file2", false);
        baseInputStream = new PipedInputStream();
        baseOutputStream = new PipedOutputStream(baseInputStream);
        input = new DataInputStream(baseInputStream);
        output = new DataOutputStream(baseOutputStream);
        socket = mock(Socket.class);
        when(socket.getOutputStream()).thenReturn(output);
        when(socket.getInputStream()).thenReturn(input);
    }

    @Test
    public void List_EmptyDirectory_Size0() throws Exception {
        ListTaskResolver resolver = new ListTaskResolver(socket, Paths.get(dir1.getAbsolutePath()));
        new Thread(resolver).start();
        long size = input.readLong();
        assertEquals(0, size);
    }

    @Test
    public void List_NotExistedDirectory_Size0() throws Exception {
        ListTaskResolver resolver = new ListTaskResolver(socket, Paths.get(dir1.getAbsolutePath() + "abacada"));
        new Thread(resolver).start();
        long size = input.readLong();
        assertEquals(0, size);
    }

    @Test
    public void List_NotEmptyDirectory_CorrectFiles() throws Exception {
        ListTaskResolver resolver = new ListTaskResolver(socket, Paths.get(dir2.getAbsolutePath()));
        new Thread(resolver).start();
        long size = input.readLong();
        assertEquals(3, size);
        for (int i = 0; i < (int) size; i++) {
            String name = input.readUTF();
            boolean ans = input.readBoolean();
            System.out.println(i + ": " + name + " (is_dir: " + ans + ")");
            assertTrue(answers.containsKey(name));
            assertEquals(answers.get(name), ans);
            answers.remove(name);
        }
    }
}