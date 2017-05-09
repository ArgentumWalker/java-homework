package ru.spbau.svidchenko.hw02.vcs;

import ru.spbau.svidchenko.hw02.vcs.exceptions.BadIOException;
import ru.spbau.svidchenko.hw02.vcs.exceptions.VCSException;
import ru.spbau.svidchenko.hw02.vcs.results.LogResult;
import ru.spbau.svidchenko.hw02.vcs.results.StatusResult;
import ru.spbau.svidchenko.hw02.vcs.tasks.*;

import java.io.IOException;

/**
 * Simple one-thread implementation of VCSController interface
 */
public class SimpleVCSController implements VCSController{
    private VCSDataController dataController;

    public SimpleVCSController(VCSDataController dataController) {
        this.dataController = dataController;
    }

    @Override
    public void add(String path) throws VCSException {
        try {
            new AddTask(path, dataController).execute();
        } catch (IOException e) {
            throw new BadIOException(e);
        }
    }

    @Override
    public void remove(String path) throws VCSException {
        try {
            new RemoveTask(path, dataController).execute();
        } catch (IOException e) {
            throw new BadIOException(e);
        }
    }

    @Override
    public LogResult log() throws VCSException {
        try {
            return new LogResult(dataController);
        } catch (IOException e) {
            throw new BadIOException(e);
        }
    }

    @Override
    public StatusResult status() throws VCSException {
        try {
            return new StatusResult(dataController);
        } catch (IOException e) {
            throw new BadIOException(e);
        }
    }

    @Override
    public void branch(Integer task, String targetBranchName) throws VCSException {
        try {
            new BranchTask(task, targetBranchName, dataController).execute();
        } catch (IOException e) {
            throw new BadIOException(e);
        }
    }

    @Override
    public void checkout(Integer reviewID) throws VCSException {
        try {
            new CheckoutTask(reviewID, dataController).execute();
        } catch (IOException e) {
            throw new BadIOException(e);
        }
    }

    @Override
    public void checkout(String branchName) throws VCSException {
        try {
            new CheckoutTask(
                    dataController.getBranchData(
                            dataController.findBranchByName(branchName)
                    ).getLastCommit(), dataController).execute();
        } catch (IOException e) {
            throw new BadIOException(e);
        }
    }

    @Override
    public void commit(String message) throws VCSException {
        try {
            new CommitTask(message, dataController).execute();
        } catch (IOException e) {
            throw new BadIOException(e);
        }
    }

    @Override
    public void merge(String donorBranch, String targetBranch) throws VCSException {
        try {
            Integer donor = dataController.findBranchByName(donorBranch);
            Integer target = dataController.findBranchByName(targetBranch);
            new MergeTask(donor, target, dataController).execute();
        } catch (IOException e) {
            throw new BadIOException(e);
        }
    }

    @Override
    public void reset(String path) throws VCSException {
        try {
            new ResetTask(path, dataController).execute();
        } catch (IOException e) {
            throw new BadIOException(e);
        }
    }

    @Override
    public void clean(String path) throws VCSException {
        try {
            new CleanTask(path, dataController).execute();
        } catch (IOException e) {
            throw new BadIOException(e);
        }
    }
}
