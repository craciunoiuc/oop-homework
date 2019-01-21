// Craciunoiu Cezar
package vcs;

import filesystem.FileSystemSnapshot;
import utils.ErrorCodeManager;
import utils.OperationType;
import java.util.ArrayList;
import static utils.ErrorCodeManager.getInstance;
import static utils.ErrorCodeManager.VCS_BAD_CMD_CODE;

public final class BranchOperation extends VcsOperation {

    /**
     * Branch operation constructor using the AbstractOperation constructor.
     *
     * @param type          type of the operation
     * @param operationArgs operation arguments
     */
    public BranchOperation(OperationType type, ArrayList<String> operationArgs) {
        super(type, operationArgs);
    }

    /**
     * Verifies that the command is good and creates a new branch.
     * @param vcs the vcs
     * @return execution code
     */
    @Override
    public int execute(Vcs vcs) {
        for (VcsBranch branch : vcs.getBranches()) {
            if (operationArgs.get(0).equals(branch.getBranchName())) {
                ErrorCodeManager err = getInstance();
                err.checkExitCode(vcs.getOutputWriter(), VCS_BAD_CMD_CODE);
                return VCS_BAD_CMD_CODE;
            }
        }
        FileSystemSnapshot aux = vcs.getActiveSnapshot().cloneFileSystem();
        vcs.getBranches().add(new VcsBranch(aux, operationArgs.get(0)));
        return 0;
    }
}
