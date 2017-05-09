package ru.spbau.svidchenko.hw02.vcs;

import ru.spbau.svidchenko.hw02.vcs.exceptions.VCSException;
import ru.spbau.svidchenko.hw02.vcs.results.LogResult;
import ru.spbau.svidchenko.hw02.vcs.results.StatusResult;

/**
 * Controller for all supported tasks and actions in VCS
 */
public interface VCSController {

    /**
     * Add file/directory to repository
     */
    void add(String path) throws VCSException;

    /**
     * Remove file/directory to repository
     */
    void remove(String path) throws VCSException;

    /**
     * Returns LogResult with indexes and names of commits of current branch
     */
    LogResult log() throws VCSException;

    /**
     * Returns StatusResult with information about files in repository
     */
    StatusResult status() throws VCSException;

    /**
     * Work with branches
     * @param task is BranchTask.CONSTRUCTOR_TASK or BranchTask.REMOVE_TASK
     * @param targetBranchName is name of target branch
     */
    void branch(Integer task, String targetBranchName) throws VCSException;

    /**
     * Checkout to revision
     */
    void checkout(Integer reviewID) throws VCSException;

    /**
     * Checkout to branch head
     */
    void checkout(String branchName) throws VCSException;

    /**
     * Commit changes to repository with message
     */
    void commit(String message) throws VCSException;

    /**
     * Merge donorBranch to targetBranch
     */
    void merge(String donorBranch, String targetBranch) throws VCSException;

    /**
     * Reset file to current revision version
     */
    void reset(String path) throws VCSException;

    /**
     * Remove all not tracked files
     */
    void clean(String path) throws VCSException;
}
