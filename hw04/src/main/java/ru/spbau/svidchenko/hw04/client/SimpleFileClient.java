package ru.spbau.svidchenko.hw04.client;

import javafx.util.Pair;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple implementation of FileClient
 */
public class SimpleFileClient implements FileClient {
    private InetAddress serverAddress;
    private int serverPort;

    public SimpleFileClient(InetAddress serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    @Override
    public List<Pair<String, Boolean>> listTask(String path) throws IOException {
        try (Socket socket = new Socket(serverAddress, serverPort)) {
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            output.writeInt(1);
            output.writeUTF(path);
            ArrayList<Pair<String, Boolean>> result = new ArrayList<>();
            DataInputStream input = new DataInputStream(socket.getInputStream());
            long size = input.readLong();
            for (long i = 0; i < size; i++) {
                boolean isDirectory;
                String filename;
                filename = input.readUTF();
                isDirectory = input.readBoolean();
                result.add(new Pair<>(filename, isDirectory));
            }
            return result;
        }
        catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Pair<byte[], Long> getTask(String path) throws IOException {
        try (Socket socket = new Socket(serverAddress, serverPort)) {
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            output.writeInt(2);
            output.writeUTF(path);
            DataInputStream input = new DataInputStream(socket.getInputStream());
            long size = input.readLong();
            byte[] bytes = new byte[(int)size];
            input.readFully(bytes);
            return new Pair<>(bytes, size);
        }
        catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }
}
