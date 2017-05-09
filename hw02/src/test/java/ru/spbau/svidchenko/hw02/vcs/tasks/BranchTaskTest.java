package ru.spbau.svidchenko.hw02.vcs.tasks;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.spbau.svidchenko.hw02.vcs.SimpleVCSController;
import ru.spbau.svidchenko.hw02.vcs.VCSFilesystemDataController;
import ru.spbau.svidchenko.hw02.vcs.data.BranchData;
import ru.spbau.svidchenko.hw02.vcs.data.CommitData;
import ru.spbau.svidchenko.hw02.vcs.data.RepositoryInfo;
import ru.spbau.svidchenko.hw02.vcs.exceptions.NotRepositoryException;
import ru.spbau.svidchenko.hw02.vcs.exceptions.VCSException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class BranchTaskTest {
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
    }

    @Test
    public void createAndDeleteBranch_CreateSimpleBranch_CurrentCommitOnNewBranch() throws Exception {
        //Create
        System.out.println("My test");
        controller.add("");
        controller.commit("test");
        controller.branch(BranchTask.CREATE_TASK, "test");
        RepositoryInfo info = dataController.getRepositoryInfo();
        BranchData data = dataController.getBranchData(1);
        CommitData commit = dataController.getCommitData(2);
        System.out.println("/My test");

        assertEquals(2, (int)info.getCurrentCommitIndex());
        assertEquals(2, (int)data.getLastCommit());
        assertEquals(1, data.getCommitIndexes().size());
        assertTrue(data.getCommitIndexes().contains(2));
        assertEquals(3, commit.getTrackedFiles().size());
        assertEquals("test", data.getName());
        assertTrue(info.getBranchIDs().containsKey("test"));

        //Delete
        controller.checkout("master");
        controller.branch(BranchTask.REMOVE_TASK, "test");
        info = dataController.getRepositoryInfo();

        assertFalse(info.getBranchIDs().containsKey("test"));
        assertEquals(1, (int)info.getCurrentCommitIndex());
    }

}