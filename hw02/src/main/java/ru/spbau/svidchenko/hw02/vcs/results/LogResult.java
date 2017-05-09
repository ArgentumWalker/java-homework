package ru.spbau.svidchenko.hw02.vcs.results;

import ru.spbau.svidchenko.hw02.vcs.VCSDataController;
import ru.spbau.svidchenko.hw02.vcs.data.BranchData;
import ru.spbau.svidchenko.hw02.vcs.data.CommitData;
import ru.spbau.svidchenko.hw02.vcs.exceptions.VCSException;

import java.io.IOException;
import java.util.*;

/**
 * Result of LogTask
 * Calculates all results inside
 */
public class LogResult {
    private ArrayList<LogResultCommit> result = new ArrayList<>();

    public LogResult(VCSDataController dataController) throws VCSException, IOException {
        CommitData currentCommit = dataController.getCommitData(
                dataController.getRepositoryInfo().getCurrentCommitIndex()
        );
        BranchData currentBranch = dataController.getBranchData(currentCommit.getBranch());
        ArrayDeque<Integer> commitDeque = new ArrayDeque<>();
        commitDeque.addAll(currentBranch.getCommitIndexes());
        HashSet<Integer> addedCommits = new HashSet<>();
        addedCommits.addAll(commitDeque);
        HashMap<Integer, String> branchNames = new HashMap<>();
        branchNames.put(currentBranch.getIndex(), currentBranch.getName());
        while (!commitDeque.isEmpty()) {
            Integer id = commitDeque.removeFirst();
            CommitData commit = dataController.getCommitData(id);
            if (!branchNames.containsKey(commit.getBranch())) {
                BranchData branch = dataController.getBranchData(commit.getBranch());
                branchNames.put(branch.getIndex(), branch.getName());
            }
            result.add(new LogResultCommit(id, commit.getMessage(), branchNames.get(commit.getBranch())));
            commit.getParents().removeAll(addedCommits);
            commitDeque.addAll(commit.getParents());
            addedCommits.addAll(commit.getParents());
        }
    }

    public LogResultCommit get(int i) {
        return result.get(i);
    }

    public int length() {
        return result.size();
    }

    public ArrayList<LogResultCommit> getAll() {
        return result;
    }

    public class LogResultCommit {
        Integer commitID;
        String commitName;
        String branchName;

        private LogResultCommit(Integer commitID, String commitName, String branchName) {
            this.commitID = commitID;
            this.branchName = branchName;
            this.commitName = commitName;
        }

        public Integer getCommitID() {
            return commitID;
        }

        public String getCommitName() {
            return commitName;
        }

        public String getBranchName() {
            return branchName;
        }
    }
}
