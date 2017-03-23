package ru.spbau.svidchenko.hw02.vcs;

import ru.spbau.svidchenko.hw02.vcs.data.BranchData;
import ru.spbau.svidchenko.hw02.vcs.data.CommitData;
import ru.spbau.svidchenko.hw02.vcs.data.RepositoryInfo;
import ru.spbau.svidchenko.hw02.vcs.exceptions.*;
import ru.spbau.svidchenko.hw02.vcs.tasks.*;

import java.io.IOException;

/**
 * Main class of my VCS console interface
 */
public class Main {

    public void main(String[] args) {
        try {
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
                        "       Hint: You can use this commend without arguments to know exist branches\n" +
                        "   'commit [message]' - commit all changes from repository\n" +
                        "       [message] - any string to identify commit\n" +
                        "   'merge [donor branch]' - merge two branches\n" +
                        "       [donor branch] - changes will be token from this branch\n" +
                        "       Changes will be stored at current branch\n"
                );
                return;
            }
            if (args[0].toLowerCase().equals("create")) {
                VCSFilesystemDataController.createRepository();
                return;
            }
            if (args[0].toLowerCase().equals("log")) {
                VCSDataController dataController = VCSFilesystemDataController.getInstance();
                CommitData currentCommit = dataController.getCommitData(
                        dataController.getRepositoryInfo().getCurrentCommitIndex()
                );
                BranchData currentBranch = dataController.getBranchData(currentCommit.getBranch());
                System.out.println("Branch: " + currentBranch.getName());
                for (Integer i : currentBranch.getCommitIndexes()) {
                    CommitData commit = dataController.getCommitData(i);
                    System.out.println(i + ": " + commit.getMessage() + " "
                            + "(by " + commit.getUser() + " from " + commit.getCommitTime() + ")");
                }
                return;
            }
            VCSTask task = null;
            String[] taskArgs = new String[args.length - 1];
            for (int i = 1; i < args.length; i++) {
                taskArgs[i - 1] = args[i];
            }
            VCSDataController dataController = VCSFilesystemDataController.getInstance();
            if (args[0].toLowerCase().equals("branch")) {
                if (taskArgs.length == 0) {
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
                task = new BranchTask(taskArgs, dataController);
            }
            if (args[0].toLowerCase().equals("checkout")) {
                task = new CheckoutTask(taskArgs, dataController);
            }
            if (args[0].toLowerCase().equals("commit")) {
                task = new CommitTask(taskArgs, dataController);
            }
            if (args[0].toLowerCase().equals("merge")) {
                task = new MergeTask(taskArgs, dataController);
            }
            task.execute();
        } catch (WrongArgumentsException e) {
            System.out.println("Wrong number of arguments, try 'help' for more information.");
        } catch (IOException e) {
            System.out.println("FATAL: Repository files are crashed");
        } catch (NotRepositoryException e) {
            System.out.println("This directory do not belongs to any repository");
        } catch (BranchNotExistException e) {
            System.out.println("Branch not exist. Try 'branch' command to know branch names");
        } catch (CommitNotExistException e) {
            System.out.println("Commit not exist. Try 'log' command to know branch names");
        } catch (VCSException e) {
            System.out.println("Something wrong. Try to check typed arguments and try again");
        }
    }
}
