package ru.spbau.svidchenko.hw04.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Resolve ListTask and write result to output stream
 * Output format: [count:long]([filename:string][isDirectory:boolean])*
 * If file not exist or not directory returns 0
 */
public class ListTaskResolver implements TaskResolver {
    private Socket socket;
    private Path path;

    public ListTaskResolver(Socket socket, Path path) {
        this.socket = socket;
        this.path = path;
    }

    @Override
    public void run() {
        try {
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            if (Files.exists(path) && Files.isDirectory(path)) {
                List<Path> files = Files.list(path).collect(Collectors.toList());
                output.writeLong(files.size());
                for (Path p : files){
                    output.writeUTF(p.toFile().getName());
                    output.writeBoolean(Files.isDirectory(p));
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
