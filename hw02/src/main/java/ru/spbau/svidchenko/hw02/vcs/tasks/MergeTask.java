package ru.spbau.svidchenko.hw02.vcs.tasks;

import ru.spbau.svidchenko.hw02.vcs.VCSDataController;
import ru.spbau.svidchenko.hw02.vcs.data.BranchData;
import ru.spbau.svidchenko.hw02.vcs.data.CommitData;
import ru.spbau.svidchenko.hw02.vcs.data.RepositoryInfo;
import ru.spbau.svidchenko.hw02.vcs.data.TrackedFileData;
import ru.spbau.svidchenko.hw02.vcs.exceptions.BranchNotExistException;
import ru.spbau.svidchenko.hw02.vcs.exceptions.MergeConflictsException;
import ru.spbau.svidchenko.hw02.vcs.exceptions.WrongArgumentsException;

import java.io.IOException;
import java.util.*;

/**
 * Merge branches request
 */
public class MergeTask implements VCSTask {
    private Integer donorBranch;
    private Integer targetBranch;
    private VCSDataController dataController;

    /** Simple constructor. Result will be in targetBranchID */
    public MergeTask(Integer donorBranchID, Integer targetBranchID, VCSDataController dataController) {
        this.donorBranch = donorBranchID;
        this.targetBranch = targetBranchID;
        this.dataController = dataController;
    }

    @Override
    public void execute() throws IOException, MergeConflictsException {
        BranchData donor = dataController.getBranchData(donorBranch);
        BranchData target = dataController.getBranchData(targetBranch);
        CommitData donorCommit = dataController.getCommitData(donor.getLastCommit());
        CommitData targetCommit = dataController.getCommitData(target.getLastCommit());

        ArrayList<TrackedFileData> targetCommitFiles = new ArrayList<>();
        for (Integer i : targetCommit.getTrackedFiles()) {
            targetCommitFiles.add(dataController.getTrackedFileData(i));
        }
        ArrayList<TrackedFileData> donorCommitFiles = new ArrayList<>();
        for (Integer i : donorCommit.getTrackedFiles()) {
            donorCommitFiles.add(dataController.getTrackedFileData(i));
        }
        ArrayList<Integer> indexes = targetCommit.getTrackedFiles();
        MergeConflictsException conflicts = new MergeConflictsException();

        for (TrackedFileData data : donorCommitFiles) {
            boolean changed = false;
            for (int i = 0; i < targetCommitFiles.size(); i++) {
                TrackedFileData checker = targetCommitFiles.get(i);
                if (checker.getPath().equals(data.getPath())) {
                    if (checker.getLastModifyDate().compareTo(data.getLastModifyDate()) < 0) {
                        targetCommitFiles.set(i, data);
                        indexes.set(i, data.getIndex());
                        dataController.restoreFile(data.getIndex());
                        conflicts.addConflictFile(checker.getPath(), donor.getName());
                    } else {
                        conflicts.addConflictFile(checker.getPath(), target.getName());
                    }
                    changed = true;
                }
            }
            if (!changed) {
                targetCommitFiles.add(data);
                indexes.add(data.getIndex());
                dataController.restoreFile(data.getIndex());
            }
        }
        CommitData mergeCommit = new CommitData();
        RepositoryInfo info = dataController.getRepositoryInfo();
        mergeCommit.setIndex(info.getLastCommitIndex() + 1);
        info.setLastCommitIndex(info.getLastCommitIndex() + 1);
        info.setCurrentCommitIndex(mergeCommit.getIndex());
        mergeCommit.setBranch(targetBranch);
        mergeCommit.setChilds(new ArrayList<>());
        ArrayList<Integer> parents = new ArrayList<>();
        parents.add(targetCommit.getIndex());
        parents.add(donorCommit.getIndex());
        mergeCommit.setParents(parents);
        mergeCommit.setCommitTime(new Date());
        mergeCommit.setMessage("Merge commit");
        mergeCommit.setUser(System.getProperty("user.name"));
        mergeCommit.setTrackedFiles(indexes);
        dataController.saveCommitData(mergeCommit);
        dataController.saveRepositoryInfo(info);

        if (conflicts.gerConflictFiles().size() > 0) {
            throw conflicts;
        }
    }
}
