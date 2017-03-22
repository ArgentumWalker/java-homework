package ru.spbau.svidchenko.hw02.vcs;

import ru.spbau.svidchenko.hw02.vcs.data.BranchData;
import ru.spbau.svidchenko.hw02.vcs.data.CommitData;
import ru.spbau.svidchenko.hw02.vcs.data.RepositoryInfo;
import ru.spbau.svidchenko.hw02.vcs.data.TrackedFileData;
import ru.spbau.svidchenko.hw02.vcs.exceptions.NotRepositoryException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of VCSDataController
 * Works with standard filesystem
 */
public class VCSFilesystemDataController implements VCSDataController {
    private static final String VCS_DATA_DIRECTORY = ".VCS";
    private static final String VCS_COMMIT_DATA_DIRECTORY  = VCS_DATA_DIRECTORY + "\\" + "COMMITS";
    private static final String VCS_BRANCH_DATA_DIRECTORY  = VCS_DATA_DIRECTORY + "\\" + "BRANCHES";
    private static final String VCS_FILE_DATA_DIRECTORY    = VCS_DATA_DIRECTORY + "\\" + "FILE_DATAS";
    private static final String VCS_FILE_CONTENT_DIRECTORY = VCS_DATA_DIRECTORY + "\\" + "FILE_CONTENTS";
    private static final String VCS_REPOSITORY_INFO        = VCS_DATA_DIRECTORY + "\\" + "INFO";

    private String repositoryPath;

    private VCSFilesystemDataController() throws NotRepositoryException {
        this(System.getProperty("user.dir"));
    }

    private VCSFilesystemDataController(String directory) throws NotRepositoryException {
        Path path = Paths.get(directory).toAbsolutePath();
        Path tmp;
        while (!path.equals(path.getRoot())) {
            tmp = Paths.get(path.toString() + "\\" + VCS_DATA_DIRECTORY);
            if (tmp.toFile().exists()) {
                break;
            }
            path = path.getParent();
        }
        repositoryPath = path.toString();
        tmp = Paths.get(path.toString() + "\\" + VCS_DATA_DIRECTORY);
        if (tmp.toFile().exists()) {
            repositoryPath = path.toString();
        } else {
            repositoryPath = null;
            throw new NotRepositoryException();
        }
    }

    static public void createRepository() throws IOException {
        createRepository(System.getProperty("user.dir"));
    }

    static public void createRepository(String directory) throws IOException {
        Files.createDirectories(Paths.get(directory + "\\" + VCS_BRANCH_DATA_DIRECTORY));
        Files.createDirectories(Paths.get(directory + "\\" + VCS_COMMIT_DATA_DIRECTORY));
        Files.createDirectories(Paths.get(directory + "\\" + VCS_FILE_DATA_DIRECTORY));
        Files.createDirectories(Paths.get(directory + "\\" + VCS_FILE_CONTENT_DIRECTORY));

        BranchData masterBranch = new BranchData();
        CommitData initialCommit = new CommitData();
        RepositoryInfo info = new RepositoryInfo();

        masterBranch.setName("master");
        masterBranch.setIndex(0);
        masterBranch.setCommitIndexes(new ArrayList<>());
        masterBranch.getCommitIndexes().add(0);
        masterBranch.setLastCommit(0);

        initialCommit.setBranch(0);
        initialCommit.setUser(System.getProperty("user.name"));
        initialCommit.setIndex(0);
        initialCommit.setTrackedFiles(new ArrayList<>());
        initialCommit.setChilds(new ArrayList<>());
        initialCommit.setParents(new ArrayList<>());
        initialCommit.setMessage("Empty initial commit");
        initialCommit.setCommitTime(new Date());

        info.setCurrentCommitIndex(0);
        info.setLastFileIndex(-1);
        info.setLastBranchIndex(0);
        info.setLastCommitIndex(0);
        info.setBranchIDs(new HashMap<>());
        info.getBranchIDs().put(masterBranch.getName(), masterBranch.getIndex());

        saveSomething(initialCommit,
                directory + "\\" + VCS_COMMIT_DATA_DIRECTORY + "\\0");
        saveSomething(masterBranch,
                directory + "\\" + VCS_BRANCH_DATA_DIRECTORY + "\\0");
        saveSomething(info,
                directory + "\\" + VCS_REPOSITORY_INFO);
    }

    static public VCSFilesystemDataController getInstance() throws NotRepositoryException {
        return new VCSFilesystemDataController();
    }

    static public VCSFilesystemDataController getInstance(String directory) throws NotRepositoryException {
        return new VCSFilesystemDataController(directory);
    }

    @Override
    public Integer findBranchByName(String branchName) throws IOException {
        return getRepositoryInfo().getBranchIDs().get(branchName);
    }

    @Override
    public void saveCommitData(CommitData commit) throws IOException {
        saveSomething(commit,
                repositoryPath + "\\" + VCS_COMMIT_DATA_DIRECTORY + "\\" + commit.getIndex());
    }

    @Override
    public void saveBranchData(BranchData branch) throws IOException {
        saveSomething(branch,
                repositoryPath + "\\" + VCS_BRANCH_DATA_DIRECTORY + "\\" + branch.getIndex());
        RepositoryInfo info = getRepositoryInfo();
        info.getBranchIDs().put(branch.getName(), branch.getIndex());
        saveRepositoryInfo(info);
    }

