package ru.spbau.svidchenko.hw02.vcs;

import ru.spbau.svidchenko.hw02.vcs.data.RepositoryInfo;
import ru.spbau.svidchenko.hw02.vcs.exceptions.*;
import ru.spbau.svidchenko.hw02.vcs.results.LogResult;
import ru.spbau.svidchenko.hw02.vcs.results.StatusResult;
import ru.spbau.svidchenko.hw02.vcs.tasks.*;
import org.apache.logging.log4j.*;

import java.io.IOException;

/**
 * Main class of my VCS console interface
 */
public class Main {
    private static final Logger logger = LogManager.getLogger("Main");

    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                System.out.println("What do ya want from me? \n I have nothing!");
                return;
            }
            if (args[0].toLowerCase().equals("help")) {
                System.out.println(
                        "VCS: simple version control system\n" +
                        "List of commands:\n" +
                        "   'create' - create repository from current directory\n" +
                        "       Hint: no files will be added to repo after creation\n" +
                        "   'log' - shows all commits of current branch\n" +
                        "   'branch [action] [name]' - work with branches\n" +
                        "       [action] - is '-c' for create of '-r' for remove\n" +
                        "       [name]   - is name of branch you want to work with\n" +
                        "   'checkout [target type] [target]' - change viewed revision\n" +
                        "       [target type] - is '-c' for commits or '-b' for branches\n" +
                        "       [target] - is branch name or commit index\n" +
                        "       Hint: You can use this command without arguments to get list of exist branches\n" +
                        "   'commit [message]' - commit all changes from repository\n" +
                        "       [message] - any string to identify commit\n" +
                        "   'merge [donor branch]' - merge two branches\n" +
                        "       [donor branch] - changes will be taken from this branch\n" +
                        "       Changes will be stored at current branch\n" +
                        "   'add [path]' - track all changed files in this path\n" +
                        "       [path] - path to any directory or file in repository\n" +
                        "   'remove [path]' - remove tracked files\n" +
                        "       [path] - path to any directory or file in repository\n" +
                        "   'reset [path]' - remove all changes in files\n" +
                        "       [path] - path to any directory or file in repository\n" +
                        "   'clean [path]' - remove all untracked (not commited) files\n" +
                        "       [path] - path to any directory or file in repository\n" +
                        "   'status' - show information about all files in repository\n"
                );
                return;
            }
            if (args[0].toLowerCase().equals("create")) {
                VCSFilesystemDataController.createRepository();
                return;
            }
            if (args[0].toLowerCase().equals("log")) {
                VCSDataController dataController = VCSFilesystemDataController.getInstance();
                LogResult logResult = new SimpleVCSController(dataController).log();
                for (LogResult.LogResultCommit commit : logResult.getAll()) {
                    System.out.println(
                            commit.getCommitID() + ": " + commit.getCommitName() + " in branch " + commit.getBranchName());
                }
                return;
            }
            if (args[0].toLowerCase().equals("status")) {
                VCSDataController dataController = VCSFilesystemDataController.getInstance();
                StatusResult statusResult = new SimpleVCSController(dataController).status();
                System.out.println("Added changes:");
                System.out.println(">New files:");
                for (String filePath : statusResult.getTrackedAddedFiles()) {
                    System.out.println("--->" + filePath);
                }
                System.out.println(">Changed files:");
                for (String filePath : statusResult.getTrackedChangedFiles()) {
                    System.out.println("--->" + filePath);
                }
                System.out.println(">Removed files:");
                for (String filePath : statusResult.getTrackedRemovedFiles()) {
                    System.out.println("--->" + filePath);
                }
                System.out.println("Not added changes:");
                System.out.println(">New files:");
                for (String filePath : statusResult.getUntrackedAddedFiles()) {
                    System.out.println("--->" + filePath);
                }
                System.out.println(">Changed files:");
                for (String filePath : statusResult.getUntrackedChangedFiles()) {
                    System.out.println("--->" + filePath);
                }
                System.out.println(">Removed files:");
                for (String filePath : statusResult.getUntrackedRemovedFiles()) {
                    System.out.println("--->" + filePath);
                }
                return;
            }
            VCSDataController dataController = VCSFilesystemDataController.getInstance();
            VCSController controller = new SimpleVCSController(dataController);
            if (args[0].toLowerCase().equals("branch")) {
                if (args.length == 1) {
                    RepositoryInfo info = dataController.getRepositoryInfo();
                    for (String branchname : info.getBranchIDs().keySet()) {
                        System.out.println(branchname);
                    }
                    System.out.println("Current branch: " +
                            dataController.getBranchData(
                                    dataController.getCommitData(info.getCurrentCommitIndex()).getBranch()).getName()
                    );
                    return;
                }
                if (args.length != 3) {
                    throw new WrongArgumentsException();
                }

                Integer task = -1;
                if (args[1].toLowerCase().equals("-c")) {
                    task = BranchTask.CREATE_TASK;
                }
                if (args[1].toLowerCase().equals("-r")) {
                    task = BranchTask.REMOVE_TASK;
                }
                if (task < 0) {
                    throw new WrongArgumentsException();
                }

                logger.info("^ target branch {}", args[2]);
                String targetBranch = args[2];
                controller.branch(task, targetBranch);
                //task = new BranchTask(taskArgs, dataController);
            }
            if (args[0].toLowerCase().equals("checkout")) {
                if (args.length != 3) {
                    throw new WrongArgumentsException();
                }
                if (args[1].toLowerCase().equals("-b")) {
                    controller.checkout(args[2]);
                    return;
                }
                if (args[1].toLowerCase().equals("-c")) {
                    Integer id = Integer.decode(args[2]);
                    controller.checkout(id);
                    return;
                }
                throw new WrongArgumentsException();
            }
            if (args[0].toLowerCase().equals("commit")) {
                if (args.length != 2) {
                    throw new WrongArgumentsException();
                }
                String message = args[1];
                controller.commit(message);
            }
            if (args[0].toLowerCase().equals("add")) {
                if (args.length != 2) {
                    throw new WrongArgumentsException();
                }
                String path = args[1];
                controller.add(path);
            }
            if (args[0].toLowerCase().equals("remove")) {
                if (args.length != 2) {
                    throw new WrongArgumentsException();
                }
                String path = args[1];
                controller.remove(path);
            }
            if (args[0].toLowerCase().equals("clean")) {
                if (args.length != 2) {
                    throw new WrongArgumentsException();
                }
                String path = args[1];
                controller.clean(path);
            }
            if (args[0].toLowerCase().equals("reset")) {
                if (args.length != 2) {
                    throw new WrongArgumentsException();
                }
                String path = args[1];
                controller.reset(path);
            }
            if (args[0].toLowerCase().equals("merge")) {
                if (args.length != 2) {
                    throw new WrongArgumentsException();
                }
                String donorBranch = args[1];
                String targetBranch = dataController.getBranchData(dataController.getCommitData(
                        dataController.getRepositoryInfo().getCurrentCommitIndex())
                        .getBranch()).getName();
                controller.merge(donorBranch, targetBranch);
            }
        } catch (WrongArgumentsException e) {
            System.out.println("Wrong number of arguments, try 'help' for more information.");
        } catch (IOException e) {
            logger.fatal("IO Exception\nError message:{}\nStackTrace:{}", e.getMessage(), e.getStackTrace());
            System.out.println("FATAL: Repository files are crashed");
        } catch (BadIOException e) {
            logger.fatal("BadIO Exception\nError message:{}\nStackTrace:{}",
                    e.getCause().getMessage(), e.getCause().getStackTrace());
            System.out.println("FATAL: Repository files are crashed");
        } catch (NotRepositoryException e) {
            System.out.println("This directory do not belongs to any repository");
        } catch (BranchNotExistException e) {
            System.out.println("Branch not exist. Try 'branch' command to know branch names");
        } catch (CommitNotExistException e) {
            System.out.println("Commit not exist. Try 'log' command to know branch names");
        } catch (AlreadyARepositoryException e) {
            System.out.println("Directory is already a repository");
        } catch (MergeConflictsException e) {
            System.out.println("There are some merge conflicts: ");
            for (String file : e.gerConflictFiles()) {
                System.out.println(">" + file + " automatically resolved by file from branch " + e.getStandardSolutionBranchName(file));
            }
        } catch (VCSException e) {
            logger.error("Unknown Exception\nError message:{}\nStackTrace:{}", e.getMessage(), e.getStackTrace());
            System.out.println("Something wrong. Try to check typed arguments and try again");
        }
    }
}
