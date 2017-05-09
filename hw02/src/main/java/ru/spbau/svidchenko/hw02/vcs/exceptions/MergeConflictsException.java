package ru.spbau.svidchenko.hw02.vcs.exceptions;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Exception represents that there are some conflicts when merging branches
 */
public class MergeConflictsException extends VCSException {
    private ArrayList<String> conflictFiles = new ArrayList<>();
    private HashMap<String, String> conflictFileNameToBranchWithStandardSolution = new HashMap<>();

    public void addConflictFile(String fileName, String branchSolutionName) {
        conflictFiles.add(fileName);
        conflictFileNameToBranchWithStandardSolution.put(fileName, branchSolutionName);
    }

    public String getStandardSolutionBranchName(String filePath) {
        return conflictFileNameToBranchWithStandardSolution.get(filePath);
    }

    public ArrayList<String> gerConflictFiles() {
        return conflictFiles;
    }
}