    @Override
    public void saveRepositoryInfo(RepositoryInfo info) throws IOException {
        saveSomething(info,
                repositoryPath + "\\" + VCS_REPOSITORY_INFO);
    }

    @Override
    public void saveTrackedFileData(TrackedFileData data) throws IOException {
        saveSomething(data,
                repositoryPath + "\\" + VCS_FILE_DATA_DIRECTORY + "\\" + data.getIndex());
    }

    @Override
    public void saveTrackedFileData(TrackedFileData data, String fileContent) throws IOException {
        saveSomething(data,
                repositoryPath + "\\" + VCS_FILE_DATA_DIRECTORY + "\\" + data.getIndex());
        saveSomething(fileContent,
                repositoryPath + "\\" + VCS_FILE_CONTENT_DIRECTORY + "\\" + data.getIndex());
    }

    @Override
    public RepositoryInfo getRepositoryInfo() throws IOException {
        return (RepositoryInfo)loadSomething("\\" + VCS_REPOSITORY_INFO);
    }

    @Override
    public CommitData getCommitData(Integer index) throws IOException {
        return (CommitData) loadSomething("\\" + VCS_COMMIT_DATA_DIRECTORY + "\\" + index);
    }

    @Override
    public BranchData getBranchData(Integer index) throws IOException {
        return (BranchData) loadSomething("\\" + VCS_BRANCH_DATA_DIRECTORY + "\\" + index);
    }

    @Override
    public TrackedFileData getTrackedFileData(Integer index) throws IOException {
        return (TrackedFileData) loadSomething("\\" + VCS_FILE_DATA_DIRECTORY + "\\" + index);
    }

    @Override
    public String getTrackedFileContent(Integer index) throws IOException {
        return (String) loadSomething("\\" + VCS_FILE_CONTENT_DIRECTORY + "\\" + index);
    }

    @Override
    public String getFileContent(String path) throws IOException {
        File file = Paths.get(repositoryPath + "\\" + path).toFile();
        if (file.exists()) {
            FileInputStream fileIn = new FileInputStream(file);
            Reader r = new InputStreamReader(fileIn);
            StringBuilder builder = new StringBuilder();
            int c = 0;
            while ((c = r.read()) != -1) {
                builder.append((char) c);
            }
            fileIn.close();
            return builder.toString();
        }
        return null;
    }

    @Override
    public List<String> getAllFilesInRepository() throws IOException {
        return Files.walk(Paths.get(repositoryPath))
                .filter(Files::isRegularFile)
                .filter((p) -> !p.startsWith(repositoryPath + "\\" + VCS_DATA_DIRECTORY))
                .map((p) -> p.toString().substring(repositoryPath.length() + 1))
                .collect(Collectors.toList());
    }

    @Override
    public void removeCommitData(Integer index) throws IOException {
        removeSomething("\\" + VCS_COMMIT_DATA_DIRECTORY + "\\" + index);
    }

    @Override
    public void removeBranchData(Integer index) throws IOException {
        BranchData branch = getBranchData(index);
        removeSomething("\\" + VCS_BRANCH_DATA_DIRECTORY + "\\" + index);
        RepositoryInfo info = getRepositoryInfo();
        info.getBranchIDs().remove(branch.getName());
        saveRepositoryInfo(info);
    }

    @Override
    public void removeTrackedFileData(Integer index) throws IOException {
        removeSomething("\\" + VCS_FILE_DATA_DIRECTORY + "\\" + index);
        removeSomething("\\" + VCS_FILE_CONTENT_DIRECTORY + "\\" + index);
    }

    @Override
    public void clearFile(Integer index) throws IOException {
        TrackedFileData fileData = getTrackedFileData(index);
        removeSomething(fileData.getPath());
    }

    @Override
    public void restoreFile(Integer index) throws IOException {
        TrackedFileData fileData = getTrackedFileData(index);
        File file = Paths.get(repositoryPath + "\\" + fileData.getPath()).toFile();
        if (!file.getParentFile().exists()) {
            Files.createDirectories(Paths.get(file.getParentFile().getAbsolutePath()));
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fileOut = new FileOutputStream(file);
        fileOut.write(getTrackedFileContent(index).getBytes());
        fileOut.close();
    }


    private void removeSomething(String where) throws IOException {
        File file = Paths.get(repositoryPath + "\\"+ where)
                .toFile();
        if (file.exists()) {
            Files.delete(Paths.get(file.getAbsolutePath()));
        }
    }

    static private void saveSomething(Object o, String where) throws IOException {
        File file = Paths.get(where)
                .toFile();
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fileOut = new FileOutputStream(file);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(o);
        out.close();
        fileOut.close();
    }

    private Object loadSomething(String path) throws IOException {
        File file = Paths.get(repositoryPath + path)
                .toFile();
        FileInputStream fileIn = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        Object result = null;
        try {
            result = in.readObject();
        }
        catch (ClassNotFoundException e) {
            ////Just return null
        }
        in.close();
        fileIn.close();
        return result;
    }
}
