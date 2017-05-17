package ru.spbau.svidchenko.hw06;

import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.*;

public class FileUtilTest {
    @Test
    public void loadClasses_loadCompiledClasses_5classes() throws Exception {
        assertEquals(5,
                FileUtil.loadClasses(
                        Paths.get("build", "classes", "test", "ru", "spbau", "svidchenko", "hw06", "testclasses")
                                .toAbsolutePath().toString()).size());
    }

}