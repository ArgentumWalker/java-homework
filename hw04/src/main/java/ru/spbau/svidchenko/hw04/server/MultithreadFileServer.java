package ru.spbau.svidchenko.hw04.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
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

    private Thread acceptorThread = null;
    private Thread speakerThread = null;
    private MultithreadAcceptorSpeaker speaker = null;
    private ServerSocketChannel serverSocket = null;
    private boolean interrupted = false;
    private ThreadPoolExecutor taskThreadPool = null;
    private int port;

    public MultithreadFileServer(int port) throws IOException {
        this.port = port;
        taskThreadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(TASK_THREAD_POOL_SIZE);
    }

    /**
     * Return InetAddress of server if server works
     */
    public InetAddress getInetAddress() {
        return serverSocket.socket().getInetAddress();
    }

    @Override
    public void start() throws IOException {
        serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress(port)); //= new ServerSocket(port);
        interrupted = false;
        acceptorThread = new Thread(new Acceptor());
        acceptorThread.start();
        speaker = new MultithreadAcceptorSpeaker(taskThreadPool);
        speakerThread = new Thread(speaker);
        speakerThread.start();
    }

    @Override
    public void stop() throws IOException {
        interrupted = true;
        speaker.interrupt();
        acceptorThread.interrupt();
        serverSocket.close();
        serverSocket = null;
    }

    //Class for accept sockets
    private class Acceptor implements Runnable {

        public void run() {
            while (!interrupted) {
                try {
                    SocketChannel socket = serverSocket.accept();
                    speaker.addChannel(socket);
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
