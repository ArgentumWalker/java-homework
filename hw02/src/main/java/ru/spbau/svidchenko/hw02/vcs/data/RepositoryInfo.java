package ru.spbau.svidchenko.hw02.vcs.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Information about current repository
 */
public class RepositoryInfo implements Serializable {
    private Integer currentCommitIndex = 0;

    /**
     * index of last committed commit
     */
    private Integer lastCommitIndex = 0;

    /**
     * index of last created branch
     */
    private Integer lastBranchIndex = 0;

    /**
     * index of last stored filedata
     */
    private Integer lastFileIndex = 0;

    /**
     * map from all branch names to their indexes
     */
    private HashMap<String, Integer> branchIDs = new HashMap<>();

    /**
     * Files added by task Add
     */
    private ArrayList<String> addedFiles = new ArrayList<>();

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

    public ArrayList<String> getAddedFiles() {
        return addedFiles;
    }

    public void setAddedFiles(ArrayList<String> addedFiles) {
        this.addedFiles = addedFiles;
    }
}
