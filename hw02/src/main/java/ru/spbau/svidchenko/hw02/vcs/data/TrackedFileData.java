package ru.spbau.svidchenko.hw02.vcs.data;

import java.io.Serializable;
import java.util.Date;
import java.util.Scanner;

/**
 * Serialized file data
 */
public class TrackedFileData implements Serializable {

    /**
     * index of file data bundle
     */
    private Integer index = 0;

    /**
     * how much commits links to that bundle
     */
    private Integer linkCounter = 0;

    /**
     * person who committed this file last
     */
    private String lastModifyUser = "";

    /**
     * bundle creation date
     */
    private Date lastModifyDate = new Date();

    /**
     * recurent path to file
     */
    private String path = "";

    /**
     * HashCode of file content
     */
    private byte[] hash = new byte[0];

    public TrackedFileData() {

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

    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }
}
