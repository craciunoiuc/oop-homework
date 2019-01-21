package inventory;

import main.Pair;

/**
 * In aceasta clasa sunt salvate informatiile importante in legatura cu obiectele din sacul ce
 * urmeaza sa fie dat catre inspectie. O instanta a acestei clase va fi detinuta de clasa Bag.
 * @author Cezar Craciunoiu
 */
public final class SheriffBag {
    private int bribe;
    private int declaredAssets;
    private int numberOfDeclared;
    private Pair[] actualAssets;

    SheriffBag() {
        bribe = 0;
        declaredAssets = -1;
        numberOfDeclared = 0;
    }

    public void addBribe(final int additionalBribe) {
        bribe += additionalBribe;
    }

    public int getBribe() {
        return this.bribe;
    }

    public int getDeclaredAssets() {
        return declaredAssets;
    }

    public int getNumberOfDeclared() {
        return numberOfDeclared;
    }

    public Pair[] getActualAssets() {
        return actualAssets;
    }

    public void setBribe(final int bribe) {
        this.bribe = bribe;
    }

    void setDeclaredAssets(final int declaredAssets) {
        this.declaredAssets = declaredAssets;
    }

    void setNumberOfDeclared(final int numberOfDeclared) {
        this.numberOfDeclared = numberOfDeclared;
    }

    public void setActualAssets(final Pair[] actualAssets) {
        this.actualAssets = actualAssets;
    }
}
