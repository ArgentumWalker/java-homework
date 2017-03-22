package ru.spbau.svidchenko.hw02.vcs.tasks;

import ru.spbau.svidchenko.hw02.vcs.VCSDataController;
import ru.spbau.svidchenko.hw02.vcs.data.CommitData;
import ru.spbau.svidchenko.hw02.vcs.data.RepositoryInfo;
import ru.spbau.svidchenko.hw02.vcs.exceptions.WrongArgumentsException;

import java.io.IOException;

/**
 * Task for change branch/revision
 */
public class CheckoutTask implements VCSTask {
    private Integer reviewID;
    private VCSDataController dataController;

    /**
     * Task for changing review
     * @param args - two arguments: first is '-b' for checkout to branch of '-c' for checkout to commit
     *             second is name of branch or index of commit
     * @param dataController - any implementation of VCSDataController
     */
    public CheckoutTask(String[] args, VCSDataController dataController) throws WrongArgumentsException, IOException {
        this.dataController = dataController;
        
        if (args.length != 2) {
            throw new WrongArgumentsException();
        }
        reviewID = -1;
        if (args[0].toLowerCase().equals("-b")) {
            Integer id = dataController.findBranchByName(args[1]);
            reviewID = dataController.getBranchData(id).getLastCommit();
        }
        if (args[0].toLowerCase().equals("-c")) {
            reviewID = Integer.decode(args[1]);
        }
        if (reviewID < -1) {
            throw new WrongArgumentsException();
        }
    }

    @Override
    public void execute() throws IOException {
        RepositoryInfo info = dataController.getRepositoryInfo();
        CommitData currentCommit = dataController.getCommitData(info.getCurrentCommitIndex());
        CommitData newCommit = dataController.getCommitData(reviewID);
        for (Integer fileDataID : currentCommit.getTrackedFiles()) {
            dataController.clearFile(fileDataID);
        }
        for (Integer fileDataID : newCommit.getTrackedFiles()) {
            dataController.restoreFile(fileDataID);
        }
        info.setCurrentCommitIndex(newCommit.getIndex());
        dataController.saveRepositoryInfo(info);
        //TODO: restore files from backups
    }
}
