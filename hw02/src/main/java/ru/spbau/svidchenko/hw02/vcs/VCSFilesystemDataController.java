package ru.spbau.svidchenko.hw02.vcs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.spbau.svidchenko.hw02.vcs.data.BranchData;
import ru.spbau.svidchenko.hw02.vcs.data.CommitData;
import ru.spbau.svidchenko.hw02.vcs.data.RepositoryInfo;
import ru.spbau.svidchenko.hw02.vcs.data.TrackedFileData;
import ru.spbau.svidchenko.hw02.vcs.exceptions.AlreadyARepositoryException;
import ru.spbau.svidchenko.hw02.vcs.exceptions.NotRepositoryException;

import java.io.*;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of VCSDataController
 * Works with standard filesystem
 */
public class VCSFilesystemDataController implements VCSDataController {
    private static final String SEP = File.separator;
    private static final String VCS_DATA_DIRECTORY = ".VCS";
    private static final String VCS_COMMIT_DATA_DIRECTORY  = VCS_DATA_DIRECTORY + SEP + "COMMITS";
    private static final String VCS_BRANCH_DATA_DIRECTORY  = VCS_DATA_DIRECTORY + SEP + "BRANCHES";
    private static final String VCS_FILE_DATA_DIRECTORY    = VCS_DATA_DIRECTORY + SEP + "FILE_DATAS";
    private static final String VCS_FILE_CONTENT_DIRECTORY = VCS_DATA_DIRECTORY + SEP + "FILE_CONTENTS";
    private static final String VCS_REPOSITORY_INFO        = VCS_DATA_DIRECTORY + SEP + "INFO";
    private static final int BUFFER_SIZE = 2048;
    private static final Logger logger = LogManager.getLogger(VCSFilesystemDataController.class);


    private String repositoryPath;

    private VCSFilesystemDataController() throws NotRepositoryException {
        this(System.getProperty("user.dir"));
    }

    private VCSFilesystemDataController(String directory) throws NotRepositoryException {
        Path path = Paths.get(directory).toAbsolutePath();
        Path tmp;
        while (!path.equals(path.getRoot())) {
            tmp = Paths.get(path.toString() + SEP + VCS_DATA_DIRECTORY);
            if (tmp.toFile().exists()) {
                break;
            }
            path = path.getParent();
        }
        tmp = Paths.get(path.toString() + SEP + VCS_DATA_DIRECTORY);
        if (tmp.toFile().exists()) {
            repositoryPath = path.toString();
        } else {
            repositoryPath = null;
            throw new NotRepositoryException();
        }
    }

    static public void createRepository() throws IOException, AlreadyARepositoryException {
        createRepository(System.getProperty("user.dir"));
    }

    static public void createRepository(String directory) throws IOException, AlreadyARepositoryException {
        try {
            getInstance();
            throw new AlreadyARepositoryException();
        } catch (NotRepositoryException e) {
            //Thats what I expected
        }
        Files.createDirectories(Paths.get(directory + SEP + VCS_BRANCH_DATA_DIRECTORY));
        Files.createDirectories(Paths.get(directory + SEP + VCS_COMMIT_DATA_DIRECTORY));
        Files.createDirectories(Paths.get(directory + SEP + VCS_FILE_DATA_DIRECTORY));
        Files.createDirectories(Paths.get(directory + SEP + VCS_FILE_CONTENT_DIRECTORY));

        BranchData masterBranch = new BranchData();
        CommitData initialCommit = new CommitData();
        RepositoryInfo info = new RepositoryInfo();

        masterBranch.setName("master");
        masterBranch.getCommitIndexes().add(0);

        initialCommit.setUser(System.getProperty("user.name"));
        initialCommit.setTrackedFiles(new ArrayList<>());
        initialCommit.setMessage("Empty initial commit");

        info.getBranchIDs().put(masterBranch.getName(), masterBranch.getIndex());

        saveSomething(initialCommit,
                directory + SEP + VCS_COMMIT_DATA_DIRECTORY + SEP + "0");
        saveSomething(masterBranch,
                directory + SEP + VCS_BRANCH_DATA_DIRECTORY + SEP + "0");
        saveSomething(info,
                directory + SEP + VCS_REPOSITORY_INFO);
    }

