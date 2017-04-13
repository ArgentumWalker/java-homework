package ru.spbau.svidchenko.hw02.vcs.tasks;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.spbau.svidchenko.hw02.vcs.SimpleVCSController;
import ru.spbau.svidchenko.hw02.vcs.VCSFilesystemDataController;
import ru.spbau.svidchenko.hw02.vcs.data.CommitData;
import ru.spbau.svidchenko.hw02.vcs.data.RepositoryInfo;
import ru.spbau.svidchenko.hw02.vcs.exceptions.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class MergeTaskTest {
    @Rule
    public TemporaryFolder fldr = new TemporaryFolder();

    private VCSFilesystemDataController dataController;
    private SimpleVCSController controller;
    private File file1;
    private File file2;
    private File folder1;
    private File file3;
    private File file4;

    @Before
    public void createFiles() throws IOException, VCSException {
        file1 = fldr.newFile();
        file3 = fldr.newFile();
        VCSFilesystemDataController.createRepository(fldr.getRoot().getAbsolutePath());
        dataController = VCSFilesystemDataController.getInstance(fldr.getRoot().getAbsolutePath());
        controller = new SimpleVCSController(dataController);

        controller.add("");
        controller.commit("test");
        controller.branch(BranchTask.CREATE_TASK, "test");
        file2 = fldr.newFile();
        FileOutputStream fileOut = new FileOutputStream(file2);
        fileOut.write("aba".getBytes());
        fileOut.close();
        fileOut = new FileOutputStream(file1);
        fileOut.write("aba".getBytes());
        fileOut.close();
        controller.add("");
        controller.commit("test");
        String[] args2 = {"-b", "master"};
        controller.checkout("master");
        fileOut = new FileOutputStream(file1);
        fileOut.write("baba".getBytes());
        fileOut.close();
        file4 = fldr.newFile();
        file3.delete();
        controller.add("");
        controller.commit("test");
    }

    @Test
    public void mergeTask_SimpleMergeResult_CheckCorrectness() throws Exception {
        String[] args = {"test"};
        try {
            controller.merge("test", "master"); //5 commit
            fail();
        } catch (MergeConflictsException e) {
            //Do nothing
        }
        RepositoryInfo info = dataController.getRepositoryInfo();
        CommitData currentCommit = dataController.getCommitData(info.getCurrentCommitIndex());

        System.out.println("File1: " + file1.getPath());
        System.out.println("File2: " + file2.getPath());
        System.out.println("File3: " + file3.getPath());
        System.out.println("File4: " + file4.getPath());

        assertEquals(5, (int)currentCommit.getIndex());
        assertEquals(0, (int)currentCommit.getBranch());
        assertTrue(file1.exists());
        assertTrue(file2.exists());
        assertTrue(file3.exists());
        assertTrue(file4.exists());
        assertEquals("aba", readFromFile(file2));
        assertEquals("baba", readFromFile(file1));
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

}