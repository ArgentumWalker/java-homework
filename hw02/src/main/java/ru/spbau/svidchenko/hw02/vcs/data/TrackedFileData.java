package ru.spbau.svidchenko.hw02.vcs.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Serialized file data
 */
public class TrackedFileData implements Serializable {
    private Integer branchVersion;

    /**
     * index of file data bundle
     */
    private Integer index;

    /**
     * how much commits links to that bundle
     */
    private Integer linkCounter;

    /**
     * person who committed this file last
     */
    private String lastModifyUser;

    /**
     * bundle creation date
     */
    private Date lastModifyDate;

    /**
     * recurent path to file
     */
    private String path;

    public TrackedFileData() {

    }

    public Integer getBranchVersion() {
        return branchVersion;
    }

    public void setBranchVersion(Integer branchVersion) {
        this.branchVersion = branchVersion;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getLinkCounter() {
        return linkCounter;
    }

    public void setLinkCounter(Integer linkCounter) {
        this.linkCounter = linkCounter;
    }

    public String getLastModifyUser() {
        return lastModifyUser;
    }

    public void setLastModifyUser(String lastModifyUser) {
        this.lastModifyUser = lastModifyUser;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
