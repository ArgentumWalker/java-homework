package ru.spbau.svidchenko.hw02.vcs.tasks;

import ru.spbau.svidchenko.hw02.vcs.VCSDataController;
import ru.spbau.svidchenko.hw02.vcs.data.BranchData;
import ru.spbau.svidchenko.hw02.vcs.data.CommitData;
import ru.spbau.svidchenko.hw02.vcs.data.RepositoryInfo;
import ru.spbau.svidchenko.hw02.vcs.data.TrackedFileData;
import ru.spbau.svidchenko.hw02.vcs.exceptions.WrongArgumentsException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Task for commit changes
 */
public class CommitTask implements VCSTask {
    private String message;
    private String user;
    private Date time;
    private VCSDataController dataController;

    /**
     * Task for committing changes
     * @param args - only one argument represents commit message
     * @param dataController - any implementation of VCSDataController
     */
    public CommitTask(String[] args, VCSDataController dataController) throws WrongArgumentsException {
        this.dataController = dataController;
        if (args.length != 1) {
            throw new WrongArgumentsException();
        }
        message = args[0];
        user = System.getProperty("user.name");
        time = new Date();
    }

    @Override
    public void execute() throws IOException {
        List<String> filesInRep = dataController.getAllFilesInRepository();
        RepositoryInfo info = dataController.getRepositoryInfo();
        CommitData currentCommit = dataController.getCommitData(info.getCurrentCommitIndex());
        BranchData currentBranch = dataController.getBranchData(currentCommit.getBranch());

        ArrayList<Integer> fileIndexes = new ArrayList<>();
        for (String path : filesInRep) {
            TrackedFileData fileData = new TrackedFileData();
            fileData.setIndex(info.getLastFileIndex() + 1);
            info.setLastFileIndex(info.getLastFileIndex() + 1);
            //TODO: fileData.setBranchVersion();
            fileData.setBranchVersion(0);
            fileData.setLastModifyDate(time);
            fileData.setLastModifyUser(user);
            fileData.setLinkCounter(1);
            fileData.setPath(path);
            //TODO: check equality of files and reuse already commited files
            dataController.saveTrackedFileData(fileData,
                    dataController.getFileContent(path));
            fileIndexes.add(fileData.getIndex());
        }

        CommitData commitData = new CommitData();
        commitData.setMessage(message);
        commitData.setUser(user);
        commitData.setCommitTime(time);
        ArrayList<Integer> parents = new ArrayList<>();
        parents.add(info.getCurrentCommitIndex());
        commitData.setParents(parents);
        commitData.setChilds(new ArrayList<>());
        commitData.setTrackedFiles(fileIndexes);
        commitData.setIndex(info.getLastCommitIndex() + 1);
        info.setLastCommitIndex(info.getLastCommitIndex() + 1);
        commitData.setBranch(currentCommit.getBranch());

        currentCommit.getChilds().add(commitData.getIndex());
        info.setCurrentCommitIndex(commitData.getIndex());
        currentBranch.getCommitIndexes().add(currentCommit.getIndex());
        currentBranch.setLastCommit(commitData.getIndex());

        dataController.saveRepositoryInfo(info);
        dataController.saveCommitData(commitData);
        dataController.saveCommitData(currentCommit);
        dataController.saveBranchData(currentBranch);
    }
}
