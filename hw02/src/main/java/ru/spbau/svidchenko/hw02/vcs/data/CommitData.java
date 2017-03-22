package ru.spbau.svidchenko.hw02.vcs.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Data of one commit
 */
public class CommitData implements Serializable {

    /**
     * index of commit
     */
    private Integer index;

    /**
     * indexes of parent commits
     */
    private ArrayList<Integer> parents;

    /**
     * indexes of child commits
     */
    private ArrayList<Integer> childs;

    /**
     * commit branch
     */
    private Integer branch;

    /**
     * author of commit
     */
    private String user;

    /**
     * time of commit
     */
    private Date commitTime;

    /**
     * message of commit
     */
    private String message;

    /**
     * list of indexes of all tracked by commit files
     */
    private ArrayList<Integer> trackedFiles;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public ArrayList<Integer> getParents() {
        return parents;
    }

    public void setParents(ArrayList<Integer> parents) {
        this.parents = parents;
    }

    public ArrayList<Integer> getChilds() {
        return childs;
    }

    public void setChilds(ArrayList<Integer> childs) {
        this.childs = childs;
    }

    public Integer getBranch() {
        return branch;
    }

    public void setBranch(Integer branch) {
        this.branch = branch;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(Date commitTime) {
        this.commitTime = commitTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Integer> getTrackedFiles() {
        return trackedFiles;
    }

    public void setTrackedFiles(ArrayList<Integer> trackedFiles) {
        this.trackedFiles = trackedFiles;
    }
}

