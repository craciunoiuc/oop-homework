// Craciunoiu Cezar
package vcs;

import utils.AbstractOperation;
import utils.OperationType;
import utils.OutputWriter;

import java.util.ArrayList;

public final class StatusOperation extends VcsOperation {

    /**
     * Abstract Operation constructor for the status Vcs operation.
     *
     * @param type          operation type
     * @param operationArgs operation arguments
     */
    public StatusOperation(OperationType type, ArrayList<String> operationArgs) {
        super(type, operationArgs);
    }

    /**
     * Prints all the tracked operations from the head commit.
     *
     * @param vcs the vcs
     * @return exit code
     */
    @Override
    public int execute(Vcs vcs) {
        OutputWriter writer = vcs.getOutputWriter();
        writer.write("On branch: " + vcs.getBranches().get(vcs.getHead()).getBranchName() + '\n');
        writer.write("Staged changes:" + '\n');
        for (AbstractOperation op
                : vcs.getBranches().get(vcs.getHead()).getHead().getTrackedOperations()) {
            if (op.getType() == OperationType.TOUCH) {
                writer.write('\t' + "Created file " + op.getOperationArgs().get(1) + '\n');
            }
            if (op.getType() == OperationType.MAKEDIR) {
                writer.write('\t' + "Created directory " + op.getOperationArgs().get(1) + '\n');
            }
            if (op.getType() == OperationType.WRITETOFILE) {
                writer.write("\tAdded \"" + op.getOperationArgs().get(0) + "\" to file "
                             + op.getOperationArgs().get(1) + '\n');
            }
        }
        return 0;
    }
}
