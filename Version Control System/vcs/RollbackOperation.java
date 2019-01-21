// Craciunoiu Cezar
package vcs;

import utils.OperationType;

import java.util.ArrayList;

public final class RollbackOperation extends VcsOperation {

    /**
     * Abstract operation constructor for the Rollback Vcs operation.
     *
     * @param type          operation type
     * @param operationArgs operation arguments
     */
    public RollbackOperation(OperationType type, ArrayList<String> operationArgs) {
        super(type, operationArgs);
    }

    /**
     * Copies the lastest commit snapshot to the active one.
     *
     * @param vcs the vcs
     * @return error code
     */
    @Override
    public int execute(Vcs vcs) {
        vcs.setActiveSnapshot(vcs.getBranches().get(vcs.getHead()).getHead().getCommitSnapshot());
        return 0;
    }
}
