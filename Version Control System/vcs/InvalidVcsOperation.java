// Craciunoiu Cezar
package vcs;

import utils.ErrorCodeManager;
import utils.OperationType;
import static utils.ErrorCodeManager.getInstance;
import static utils.ErrorCodeManager.VCS_BAD_CMD_CODE;

import java.util.ArrayList;

public final class InvalidVcsOperation extends VcsOperation {

    /**
     * Constructor which creates an invalid operation object.
     *
     * @param type          operation type
     * @param operationArgs operation arguments
     */
    public InvalidVcsOperation(OperationType type, ArrayList<String> operationArgs) {
        super(type, operationArgs);
    }

    /**
     * Returns the bad command error.
     *
     * @param vcs the vcs
     * @return error code
     */
    @Override
    public int execute(Vcs vcs) {
        ErrorCodeManager err = getInstance();
        err.checkExitCode(vcs.getOutputWriter(), VCS_BAD_CMD_CODE);
        return VCS_BAD_CMD_CODE;
    }
}
