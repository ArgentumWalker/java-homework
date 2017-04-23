package ru.spbau.svidchenko.hw04.client;

import javafx.util.Pair;

import java.io.IOException;
import java.util.List;

/**
 * Client for connection with FileServer
 */
public interface FileClient {

    /**
     * Returns list of Pair(Filename:String, isDirectory:Boolean)
     * If file(path) is not directory or not exsist then returns empty list
     */
    List<Pair<String, Boolean>> listTask(String path) throws IOException;

    /**
     * Returns Pair(file content:byte[], file size:Long)
     * If file is empty or not exsist then size is equal to 0
     */
    Pair<byte[], Long> getTask(String path) throws IOException;
}
