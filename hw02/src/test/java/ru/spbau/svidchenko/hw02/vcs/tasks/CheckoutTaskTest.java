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
import ru.spbau.svidchenko.hw02.vcs.exceptions.BranchNotExistException;
import ru.spbau.svidchenko.hw02.vcs.exceptions.NotRepositoryException;
import ru.spbau.svidchenko.hw02.vcs.exceptions.VCSException;
import ru.spbau.svidchenko.hw02.vcs.exceptions.WrongArgumentsException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;


public class CheckoutTaskTest {
    @Rule
    public TemporaryFolder fldr = new TemporaryFolder();

    private File file1;
    private File file2;
    private File folder1;
    private File file3;
    private File file4;
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
        controller.branch(BranchTask.CREATE_TASK, "test");
        file4 = fldr.newFile();
        Files.delete(Paths.get(file3.getAbsolutePath()));
        Files.delete(Paths.get(folder1.getAbsolutePath()));//.delete();
        FileOutputStream fileOut = new FileOutputStream(file2);
        fileOut.write("aba".getBytes());
        fileOut.close();
        controller.add("");
        controller.commit("test");
    }

    @Test
    public void checkoutBranch_SimpleCheckout_NotFall() throws Exception {
        controller.checkout("master");
        controller.checkout("test");
    }

    private String readFromFile(File file) {
        try {
            FileInputStream fileIn = new FileInputStream(file);
            Reader r = new InputStreamReader(fileIn);
            StringBuilder builder = new StringBuilder();
            int c = 0;
            while ((c = r.read()) != -1) {
                builder.append((char) c);
            }
            fileIn.close();
            return builder.toString();
        }
        catch (IOException e) {
            return null;
        }
    }

    @Test
    public void checkoutRevision_SimpleCheckout_checkCorrectness() throws Exception {
        controller.checkout(1);

        RepositoryInfo info = dataController.getRepositoryInfo();
        CommitData currentCommit = dataController.getCommitData(info.getCurrentCommitIndex());
        BranchData currentBranch = dataController.getBranchData(currentCommit.getBranch());

        assertEquals(1, (int)currentCommit.getIndex());
        assertEquals(0, (int)currentCommit.getBranch());
        assertEquals(currentBranch.getLastCommit(), currentCommit.getIndex());
        assertTrue(file1.exists());
        assertTrue(file2.exists());
        assertTrue(file3.exists());
        assertFalse(file4.exists());
        assertEquals("", readFromFile(file2));

        //To test head
        controller.checkout(3);

        info = dataController.getRepositoryInfo();
        currentCommit = dataController.getCommitData(info.getCurrentCommitIndex());
        currentBranch = dataController.getBranchData(currentCommit.getBranch());

        assertEquals(3, (int)currentCommit.getIndex());
        assertEquals(1, (int)currentCommit.getBranch());
        assertEquals(currentBranch.getLastCommit(), currentCommit.getIndex());
        assertTrue(file1.exists());
        assertTrue(file2.exists());
        assertFalse(file3.exists());
        assertTrue(file4.exists());
        assertEquals("aba", readFromFile(file2));
    }
}