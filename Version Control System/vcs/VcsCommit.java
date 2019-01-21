// Craciunoiu Cezar
package vcs;

import filesystem.FileSystemSnapshot;
import utils.AbstractOperation;
import static utils.IDGenerator.generateCommitID;

import java.util.ArrayList;
import java.util.List;

public final class VcsCommit {
    private FileSystemSnapshot commitSnapshot;
    private List<AbstractOperation> trackedOperations;
    private VcsCommit parent;
    private VcsCommit child;
    private Integer id;
    private VcsBranch branch;
    private String commitMessage;

    /**
     * A commit hold the file system with the changes made since the last commit, new stages
     * operations added, an id and a message. The commits interact with each other in a
     * linked-list fashion, each commit having a parent and a child.
     *
     * @param parent earlier commit
     * @param child newer commit
     * @param branch branch where the commit belongs
     * @param commitSnapshot file system snapshot of the commit
     * @param commitMessage commit message
     */
    VcsCommit(VcsCommit parent, VcsCommit child, VcsBranch branch,
              FileSystemSnapshot commitSnapshot, String commitMessage) {
        this.parent = parent;
        this.child = child;
        if (parent != null) {
            parent.setChild(this);
        }
        this.commitMessage = commitMessage;
        this.commitSnapshot = commitSnapshot;
        this.branch = branch;
        trackedOperations = new ArrayList<>();
        this.id = generateCommitID();
        branch.setHead(this);
    }

    public String getCommitMessage() {
        return commitMessage;
    }

    public VcsBranch getBranch() {
        return branch;
    }

    public VcsCommit getParent() {
        return parent;
    }

    public VcsCommit getChild() {
        return child;
    }

    public Integer getId() {
        return id;
    }

    public List<AbstractOperation> getTrackedOperations() {
        return trackedOperations;
    }

    public void setChild(VcsCommit child) {
        this.child = child;
    }

    public FileSystemSnapshot getCommitSnapshot() {
        return commitSnapshot;
    }
}
