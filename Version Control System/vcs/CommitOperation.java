// Craciunoiu Cezar
package vcs;

import utils.ErrorCodeManager;
import utils.OperationType;
import java.util.ArrayList;
import static utils.ErrorCodeManager.getInstance;
import static utils.ErrorCodeManager.VCS_BAD_CMD_CODE;

public final class CommitOperation extends VcsOperation {

    /**
     * Commit operation constructor using the AbstractOperation constructor.
     *
     * @param type          type of the operation
     * @param operationArgs operation arguments
     */
    public CommitOperation(OperationType type, ArrayList<String> operationArgs) {
        super(type, operationArgs);
    }

    /**
     * Checks that the staging is not empty then reconstructs the message and creates a new commit.
     *
     * @param vcs the vcs
     * @return execution code
     */
    @Override
    public int execute(Vcs vcs) {
        VcsCommit aux = vcs.getBranches().get(vcs.getHead()).getHead();
        if (aux.getTrackedOperations().isEmpty()) {
            ErrorCodeManager err = getInstance();
            err.checkExitCode(vcs.getOutputWriter(), VCS_BAD_CMD_CODE);
            return VCS_BAD_CMD_CODE;
        }
        String commitMessage = "";
        for (int i = 1; i < operationArgs.size(); ++i) {
            if (i + 1 == operationArgs.size()) {
                commitMessage += operationArgs.get(i);
            } else {
                commitMessage += operationArgs.get(i);
                commitMessage += " ";
            }
        }
        aux.setChild(new VcsCommit(aux, null, aux.getBranch(),
                     vcs.getActiveSnapshot().cloneFileSystem(), commitMessage));
        return 0;
    }
}
