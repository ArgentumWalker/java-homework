package ru.spbau.svidchenko.hw02.vcs.tasks;

import ru.spbau.svidchenko.hw02.vcs.VCSDataController;
import ru.spbau.svidchenko.hw02.vcs.data.CommitData;
import ru.spbau.svidchenko.hw02.vcs.data.RepositoryInfo;
import ru.spbau.svidchenko.hw02.vcs.exceptions.BranchNotExistException;
import ru.spbau.svidchenko.hw02.vcs.exceptions.CommitNotExistException;
import ru.spbau.svidchenko.hw02.vcs.exceptions.WrongArgumentsException;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Task for change branch/revision
 */
public class CheckoutTask implements VCSTask {
    private Integer reviewID;
    private VCSDataController dataController;

    /**
     * Simple stock constructor
     */
    public CheckoutTask(Integer reviewID, VCSDataController dataController) {
        this.dataController = dataController;
        this.reviewID = reviewID;
    }

    @Override
    public void execute() throws IOException, CommitNotExistException {
        RepositoryInfo info = dataController.getRepositoryInfo();
        CommitData currentCommit = dataController.getCommitData(info.getCurrentCommitIndex());
        CommitData newCommit;
        try {
            newCommit = dataController.getCommitData(reviewID);
        }
        catch (FileNotFoundException e) {
            throw new CommitNotExistException();
        }
        for (Integer fileDataID : currentCommit.getTrackedFiles()) {
            dataController.clearFile(fileDataID);
        }
        for (Integer fileDataID : newCommit.getTrackedFiles()) {
            dataController.restoreFile(fileDataID);
        }
        info.setCurrentCommitIndex(newCommit.getIndex());
        dataController.saveRepositoryInfo(info);
    }
}
