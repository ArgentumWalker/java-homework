package ru.spbau.svidchenko.hw04.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Implementation of FileServer interface
 * Not block main thread when starts
 * Uses different threads for acceptor and resolver
 */
public class MultithreadFileServer implements FileServer {
    private static final int TASK_THREAD_POOL_SIZE = 6;
    private static final int READER_THREAD_POOL_SIZE = 2;

    private Thread acceptorThread = null;
    private ServerSocket serverSocket = null;
    private boolean interrupted = false;
    private Queue<Socket> socketQueue = new ConcurrentLinkedQueue<>();
    private ThreadPoolExecutor taskThreadPool = null;
    private ThreadPoolExecutor readerThreadPool = null;
    private int port;

    public MultithreadFileServer(int port) throws IOException {
        this.port = port;
        taskThreadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(TASK_THREAD_POOL_SIZE);
        readerThreadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(READER_THREAD_POOL_SIZE);
    }

    /**
     * Return InetAddress of server if server works
     */
    public InetAddress getInetAddress() {
        return serverSocket.getInetAddress();
    }

    @Override
    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        interrupted = false;
        acceptorThread = new Thread(new Acceptor());
        acceptorThread.start();
    }

    @Override
    public void stop() throws IOException {
        interrupted = true;
        acceptorThread.interrupt();
        serverSocket.close();
        serverSocket = null;
    }

    //Class for accept sockets
    private class Acceptor implements Runnable {

        public void run() {
            while (!interrupted) {
                try {
                    Socket socket = serverSocket.accept();
                    readerThreadPool.execute(new MultithreadAcceptorReader(socket, taskThreadPool));
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
