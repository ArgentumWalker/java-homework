package ru.spbau.svidchenko.hw02.vcs.tasks;

import ru.spbau.svidchenko.hw02.vcs.VCSDataController;
import ru.spbau.svidchenko.hw02.vcs.data.RepositoryInfo;
import ru.spbau.svidchenko.hw02.vcs.exceptions.VCSException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Task for adding new files to repository
 */
public class AddTask implements VCSTask{
    private String path;
    private VCSDataController dataController;

    public AddTask(String path, VCSDataController dataController) {
        this.path = path;
        this.dataController = dataController;
    }

    @Override
    public void execute() throws IOException, VCSException {
        RepositoryInfo info = dataController.getRepositoryInfo();
        List<String> filePaths = dataController.getChangedFiles(path);
        ArrayList<String> trackedFiles = new ArrayList<>();
        for (Integer i : dataController.getCommitData(info.getCurrentCommitIndex()).getTrackedFiles()) {
            trackedFiles.add(dataController.getTrackedFileData(i).getPath());
        }
        List<String> allFiles = dataController.getAllFiles(path);
        List<String> newFiles = new ArrayList<>();
        newFiles.addAll(allFiles);
        newFiles.removeAll(trackedFiles);
        filePaths.addAll(newFiles);
        List<String> removedFiles = trackedFiles.stream()
                .filter((s) -> s.startsWith(path))
                .collect(Collectors.toList());
        removedFiles.removeAll(allFiles);
        filePaths.addAll(removedFiles);
        filePaths.removeAll(info.getAddedFiles());
        info.getAddedFiles().addAll(filePaths);

        /*info.getAddedFiles().addAll(filePaths);
        info.getRemovedFiles().addAll(removedFiles);
        info.getAddedFiles().removeAll(removedFiles);
        info.getRemovedFiles().removeAll(filePaths);*/
        dataController.saveRepositoryInfo(info);
    }
}
