package ru.spbau.svidchenko.hw02.vcs.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Branch data
 */
public class BranchData implements Serializable {

    /**
     * index of branch
     */
    private Integer index;

    /**
     * index of last commit in branch
     */
    private Integer lastCommit;

    /**
     * name of branch
     */
    private String name;

    /**
     * list of all commits in branch
     */
    private ArrayList<Integer> commitIndexes;

    public BranchData() {
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getLastCommit() {
        return lastCommit;
    }

    public void setLastCommit(Integer lastCommit) {
        this.lastCommit = lastCommit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Integer> getCommitIndexes() {
        return commitIndexes;
    }

    public void setCommitIndexes(ArrayList<Integer> commitIndexes) {
        this.commitIndexes = commitIndexes;
    }
}
