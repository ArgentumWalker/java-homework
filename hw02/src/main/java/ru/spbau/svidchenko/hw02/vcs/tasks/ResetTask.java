package ru.spbau.svidchenko.hw02.vcs.tasks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.spbau.svidchenko.hw02.vcs.VCSDataController;
import ru.spbau.svidchenko.hw02.vcs.data.CommitData;
import ru.spbau.svidchenko.hw02.vcs.data.TrackedFileData;
import ru.spbau.svidchenko.hw02.vcs.exceptions.VCSException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Task for reset file or directory
 */
public class ResetTask implements VCSTask {
    private String path;
    private VCSDataController dataController;
    private static final Logger logger = LogManager.getLogger(ResetTask.class);

    public ResetTask(String path, VCSDataController dataController) {
        this.path = path;
        this.dataController = dataController;
    }

    @Override
    public void execute() throws IOException, VCSException {
        List<String> paths = dataController.getChangedFiles(path);
        CommitData currentCommit = dataController.getCommitData(dataController.getRepositoryInfo().getCurrentCommitIndex());
        List<TrackedFileData> filesToReset = currentCommit.getTrackedFiles().stream()
                .map((Integer i) -> {
                    try {
                        return dataController.getTrackedFileData(i);
                    } catch (Exception e) {
                        logger.error("Can't get tracked file data (id : {})\nStacktrace: {}", i, e.getStackTrace());
                        return null;
                    }
                })
                .filter((tfd) -> tfd.getPath().startsWith(path + dataController.sep()))
                .collect(Collectors.toList());
        for (TrackedFileData data : filesToReset) {
            dataController.clearFile(data.getIndex());
            dataController.restoreFile(data.getIndex());
        }
    }
}
