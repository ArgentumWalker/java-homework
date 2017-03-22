package ru.spbau.svidchenko.hw02.vcs.tasks;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.spbau.svidchenko.hw02.vcs.VCSDataController;
import ru.spbau.svidchenko.hw02.vcs.VCSFilesystemDataController;
import ru.spbau.svidchenko.hw02.vcs.data.BranchData;
import ru.spbau.svidchenko.hw02.vcs.data.CommitData;
import ru.spbau.svidchenko.hw02.vcs.data.RepositoryInfo;
import ru.spbau.svidchenko.hw02.vcs.data.TrackedFileData;
import ru.spbau.svidchenko.hw02.vcs.exceptions.NotRepositoryException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CommitTaskTest {
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
    public void commitTask_OneSimpleCommit_CorrectCommitExists() throws Exception {
        String[] args = {"test"};
        new CommitTask(args, dataController).execute();
        assertEquals(1, (int)dataController.getRepositoryInfo().getCurrentCommitIndex());
        CommitData data = dataController.getCommitData(dataController.getRepositoryInfo().getCurrentCommitIndex());
        RepositoryInfo info = dataController.getRepositoryInfo();
        BranchData branch = dataController.getBranchData(0);

        assertEquals(1, (int)branch.getLastCommit());
        assertEquals(2, branch.getCommitIndexes().size());

        assertEquals(1, (int)info.getCurrentCommitIndex());
        assertEquals(1, (int)info.getLastCommitIndex());

        assertEquals(data.getMessage(), "test");
        assertTrue(data.getParents().contains(0));
        assertTrue(dataController.getCommitData(0).getChilds().contains(1));
        assertEquals(0, (int)data.getBranch());
        assertEquals(1, (int)data.getIndex());
        assertEquals(3, data.getTrackedFiles().size());

        ArrayList<String> paths = new ArrayList<>();
        for (Integer i : data.getTrackedFiles()) {
            paths.add(fldr.getRoot().getAbsolutePath() + "\\" + Paths.get(dataController.getTrackedFileData(i).getPath()));
        }

        assertTrue(paths.contains(file1.getAbsolutePath()));
        assertTrue(paths.contains(file2.getAbsolutePath()));
        assertTrue(paths.contains(file3.getAbsolutePath()));
    }
}