package ru.spbau.svidchenko.hw02.vcs;

import ru.spbau.svidchenko.hw02.vcs.data.BranchData;
import ru.spbau.svidchenko.hw02.vcs.data.CommitData;
import ru.spbau.svidchenko.hw02.vcs.data.RepositoryInfo;
import ru.spbau.svidchenko.hw02.vcs.data.TrackedFileData;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Interface for Data Controller for resolving tasks.
 * There are two rules about paths:
 * 1) Empty path - all files in repository
 * 2) If there are any files with prefix path + sep(), then path is directory which contains all files with such prefix in their path
 */
public interface VCSDataController {

    /**
     * Returns path separator
     */
    String sep();

    /**
     * Return branch id by it's name
     */
    Integer findBranchByName(String branchName) throws IOException;

    /**
     * Calculate hash of file by path in repository
     */
    byte[] calculateHash(String path);

    /**
     * Store commit data
     * It differentiate commits by index
     */
    void saveCommitData(CommitData commit) throws IOException;

    /**
     * Store branch data
     * It differentiate branches by index
     */
    void saveBranchData(BranchData branch) throws IOException;


    /**
     * Store repository information
     */
    void saveRepositoryInfo(RepositoryInfo info) throws IOException;

    /**
     * Store file data
     * It differentiate files by index
     * Data will be stored with old file content
     */
    void saveTrackedFileData(TrackedFileData data) throws IOException;

    /**
     * Store file data and file content
     * It differentiate files by index
     */
    void saveTrackedFileData(TrackedFileData data, String fileContent) throws IOException;

    /**
     * Restore repository information
     */
    RepositoryInfo getRepositoryInfo() throws IOException;

    /**
     * Restore commit data
     */
    CommitData getCommitData(Integer index) throws IOException;

    /**
     * Restore branch data
     */
    BranchData getBranchData(Integer index) throws IOException;

    /**
     * Restore tracked file data
     */
    TrackedFileData getTrackedFileData(Integer index) throws IOException;

    /**
     * Restore tracked file data
     */
    String getTrackedFileContent(Integer index) throws IOException;

    /**
     * Get actual file content
     */
    String getFileContent(String path) throws IOException;

    /**
     * Get list of all files in repository
     * List contains correct paths to files in repository
     */
    List<String> getAllFiles(String path) throws IOException;

    /**
     * Get list of all files with changes in repository subdirectory
     * List contains correct paths to files in repository
     */
    List<String> getChangedFiles(String path) throws IOException;

    /**
     * Remove commit data from repository
     */
    void removeCommitData(Integer index) throws IOException;

    /**
     * Remove branch data from repository
     */
    void removeBranchData(Integer index) throws IOException;

    /**
     * Remove file data from repository
     */
    void removeTrackedFileData(Integer index) throws IOException;

    /**
     * Remove file from repository
     */
    void removeFile(String path) throws IOException;

    /**
     * Remove actual file with path from TrackedFileData from repo
     * @param index is index of TrackedFileData
     */
    void clearFile(Integer index) throws IOException;

    /**
     * Restores file content
     * @param index is index of TrackedFileData
     */
    void restoreFile(Integer index) throws IOException;

}
