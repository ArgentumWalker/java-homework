package ru.spbau.svidchenko.hw02.vcs.tasks;

import ru.spbau.svidchenko.hw02.vcs.exceptions.CommitNotExistException;
import ru.spbau.svidchenko.hw02.vcs.exceptions.VCSException;

import java.io.IOException;

/**
 * Interface for any tasks for VCS
 */
public interface VCSTask {
    /**
     * Execute a task
     */
    void execute() throws IOException, VCSException;
}
