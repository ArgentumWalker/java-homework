package ru.spbau.svidchenko.hw04.server;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.junit.Assert.*;

public class MultithreadFileServerTest {
    @Rule
    public TemporaryFolder fldr = new TemporaryFolder();
    private File file1;
    private File file2;
    private File dir1;

    private MultithreadFileServer server;
    private Socket socket;
    private HashMap<String, Boolean> answers = new HashMap<>();
    private byte[] file2Content = {6, 6, 6, 42, 13, 17, -1, -3};

    @Before
    public void before() throws Exception {
        dir1 = fldr.newFolder();
        answers.put(dir1.getName(), true);
        file1 = fldr.newFile();
        answers.put(file1.getName(), false);
        file2 = fldr.newFile();
        answers.put(file2.getName(), false);
        FileOutputStream fileOut = new FileOutputStream(file2);
        fileOut.write(file2Content);
        fileOut.close();
        int port = 6000 + (int)Math.round(1000*Math.random());
        server = new MultithreadFileServer(port);
        server.start();
        socket = new Socket(server.getInetAddress(), port);
    }

    @Test
    public void ListTask_ListAnyDirectory_CompleteWithSuccess() throws Exception {
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        output.writeInt(1);
        output.writeInt(fldr.getRoot().getAbsolutePath().getBytes().length);
        output.write(fldr.getRoot().getAbsolutePath().getBytes());
        //output.writeUTF(fldr.getRoot().getAbsolutePath());
        //output.close();
        int count = (int)input.readLong();
        assertEquals(3, count);
        for (int i = 0; i < count; i++) {
            String name = input.readUTF();
            boolean ans = input.readBoolean();
            System.out.println(i + ": " + name + " (is_dir: " + ans + ")");
            assertTrue(answers.containsKey(name));
            assertEquals(answers.get(name), ans);
            answers.remove(name);
        }
        socket.close();
    }

    @Test
    public void GetTask_GetAnyFile_CompleteWithSuccess() throws Exception {
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        output.writeInt(2);
        output.writeInt(file2.getAbsolutePath().getBytes().length);
        output.write(file2.getAbsolutePath().getBytes());
        long size = input.readLong();
        assertEquals(Files.size(Paths.get(file2.getAbsolutePath())), size);
        byte[] bytes = new byte[(int)size];
        input.readFully(bytes);
        assertArrayEquals(file2Content, bytes);
    }
}