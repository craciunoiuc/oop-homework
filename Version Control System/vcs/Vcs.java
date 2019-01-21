package vcs;

import filesystem.FileSystemOperation;
import filesystem.FileSystemSnapshot;
import utils.AbstractOperation;
import utils.OutputWriter;
import utils.Visitor;

import java.util.ArrayList;
import java.util.List;

public final class Vcs implements Visitor {
    private final OutputWriter outputWriter;
    private FileSystemSnapshot activeSnapshot;
    private int head;
    private List<VcsBranch> branches;

    /**
     * Vcs constructor.
     *
     * @param outputWriter the output writer
     */
    public Vcs(OutputWriter outputWriter) {
        this.outputWriter = outputWriter;
    }

    /**
     * Does initialisations.
     */
    public void init() {
        this.activeSnapshot = new FileSystemSnapshot(outputWriter);
        this.head = 0;
        this.branches = new ArrayList<>();
        branches.add(new VcsBranch(activeSnapshot.cloneFileSystem(), "master"));
    }

    /**
     * Visits a file system operation.
     *
     * @param fileSystemOperation the file system operation
     * @return the return code
     */
    public int visit(FileSystemOperation fileSystemOperation) {

        return fileSystemOperation.execute(this.activeSnapshot);
    }

    /**
     * Visits a vcs operation.
     *
     * @param vcsOperation the vcs operation
     * @return return code
     */
    @Override
    public int visit(VcsOperation vcsOperation) {
        vcsOperation.execute(this);
        return 0;
    }

    /**
     * Adds a file system operation to the list of the current head.
     *
     * @param abstractOperation file system operation
     */
    public void track(AbstractOperation abstractOperation) {
        branches.get(head).getHead().getTrackedOperations().add(abstractOperation);
    }

    public FileSystemSnapshot getActiveSnapshot() {
        return activeSnapshot;
    }

    public OutputWriter getOutputWriter() {
        return outputWriter;
    }

    public void setActiveSnapshot(FileSystemSnapshot aux) {
        this.activeSnapshot = aux;
    }

    public List<VcsBranch> getBranches() {
        return this.branches;
    }

    public int getHead() {
        return this.head;
    }

    public void setHead(int head) {
        this.head = head;
    }
}
