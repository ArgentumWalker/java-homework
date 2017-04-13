package ru.spbau.svidchenko.hw02.vcs.tasks;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.spbau.svidchenko.hw02.vcs.SimpleVCSController;
import ru.spbau.svidchenko.hw02.vcs.VCSFilesystemDataController;
import ru.spbau.svidchenko.hw02.vcs.data.RepositoryInfo;
import ru.spbau.svidchenko.hw02.vcs.exceptions.NotRepositoryException;
import ru.spbau.svidchenko.hw02.vcs.exceptions.VCSException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class RemoveTaskTest {
    @Rule
    public TemporaryFolder fldr = new TemporaryFolder();

    private File file1;
    private File file2;
    private File folder1;
    private File file3;
    private VCSFilesystemDataController dataController;
    private SimpleVCSController controller;

    @Before
    public void createFiles() throws IOException, VCSException {
        file1 = fldr.newFile();
        file2 = fldr.newFile();
        folder1 = fldr.newFolder();
        file3 = new File(Files.createFile(Paths.get(folder1.getAbsolutePath() + "\\file3")).toString());
        VCSFilesystemDataController.createRepository(fldr.getRoot().getAbsolutePath());
        dataController = VCSFilesystemDataController.getInstance(fldr.getRoot().getAbsolutePath());
        controller = new SimpleVCSController(dataController);
        controller.add("");
        controller.commit("test");
    }

    @Test
    public void remove_RemoveOneFile_Success() throws IOException, VCSException {
        controller.remove(file1.getName());
        RepositoryInfo info = dataController.getRepositoryInfo();
        assertEquals(1, info.getRemovedFiles().size());
        assertEquals(0, info.getAddedFiles().size());
    }

    @Test
    public void remove_RemoveDirectoryWithFile_Success() throws IOException, VCSException {
        controller.remove(folder1.getName());
        RepositoryInfo info = dataController.getRepositoryInfo();
        assertEquals(1, info.getRemovedFiles().size());
        assertEquals(0, info.getAddedFiles().size());
    }
}