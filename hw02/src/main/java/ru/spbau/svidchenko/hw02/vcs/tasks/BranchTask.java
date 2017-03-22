package ru.spbau.svidchenko.hw02.vcs.tasks;

import ru.spbau.svidchenko.hw02.vcs.VCSDataController;
import ru.spbau.svidchenko.hw02.vcs.data.BranchData;
import ru.spbau.svidchenko.hw02.vcs.data.CommitData;
import ru.spbau.svidchenko.hw02.vcs.data.RepositoryInfo;
import ru.spbau.svidchenko.hw02.vcs.data.TrackedFileData;
import ru.spbau.svidchenko.hw02.vcs.exceptions.WrongArgumentsException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Task for create/remove branch
 */
public class BranchTask implements VCSTask {
    private static final int CREATE_TASK = 0;
    private static final int REMOVE_TASK = 1;
    private Integer task;
    private String targetBranch;
    private VCSDataController dataController;

    /**
     * Task for create and remove branches
     * @param args - two arguments: first if '-c' for create branch and '-r' for remove
     *             second is target branch name
     * @param dataController - any implementation of VCSDataController
     */
    public BranchTask(String[] args, VCSDataController dataController) throws WrongArgumentsException {
        this.dataController = dataController;

        if (args.length != 2) {
            throw new WrongArgumentsException();
        }

        task = -1;
        if (args[0].toLowerCase().equals("-c")) {
            task = CREATE_TASK;
        }
        if (args[0].toLowerCase().equals("-r")) {
            task = REMOVE_TASK;
        }
        if (task < 0) {
            throw new WrongArgumentsException();
        }

        targetBranch = args[1];
    }

    @Override
    public void execute() throws IOException {
        switch (task) {
            case CREATE_TASK: {
                createTask();
                break;
            }
            case REMOVE_TASK: {
                removeTask();
                break;
            }
        }
    }

    private void removeTask() throws IOException {
        RepositoryInfo info = dataController.getRepositoryInfo();
        Integer branchIndex = dataController.findBranchByName(targetBranch);
        if (dataController.getCommitData(info.getCurrentCommitIndex()).getBranch() == branchIndex) {
            //TODO throw exception
        }
        BranchData branch = dataController.getBranchData(branchIndex);
        for (Integer commitIndex : branch.getCommitIndexes()) {
            CommitData commit = dataController.getCommitData(commitIndex);
            for (Integer fileDataIndex : commit.getTrackedFiles()) {
                TrackedFileData fileData = dataController.getTrackedFileData(fileDataIndex);
                fileData.setLinkCounter(fileData.getLinkCounter() - 1);
                if (fileData.getLinkCounter() <= 0) {
                    dataController.removeTrackedFileData(fileDataIndex);
                } else {
                    dataController.saveTrackedFileData(fileData);
                }
            }
            dataController.removeCommitData(commitIndex);
        }
        dataController.removeBranchData(branchIndex);
        info.getBranchIDs().remove(targetBranch);
        dataController.saveRepositoryInfo(info);
    }

    private void createTask() throws IOException {
        RepositoryInfo info = dataController.getRepositoryInfo();
        BranchData branch = new BranchData();
        CommitData commit = dataController.getCommitData(info.getCurrentCommitIndex());

        commit.setCommitTime(new Date());
        commit.setUser(System.getProperty("user.name"));
        commit.setMessage("branch initial commit");
        commit.setParents(new ArrayList<>());
        commit.getParents().add(commit.getIndex());
        commit.setChilds(new ArrayList<>());
        commit.setIndex(info.getLastCommitIndex() + 1);
        info.setLastCommitIndex(info.getLastCommitIndex() + 1);

        branch.setCommitIndexes(new ArrayList<Integer>());
        branch.getCommitIndexes().add(commit.getIndex());

        branch.setIndex(info.getLastBranchIndex() + 1);
        info.setLastBranchIndex(info.getLastBranchIndex() + 1);
        commit.setBranch(branch.getIndex());

        branch.setName(targetBranch);
        info.getBranchIDs().put(targetBranch, branch.getIndex());

        branch.setLastCommit(commit.getIndex());
        info.setCurrentCommitIndex(commit.getIndex());

        dataController.saveBranchData(branch);
        dataController.saveRepositoryInfo(info);
        dataController.saveCommitData(commit);
    }
}
