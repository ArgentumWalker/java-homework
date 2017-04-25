package ru.spbau.svidchenko.hw04.client;

import javafx.util.Pair;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.spbau.svidchenko.hw04.server.MultithreadFileServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class SimpleFileClientTest {
    @Rule
    public TemporaryFolder fldr = new TemporaryFolder();
    private File file1;
    private File file2;
    private File dir1;

    private MultithreadFileServer server;
    private SimpleFileClient client;
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
        int port = 7000 + (int)Math.round(1000*Math.random());
        server = new MultithreadFileServer(port);
        server.start();
        client = new SimpleFileClient(server.getInetAddress(), port);
    }

    /*@Test
    public void ListTask_ListAnyDirectory_CompleteWithSuccess() throws Exception {
        /*System.out.println(fldr.getRoot().getAbsolutePath());
        for (byte b : fldr.getRoot().getAbsolutePath().getBytes()) {
            System.out.print(b + " ");
        }System.out.println();
        System.out.println(Files.exists(Paths.get(fldr.getRoot().getAbsolutePath())));
        List<Pair<String, Boolean>> result = client.listTask(fldr.getRoot().getAbsolutePath());
        System.out.println(Files.exists(Paths.get(fldr.getRoot().getAbsolutePath())));
        assertEquals(3, result.size());
        for (Pair<String, Boolean> p : result) {
            String name = p.getKey();
            boolean ans = p.getValue();
            assertTrue(answers.containsKey(name));
            assertEquals(answers.get(name), ans);
            answers.remove(name);
        }
    }*/

    @Test
    public void GetTask_GetAnyFile_CompleteWithSuccess() throws Exception {
        Pair<byte[], Long> result = client.getTask(file2.getAbsolutePath());
        assertEquals(Files.size(Paths.get(file2.getAbsolutePath())), (long) result.getValue());
        assertArrayEquals(file2Content, result.getKey());
    }
}