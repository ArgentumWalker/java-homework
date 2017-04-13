package ru.spbau.svidchenko.hw02.vcs.results;

import ru.spbau.svidchenko.hw02.vcs.VCSDataController;
import ru.spbau.svidchenko.hw02.vcs.data.CommitData;
import ru.spbau.svidchenko.hw02.vcs.data.RepositoryInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Result for status task
 */
public class StatusResult {
    private ArrayList<String> trackedAddedFiles = new ArrayList<>();
    private ArrayList<String> trackedRemovedFiles = new ArrayList<>();
    private ArrayList<String> trackedChangedFiles = new ArrayList<>();
    private ArrayList<String> untrackedAddedFiles = new ArrayList<>();
    private ArrayList<String> untrackedRemovedFiles = new ArrayList<>();
    private ArrayList<String> untrackedChangedFiles = new ArrayList<>();

    public StatusResult(VCSDataController dataController) throws IOException {
        RepositoryInfo info = dataController.getRepositoryInfo();
        CommitData currentCommit = dataController.getCommitData(info.getCurrentCommitIndex());
        List<String> trackedFiles = new ArrayList<>();
        for (Integer i : currentCommit.getTrackedFiles()) {
            trackedFiles.add(dataController.getTrackedFileData(i).getPath());
        }

        trackedAddedFiles.addAll(info.getAddedFiles());
        trackedAddedFiles.removeAll(trackedFiles);
        trackedChangedFiles.addAll(info.getAddedFiles());
        trackedChangedFiles.removeAll(trackedAddedFiles);
        trackedRemovedFiles.addAll(info.getRemovedFiles());
        untrackedAddedFiles.addAll(dataController.getAllFiles(""));
        untrackedAddedFiles.removeAll(trackedFiles);
        untrackedAddedFiles.removeAll(trackedAddedFiles);
        untrackedChangedFiles.addAll(dataController.getAllFiles(""));
        untrackedChangedFiles.retainAll(trackedFiles);
        untrackedChangedFiles.removeAll(trackedChangedFiles);
        untrackedRemovedFiles.addAll(trackedFiles);
        untrackedRemovedFiles.removeAll(dataController.getAllFiles(""));
    }

    public ArrayList<String> getUntrackedChangedFiles() {
        return untrackedChangedFiles;
    }

    public ArrayList<String> getUntrackedRemovedFiles() {
        return untrackedRemovedFiles;
    }

    public ArrayList<String> getUntrackedAddedFiles() {
        return untrackedAddedFiles;
    }

    public ArrayList<String> getTrackedChangedFiles() {
        return trackedChangedFiles;
    }

    public ArrayList<String> getTrackedRemovedFiles() {
        return trackedRemovedFiles;
    }

    public ArrayList<String> getTrackedAddedFiles() {
        return trackedAddedFiles;
    }
}
