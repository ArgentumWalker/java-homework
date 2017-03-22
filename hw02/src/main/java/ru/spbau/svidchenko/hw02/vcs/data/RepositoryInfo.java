package ru.spbau.svidchenko.hw02.vcs.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Information about current repository
 */
public class RepositoryInfo implements Serializable {
    private Integer currentCommitIndex;

    /**
     * index of last committed commit
     */
    private Integer lastCommitIndex;

    /**
     * index of last created branch
     */
    private Integer lastBranchIndex;

    /**
     * index of last stored filedata
     */
    private Integer lastFileIndex;

    /**
     * map from all branch names to their indexes
     */
    private HashMap<String, Integer> branchIDs;

    public RepositoryInfo() {

    }

    public Integer getCurrentCommitIndex() {
        return currentCommitIndex;
    }

    public void setCurrentCommitIndex(Integer currentCommitIndex) {
        this.currentCommitIndex = currentCommitIndex;
    }

    public Integer getLastCommitIndex() {
        return lastCommitIndex;
    }

    public void setLastCommitIndex(Integer lastCommitIndex) {
        this.lastCommitIndex = lastCommitIndex;
    }

    public Integer getLastBranchIndex() {
        return lastBranchIndex;
    }

    public void setLastBranchIndex(Integer lastBranchIndex) {
        this.lastBranchIndex = lastBranchIndex;
    }

    public Integer getLastFileIndex() {
        return lastFileIndex;
    }

    public void setLastFileIndex(Integer lastFileIndex) {
        this.lastFileIndex = lastFileIndex;
    }

    public HashMap<String, Integer> getBranchIDs() {
        return branchIDs;
    }

    public void setBranchIDs(HashMap<String, Integer> branchIDs) {
        this.branchIDs = branchIDs;
    }
}
