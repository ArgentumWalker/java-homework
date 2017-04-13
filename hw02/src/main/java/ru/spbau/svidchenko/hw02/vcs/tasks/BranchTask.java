package ru.spbau.svidchenko.hw02.vcs.tasks;

import ru.spbau.svidchenko.hw02.vcs.VCSDataController;
import ru.spbau.svidchenko.hw02.vcs.data.BranchData;
import ru.spbau.svidchenko.hw02.vcs.data.CommitData;
import ru.spbau.svidchenko.hw02.vcs.data.RepositoryInfo;
import ru.spbau.svidchenko.hw02.vcs.data.TrackedFileData;
import ru.spbau.svidchenko.hw02.vcs.exceptions.BranchNotExistException;
import ru.spbau.svidchenko.hw02.vcs.exceptions.WrongArgumentsException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Task for create/remove branch
 */
public class BranchTask implements VCSTask {
    public static final int CREATE_TASK = 0;
    public static final int REMOVE_TASK = 1;
    private Integer task;
    private String targetBranch;
    private VCSDataController dataController;

    /**
     * Stock constructor
     * @param task is CONSTRUCTOR_TASK or REMOVE_TASK
     */
    public BranchTask(Integer task, String targetBranchName, VCSDataController dataController) throws WrongArgumentsException {
        this.dataController = dataController;
        if (task != CREATE_TASK && task != REMOVE_TASK) {
            throw new WrongArgumentsException();
        }
        targetBranch = targetBranchName;
        this.task = task;
    }

    @Override
    public void execute() throws IOException, BranchNotExistException {
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

    private void removeTask() throws IOException, BranchNotExistException {
        RepositoryInfo info = dataController.getRepositoryInfo();
        Integer branchIndex = dataController.findBranchByName(targetBranch);
        if (branchIndex == null) {
            throw new BranchNotExistException();
        }
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
