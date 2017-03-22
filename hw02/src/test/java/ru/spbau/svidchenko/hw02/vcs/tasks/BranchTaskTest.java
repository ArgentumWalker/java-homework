package ru.spbau.svidchenko.hw02.vcs.tasks;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.spbau.svidchenko.hw02.vcs.VCSFilesystemDataController;
import ru.spbau.svidchenko.hw02.vcs.data.BranchData;
import ru.spbau.svidchenko.hw02.vcs.data.CommitData;
import ru.spbau.svidchenko.hw02.vcs.data.RepositoryInfo;
import ru.spbau.svidchenko.hw02.vcs.exceptions.NotRepositoryException;

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

    @Before
    public void createFiles() throws IOException, NotRepositoryException {
        file1 = fldr.newFile();
        file2 = fldr.newFile();
        folder1 = fldr.newFolder();
        file3 = new File(Files.createFile(Paths.get(folder1.getAbsolutePath() + "\\file3")).toString());
        VCSFilesystemDataController.createRepository(fldr.getRoot().getAbsolutePath());
        dataController = VCSFilesystemDataController.getInstance(fldr.getRoot().getAbsolutePath());
    }

    @Test
    public void createAndDeleteBranch_CreateSimpleBranch_CurrentCommitOnNewBranch() throws Exception {
        //Create
        String[] args = {"test"};
        new CommitTask(args, dataController).execute();
        String[] args1 = {"-c", "test"};
        new BranchTask(args1, dataController).execute();
        RepositoryInfo info = dataController.getRepositoryInfo();
        BranchData data = dataController.getBranchData(1);
        CommitData commit = dataController.getCommitData(2);

        assertEquals(2, (int)info.getCurrentCommitIndex());
        assertEquals(2, (int)data.getLastCommit());
        assertEquals(1, data.getCommitIndexes().size());
        assertTrue(data.getCommitIndexes().contains(2));
        assertEquals(3, commit.getTrackedFiles().size());
        assertEquals("test", data.getName());
        assertTrue(info.getBranchIDs().containsKey("test"));

        //Delete
        String[] args2 = {"-b", "master"};
        new CheckoutTask(args2, dataController).execute();
        args1[0] = "-r";
        new BranchTask(args1, dataController).execute();
        info = dataController.getRepositoryInfo();

        assertFalse(info.getBranchIDs().containsKey("test"));
        assertEquals(1, (int)info.getCurrentCommitIndex());
    }

}