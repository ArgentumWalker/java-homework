package ru.spbau.svidchenko.hw02.vcs.tasks;

import java.io.IOException;

/**
 * Interface for any tasks for VCS
 */
public interface VCSTask {
    /**
     * Execute a task
     */
    void execute() throws IOException;
}
