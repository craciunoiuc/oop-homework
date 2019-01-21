// Craciunoiu Cezar
package vcs;

import filesystem.FileSystemSnapshot;

public final class VcsBranch {
    private String branchName;
    private VcsCommit initialCommit;
    private VcsCommit head;

    /**
     * Constructor for the new branch, which also creates an initial branch commit.
     *
     * @param branchSnapshot new file system saved for this branch
     * @param branchName name of the new branch
     */
    VcsBranch(FileSystemSnapshot branchSnapshot, String branchName) {
        this.branchName = branchName;
        this.initialCommit = new VcsCommit(null, null, this, branchSnapshot, "First commit");
        head = initialCommit;
    }

    public VcsCommit getHead() {
        return head;
    }

    public void setHead(VcsCommit head) {
        this.head = head;
    }

    VcsCommit getInitialCommit() {
        return initialCommit;
    }

    String getBranchName() {
        return branchName;
    }
}
