// Craciunoiu Cezar
package vcs;

import utils.OperationType;

import java.util.ArrayList;

public final class LogOperation extends VcsOperation {

    /**
     * Log operation constructor using the AbstractOperation constructor.
     *
     * @param type          type of the operation
     * @param operationArgs operation arguments
     */
    public LogOperation(OperationType type, ArrayList<String> operationArgs) {
        super(type, operationArgs);
    }

    /**
     * Passes through all the commits on a branch and prints their id and message.
     *
     * @param vcs the vcs
     * @return execution code
     */
    @Override
    public int execute(Vcs vcs) {
        VcsCommit commit = vcs.getBranches().get(vcs.getHead()).getInitialCommit();
        while (commit.getChild() != null) {
            vcs.getOutputWriter().write("Commit id: " + commit.getId() + '\n');
            vcs.getOutputWriter().write("Message: " + commit.getCommitMessage() + '\n' + '\n');
            commit = commit.getChild();
        }
        vcs.getOutputWriter().write("Commit id: " + commit.getId() + '\n');
        vcs.getOutputWriter().write("Message: " + commit.getCommitMessage() + '\n');
        return 0;
    }
}
