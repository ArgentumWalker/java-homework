package ru.spbau.svidchenko.hw02.vcs;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.spbau.svidchenko.hw02.vcs.data.BranchData;
import ru.spbau.svidchenko.hw02.vcs.data.CommitData;
import ru.spbau.svidchenko.hw02.vcs.data.RepositoryInfo;

public class VCSFilesystemDataControllerTest {
    @Rule
    public TemporaryFolder fldr = new TemporaryFolder();

    @Test
    public void createRepository_JustCallIt_ItNotFailAndCreateBaseFiles() throws Exception {
        String directory = fldr.getRoot().getAbsolutePath();
        VCSFilesystemDataController.createRepository(directory);
        VCSFilesystemDataController dataController = VCSFilesystemDataController.getInstance(directory);
        CommitData initialCommit = dataController.getCommitData(0);
        BranchData branchData = dataController.getBranchData(0);
        RepositoryInfo repositoryInfo = dataController.getRepositoryInfo();
    }
}