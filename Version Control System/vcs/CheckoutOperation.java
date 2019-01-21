// Craciunoiu Cezar
package vcs;

import java.util.ArrayList;

import static utils.ErrorCodeManager.VCS_STAGED_OP_CODE;
import static utils.ErrorCodeManager.getInstance;
import static utils.ErrorCodeManager.VCS_BAD_CMD_CODE;
import static utils.ErrorCodeManager.VCS_BAD_PATH_CODE;
import utils.ErrorCodeManager;
import utils.OperationType;

public final class CheckoutOperation extends VcsOperation {

    /**
     * Abstract Operation constructor used for the checkout Vcs operation.
     *
     * @param type          operation type
     * @param operationArgs operation arguments
     */
    public CheckoutOperation(OperationType type, ArrayList<String> operationArgs) {
        super(type, operationArgs);
    }

    /**
     * Checks if the command has been given correctly and, depending on the "-c" argument, it
     * either moves the head pointer from one branch to another, or it moves the head pointer to
     * an earlier commit deleting all later commits.
     *
     * @param vcs the vcs
     * @return exit code
     */
    @Override
    public int execute(Vcs vcs) {
        if (!vcs.getBranches().get(vcs.getHead()).getHead().getTrackedOperations().isEmpty()) {
            ErrorCodeManager err = getInstance();
            err.checkExitCode(vcs.getOutputWriter(), VCS_STAGED_OP_CODE);
            return VCS_STAGED_OP_CODE;
        }
        if (!operationArgs.get(0).equals("-c")) {
            int found = -1;
            int it = -1;
            for (VcsBranch branch : vcs.getBranches()) {
                it++;
                if (branch.getBranchName().equals(operationArgs.get(0))) {
                    found = it;
                    break;
                }
            }
            if (found == -1) {
                ErrorCodeManager err = getInstance();
                err.checkExitCode(vcs.getOutputWriter(), VCS_BAD_CMD_CODE);
                return VCS_BAD_CMD_CODE;
            } else {
                for (int i = 0; i < vcs.getBranches().size(); ++i) {
                    if (vcs.getBranches().get(i).getBranchName().equals(operationArgs.get(0))) {
                        vcs.setHead(i);
                        return 0;
                    }
                }
            }
        } else {
            VcsCommit aux = vcs.getBranches().get(vcs.getHead()).getHead();
            boolean found = false;
            while (aux.getParent() != null) {
                aux = aux.getParent();
                if (aux.getId() == Integer.parseInt(operationArgs.get(1))) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                ErrorCodeManager err = getInstance();
                err.checkExitCode(vcs.getOutputWriter(), VCS_BAD_PATH_CODE);
                return VCS_BAD_PATH_CODE;
            }
            vcs.getBranches().get(vcs.getHead()).setHead(aux);
            aux.setChild(null);
            vcs.setActiveSnapshot(aux.getCommitSnapshot());
        }
        return 0;
    }
}
