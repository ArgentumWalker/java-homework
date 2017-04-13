package ru.spbau.svidchenko.hw02.vcs.tasks;

import ru.spbau.svidchenko.hw02.vcs.VCSDataController;
import ru.spbau.svidchenko.hw02.vcs.data.RepositoryInfo;
import ru.spbau.svidchenko.hw02.vcs.exceptions.VCSException;

import java.io.IOException;

/**
 * Remove file/directory from repository and from VCS
 */
public class RemoveTask implements VCSTask {
    private String path;
    private VCSDataController dataController;
    private AddTask addTask;

    public RemoveTask(String path, VCSDataController dataController) {
        this.path = path;
        this.dataController = dataController;
        addTask = new AddTask(path, dataController);
    }

    @Override
    public void execute() throws IOException, VCSException {
        dataController.removeFile(path);
        addTask.execute();
    }
}