    static public VCSFilesystemDataController getInstance() throws NotRepositoryException {
        return new VCSFilesystemDataController();
    }

    static public VCSFilesystemDataController getInstance(String directory) throws NotRepositoryException {
        return new VCSFilesystemDataController(directory);
    }

    @Override
    public String sep() {
        return SEP;
    }

    @Override
    public Integer findBranchByName(String branchName) throws IOException {
        return getRepositoryInfo().getBranchIDs().get(branchName);
    }

    @Override
    public byte[] calculateHash(String path) {
        return calculateHash(Paths.get(repositoryPath + SEP + path).toFile());
    }

    @Override
    public void saveCommitData(CommitData commit) throws IOException {
        saveSomething(commit,
                repositoryPath + SEP + VCS_COMMIT_DATA_DIRECTORY + SEP + commit.getIndex());
    }

    @Override
    public void saveBranchData(BranchData branch) throws IOException {
        saveSomething(branch,
                repositoryPath + SEP + VCS_BRANCH_DATA_DIRECTORY + SEP + branch.getIndex());
        RepositoryInfo info = getRepositoryInfo();
        info.getBranchIDs().put(branch.getName(), branch.getIndex());
        saveRepositoryInfo(info);
    }

    @Override
    public void saveRepositoryInfo(RepositoryInfo info) throws IOException {
        saveSomething(info,
                repositoryPath + SEP + VCS_REPOSITORY_INFO);
    }

    @Override
    public void saveTrackedFileData(TrackedFileData data) throws IOException {
        saveSomething(data,
                repositoryPath + SEP + VCS_FILE_DATA_DIRECTORY + SEP + data.getIndex());
    }

    @Override
    public void saveTrackedFileData(TrackedFileData data, String fileContent) throws IOException {
        saveSomething(data,
                repositoryPath + SEP + VCS_FILE_DATA_DIRECTORY + SEP + data.getIndex());
        saveSomething(fileContent,
                repositoryPath + SEP + VCS_FILE_CONTENT_DIRECTORY + SEP + data.getIndex());
    }

    @Override
    public RepositoryInfo getRepositoryInfo() throws IOException {
        return (RepositoryInfo)loadSomething(SEP + VCS_REPOSITORY_INFO);
    }

    @Override
    public CommitData getCommitData(Integer index) throws IOException {
        return (CommitData) loadSomething(SEP + VCS_COMMIT_DATA_DIRECTORY + SEP + index);
    }

    @Override
    public BranchData getBranchData(Integer index) throws IOException {
        return (BranchData) loadSomething(SEP + VCS_BRANCH_DATA_DIRECTORY + SEP + index);
    }

    @Override
    public TrackedFileData getTrackedFileData(Integer index) throws IOException {
        return (TrackedFileData) loadSomething(SEP + VCS_FILE_DATA_DIRECTORY + SEP + index);
    }

    @Override
    public String getTrackedFileContent(Integer index) throws IOException {
        return (String) loadSomething(SEP + VCS_FILE_CONTENT_DIRECTORY + SEP + index);
    }

