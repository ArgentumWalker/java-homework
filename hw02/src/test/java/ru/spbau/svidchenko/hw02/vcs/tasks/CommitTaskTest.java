package ru.spbau.svidchenko.hw02.vcs.tasks;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.internal.verification.VerificationModeFactory;
import ru.spbau.svidchenko.hw02.vcs.SimpleVCSController;
import ru.spbau.svidchenko.hw02.vcs.VCSDataController;
import ru.spbau.svidchenko.hw02.vcs.VCSFilesystemDataController;
import ru.spbau.svidchenko.hw02.vcs.data.BranchData;
import ru.spbau.svidchenko.hw02.vcs.data.CommitData;
import ru.spbau.svidchenko.hw02.vcs.data.RepositoryInfo;
import ru.spbau.svidchenko.hw02.vcs.data.TrackedFileData;
import ru.spbau.svidchenko.hw02.vcs.exceptions.NotRepositoryException;
import ru.spbau.svidchenko.hw02.vcs.exceptions.VCSException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CommitTaskTest {
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
    public void commitTask_MockSimpleCommit_Success() throws Exception {
        VCSDataController dataController = mock(VCSDataController.class);
        when(dataController.getRepositoryInfo()).thenReturn(new RepositoryInfo());
        when(dataController.getCommitData(anyInt())).thenReturn(new CommitData());
        when(dataController.getBranchData(anyInt())).thenReturn(new BranchData());
        when(dataController.getTrackedFileData(anyInt())).thenReturn(new TrackedFileData());
        when(dataController.getAllFiles(anyString())).thenReturn(new ArrayList<>());
        when(dataController.getChangedFiles(anyString())).thenReturn(new ArrayList<>());

        controller = new SimpleVCSController(dataController);
        controller.add("");
        verify(dataController).getRepositoryInfo(); //This for getting current commit
        verify(dataController).getChangedFiles(""); //This for getting only changed files
        verify(dataController).getCommitData(0); //This for getting tracked by current commit files
        verify(dataController).getAllFiles(""); //This for adding new files
        verify(dataController).saveRepositoryInfo(any()); //This for saving added files
        controller.commit("test");
        verify(dataController, VerificationModeFactory.atLeast(2)).getRepositoryInfo();
        verify(dataController, VerificationModeFactory.times(2)).saveCommitData(any());
    }

    @Test
    public void commitTask_OneSimpleCommit_CorrectCommitExists() throws Exception {
        controller.add("");
        controller.commit("test");
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
            paths.add(fldr.getRoot().getAbsolutePath() + dataController.sep() + Paths.get(dataController.getTrackedFileData(i).getPath()));
        }

        assertTrue(paths.contains(file1.getAbsolutePath()));
        assertTrue(paths.contains(file2.getAbsolutePath()));
        assertTrue(paths.contains(file3.getAbsolutePath()));
    }
}