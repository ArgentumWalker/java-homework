package ru.spbau.svidchenko.hw04.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Read and parse task from socket, then create tasks
 * Input format: [taskID:int][path:String]
 * Known task ID's:
 * 1 - List task
 * 2 - Get task
 */
public class MultithreadAcceptorReader implements Runnable {
    private static final int LIST_TASK = 1;
    private static final int GET_TASK = 2;

    private Socket socket;
    private ThreadPoolExecutor taskThreadPool;

    public MultithreadAcceptorReader(Socket socket, ThreadPoolExecutor taskThreadPool) {
        this.socket = socket;
        this.taskThreadPool = taskThreadPool;
    }

    @Override
    public void run() {
        try {
            DataInputStream input = new DataInputStream(socket.getInputStream());
            int task = input.readInt();
            switch (task) {
                case (LIST_TASK): {
                    String path = input.readUTF();
                    taskThreadPool.execute(new ListTaskResolver(socket, Paths.get(path)));
                    break;
                }
                case (GET_TASK): {
                    String path = input.readUTF();
                    taskThreadPool.execute(new GetTaskResolver(socket, Paths.get(path)));
                    break;
                }
            }
            System.out.println(task);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
