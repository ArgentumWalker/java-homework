package ru.spbau.svidchenko.hw08.task02;

import org.junit.Test;

import java.net.URL;
import java.io.File;

import static org.junit.Assert.*;

public class ZipReaderTest {
    @Test
    public void unzipFilesTest() throws Exception {
        URL url = ZipReaderTest.class.getResource("/TestZip.zip");
        String path = url.getPath();
        String directory = path;
        if (path.lastIndexOf("/") > -1) {
            directory = path.substring(0, path.lastIndexOf("/"));
        }
        ZipReader.unzipFiles(new File(directory), ".*");
    }

}