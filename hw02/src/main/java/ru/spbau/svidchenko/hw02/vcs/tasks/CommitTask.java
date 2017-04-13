package ru.spbau.svidchenko.hw02.vcs.tasks;

import ru.spbau.svidchenko.hw02.vcs.VCSDataController;
import ru.spbau.svidchenko.hw02.vcs.data.BranchData;
import ru.spbau.svidchenko.hw02.vcs.data.CommitData;
import ru.spbau.svidchenko.hw02.vcs.data.RepositoryInfo;
import ru.spbau.svidchenko.hw02.vcs.data.TrackedFileData;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Task for commit changes
 */
public class CommitTask implements VCSTask {
    private String message;
    private String user;
    private Date time;
    private VCSDataController dataController;

    /**
     * Simple constructor
     */
    public CommitTask(String message, VCSDataController dataController) {
        this.dataController = dataController;
        this.message = message;
        user = System.getProperty("user.name");
        time = new Date();
    }

    @Override
    public void execute() throws IOException {
        HashMap<String, Integer> fileIdByFilePath = new HashMap<>();
        RepositoryInfo info = dataController.getRepositoryInfo();
        CommitData currentCommit = dataController.getCommitData(info.getCurrentCommitIndex());
        BranchData currentBranch = dataController.getBranchData(currentCommit.getBranch());
        ArrayList<Integer> fileIndexes = currentCommit.getTrackedFiles();
        ArrayList<String> pathes = new ArrayList<>();

        for (Integer id : fileIndexes) {
            fileIdByFilePath.put(dataController.getTrackedFileData(id).getPath(), id);
            pathes.add(dataController.getTrackedFileData(id).getPath());
        }

        pathes.removeAll(info.getRemovedFiles());

        for (String path : info.getAddedFiles()) {
            byte[] hash = dataController.calculateHash(path);
            if (fileIdByFilePath.containsKey(path) &&
                    dataController.getTrackedFileData(fileIdByFilePath.get(path)).getHash().equals(hash)) {
                continue;
            }
            TrackedFileData fileData = new TrackedFileData();
            fileData.setIndex(info.getLastFileIndex() + 1);
            info.setLastFileIndex(info.getLastFileIndex() + 1);
            //TODO: fileData.setBranchVersion();
            fileData.setLastModifyDate(time);
            fileData.setLastModifyUser(user);
            fileData.setLinkCounter(1);
            fileData.setPath(path);
            fileData.setHash(hash);
            dataController.saveTrackedFileData(fileData,
                    dataController.getFileContent(path));
            if (!fileIdByFilePath.containsKey(path)) {
                pathes.add(path);
            }
            fileIdByFilePath.put(path, fileData.getIndex());
        }

        fileIndexes.clear();
        fileIndexes.addAll(pathes.stream().map((p) -> fileIdByFilePath.get(p)).collect(Collectors.toList()));

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
        info.getAddedFiles().clear();
        info.getRemovedFiles().clear();

        dataController.saveRepositoryInfo(info);
        dataController.saveCommitData(commitData);
        dataController.saveCommitData(currentCommit);
        dataController.saveBranchData(currentBranch);
    }
}
