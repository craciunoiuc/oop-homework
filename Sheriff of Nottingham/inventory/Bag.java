package inventory;

import java.util.HashMap;

/**
 * In clasa Bag sunt salvate toate informatiile in legatura cu inventarul jucatorului, de la bani
 * pana la obiectele care le are asupra sa. Totul in afara de nume.
 * @author Cezar Craciunoiu
 */
public final class Bag {
    private SheriffBag bagToGive;
    private Integer[] assets;
    private int money;
    private int numberOfCards;
    private HashMap<Integer, Integer> score;

    /**
     * Constructorul initializeaza atat valorile de inceput cat si creeaza un obiect de tip
     * SheriffBag care va fi folosit la inspectie, respectiv un HashMap pentru pastrarea scorului.
     * @param maxCards Numarul maxim de carti din mana.
     * @param startingMoney Valoarea de inceput a banilor.
     */
    public Bag(final int maxCards, final int startingMoney) {
        this.assets = new Integer[maxCards];
        bagToGive = new SheriffBag();
        numberOfCards = 0;
        money = startingMoney;
        score = new HashMap<>();
    }

    public HashMap<Integer, Integer> getScore() {
        return score;
    }

    public void setDeclaredAssets(final int declaredAssets) {
        this.bagToGive.setDeclaredAssets(declaredAssets);
    }

    public void setNumberOfDeclared(final int numberOfDeclared) {
        this.bagToGive.setNumberOfDeclared(numberOfDeclared);
    }

    public int getNumberOfDeclared() {
        return this.bagToGive.getNumberOfDeclared();
    }

    public SheriffBag getBagToGive() {
        return bagToGive;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(final int money) {
        this.money = money;
    }

    public void addMoney(final int additionalMoney) {
        money += additionalMoney;
    }

    public int getNumberOfCards() {
        return numberOfCards;
    }

    public void setNumberOfCards(final int numberOfCards) {
        this.numberOfCards = numberOfCards;
    }

    public Integer[] getAssets() {
        return assets;
    }
}
