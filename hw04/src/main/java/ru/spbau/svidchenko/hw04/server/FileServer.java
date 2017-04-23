package ru.spbau.svidchenko.hw04.server;

import java.io.IOException;

/**
 * Work with files on server
 */
public interface FileServer {

    /**
     * Start work of server
     */
    void start() throws IOException;

    /**
     * Stop work of server
     */
    void stop() throws IOException;
}
