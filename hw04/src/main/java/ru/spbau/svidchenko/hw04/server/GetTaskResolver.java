package ru.spbau.svidchenko.hw04.server;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Resolves Get task and write result to output stream
 * Output format: [size:long]([dataContent:byte])*
 * If file not exist or directory returns 0
 */
public class GetTaskResolver implements TaskResolver {
    private final static int BUFFER_SIZE = 2048;

    private OutputStream output;
    private Path path;

    public GetTaskResolver(OutputStream output, Path path) {
        this.path = path;
        this.output = output;
    }

    @Override
    public void run() {
        try {
            DataOutputStream output = new DataOutputStream(this.output);
            if (Files.exists(path) && !Files.isDirectory(path)) {
                long fileSize = Files.size(path);
                output.writeLong(fileSize);
                byte[] buffer = new byte[BUFFER_SIZE];
                try (InputStream file = Files.newInputStream(path)) {
                    long readedBytes = 0;
                    while (readedBytes != fileSize) {
                        readedBytes += file.read(buffer);
                        output.write(buffer);
                    }
                }
            } else {
                output.writeLong(0);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