    @Override
    public String getFileContent(String path) throws IOException {
        File file = Paths.get(repositoryPath + SEP + path).toFile();
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
    public List<String> getAllFiles(String path) throws IOException {
        String dirPath;
        if (path.equals("")) {
            dirPath = repositoryPath;
        } else {
            dirPath = repositoryPath + SEP + path;
        }
        if (Files.exists(Paths.get(dirPath))) {
            return Files.walk(Paths.get(dirPath))
                    .filter(Files::isRegularFile)
                    .filter((p) -> !p.startsWith(repositoryPath + SEP + VCS_DATA_DIRECTORY))
                    .map((p) -> p.toString().substring(repositoryPath.length() + 1))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<String> getChangedFiles(String path) throws IOException {
        HashMap<String, TrackedFileData> pathToFileData = new HashMap<>();
        getCommitData(getRepositoryInfo().getCurrentCommitIndex()).getTrackedFiles().stream()
                .map((Integer i) -> {
                        try {
                            return getTrackedFileData(i);
                        } catch (Exception e) {
                            logger.error("Something wrong when getting changed files from {}\nStackTrace: {}\n",
                                    path, e.getStackTrace());
                            return null;
                        }
                    })
                .filter((d) -> d.getPath().startsWith(path))
                .forEach((TrackedFileData d) -> pathToFileData.put(d.getPath(), d));
        String dirPath = repositoryPath;
        if (!path.equals("")) {
            dirPath = repositoryPath + SEP + path;
        }
        if (Files.exists(Paths.get(dirPath))) {
            return Files.walk(Paths.get(repositoryPath))
                    .filter(Files::isRegularFile)
                    .filter((p) -> !p.startsWith(repositoryPath + SEP + VCS_DATA_DIRECTORY))
                    .filter((p) -> pathToFileData.containsKey(p.toString().substring(repositoryPath.length() + 1)))
                    .filter((p) -> !Arrays.equals(calculateHash(p.toFile()),
                            pathToFileData.get(p.toString().substring(repositoryPath.length() + 1)).getHash()))
                    .map((p) -> p.toString().substring(repositoryPath.length() + 1))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public void removeCommitData(Integer index) throws IOException {
        removeSomething(SEP + VCS_COMMIT_DATA_DIRECTORY + SEP + index);
    }

    @Override
    public void removeBranchData(Integer index) throws IOException {
        BranchData branch = getBranchData(index);
        removeSomething(SEP + VCS_BRANCH_DATA_DIRECTORY + SEP + index);
        RepositoryInfo info = getRepositoryInfo();
        info.getBranchIDs().remove(branch.getName());
        saveRepositoryInfo(info);
    }

    @Override
    public void removeTrackedFileData(Integer index) throws IOException {
        removeSomething(SEP + VCS_FILE_DATA_DIRECTORY + SEP + index);
        removeSomething(SEP + VCS_FILE_CONTENT_DIRECTORY + SEP + index);
    }

    @Override
    public void removeFile(String path) throws IOException {
        removeSomething(SEP + path);
    }

    @Override
    public void clearFile(Integer index) throws IOException {
        TrackedFileData fileData = getTrackedFileData(index);
        removeSomething(fileData.getPath());
    }

    @Override
    public void restoreFile(Integer index) throws IOException {
        TrackedFileData fileData = getTrackedFileData(index);
        File file = Paths.get(repositoryPath + SEP + fileData.getPath()).toFile();
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
        Path path = Paths.get(repositoryPath + SEP+ where);
        if (Files.isDirectory(path)) {
            List<Path> filesList = Files.list(path).collect(Collectors.toList());
            while (filesList.size() > 0) {
                filesList = filesList.stream().peek((p) -> {
                            try {
                                Files.deleteIfExists(p);
                            }
                            catch (IOException e) {
                                //Do nothing
                            }
                        })
                        .filter((p) -> Files.exists(p))
                        .collect(Collectors.toList());
            }
        }
        Files.deleteIfExists(path);
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
            logger.fatal("if this happen, you are on highway to hell\nPath: \nStacktrace: {}", path, e.getStackTrace());
            ////Just return null
        }
        in.close();
        fileIn.close();
        return result;
    }

    private byte[] calculateHash(File file) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            FileInputStream fileIn = new FileInputStream(file);
            DigestInputStream inp = new DigestInputStream(fileIn, digest);
            byte[] buffer = new byte[BUFFER_SIZE];
            while (inp.read(buffer) != -1) {

            }
            inp.close();
            fileIn.close();
            return digest.digest();
        }
        catch (Exception e) {
            logger.error("calculateHash error. We can't calculate it all, right?\nStacktrace: {}",
                    (Object[])e.getStackTrace());
            return null;
        }
    }
}
