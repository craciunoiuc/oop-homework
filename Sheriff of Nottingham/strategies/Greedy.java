package strategies;

import inventory.SheriffBag;
import main.Pair;

import java.util.HashMap;
import java.util.List;

/**
 * Clasa greedy reprezinta o subclasa a clasei Base, deoarece o mare parte din metodele acesteia
 * sunt aceleasi cu cele ale clasei Base. In cazul rundelor impare, de exemplu, functioneaza la fel
 * ca Base.
 * @author Cezar Craciunoiu
 */
public final class Greedy extends Base {
    private boolean roundEven;

    public Greedy(final int maxCards, final int startingMoney) {
        super(maxCards, startingMoney);
        roundEven = false;
        setName("greedy");
    }

    /**
     * Metoda suprascrie pe cea din clasa Base pentru a implementa strategia Greedy.
     * @param maxCards Numarul maxim de carti din mana.
     * @param association Tabela de asociere Nume-Id.
     */
    @Override
    public void putInBag(final int maxCards, final HashMap<String, Integer> association) {
        getInventory().getBagToGive().setActualAssets(calculateBag(maxCards, association));
    }

    /**
     * Metoda functioneaza similar cu cea din base, diferenta fiind ca, atunci cand rundele sunt
     * pare, se mai adauga inca un bun de ilegal, daca se gaseste.
     * @param maxCards Numarul maxim de carti din mana.
     * @param association Tabela de asociere Nume-Id.
     * @return Returneaza un vector de perechi Id-NumarDeBunuri
     */
    @Override
    public Pair[] calculateBag(final int maxCards, final HashMap<String, Integer> association) {
        Pair[] result;
        if (roundEven) {
            result = new Pair[2];
        } else {
            result = new Pair[1];
        }
        final int assetType = calcFrequency(association);
        if (assetType >= association.get("Silk")) {
            removeOneAsset(assetType);
            result[0] = new Pair<>(assetType, 1);
            getInventory().setDeclaredAssets(0);
            getInventory().setNumberOfDeclared(1);
        } else {
            removeAssets(assetType);
            result[0] = new Pair<>(assetType, maxCards - getInventory().getNumberOfCards());
            getInventory().setDeclaredAssets(assetType);
            getInventory().setNumberOfDeclared(maxCards - getInventory().getNumberOfCards());
        }
        if (!roundEven) {
            roundEven = true;
            return result;
        } else {
            roundEven = false;
            int resultAux = findIllegal(association);
            if (resultAux != -1) {
                removeOneAsset(resultAux);
                if (resultAux != (int) result[0].getKey()) {
                    result[1] = new Pair<>(resultAux, 1);
                } else {
                    Pair[] aux = new Pair[1];
                    aux[0] = new Pair<>(resultAux, 2);
                    result = aux;
                }
                getInventory().setNumberOfDeclared(getInventory().getNumberOfDeclared() + 1);
            }
            return result;
        }
    }

    /**
     * Metoda cauta si returneaza cel mai valoros obiect ce il gaseste.
     * @param association Tabela de asociere Nume-Id.
     * @return Returneaza cel mai valoros obiect ce se gaseste.
     */
    private int findIllegal(final HashMap<String, Integer> association) {
        String[] illegalAssets = {"Silk", "Pepper", "Barrel"};
        for (String asset : illegalAssets) {
            for (int i = 0; i < getInventory().getNumberOfCards(); ++i) {
                if (getInventory().getAssets()[i]  == (int) association.get(asset)) {
                    return association.get(asset);
                }
            }
        }
        return -1;
    }

    /**
     * Metoda functioneaza aproximativ la fel cu cea din clasa Base, diferenta fiind, ca daca a
     * fost oferita mita, nu se mai controleaza sacosa.
     * @param players Vectorul de saci ai jucatorilor ce urmeaza sa fie controlati.
     * @param pack Pachetul de carti unde o sa se aduce cartile ce nu respecta regula.
     * @param association Tabela de asociere Nume-Id.
     * @param penalty Tabela de asociere Penalty-Id.
     */
    @Override
    public void control(final SheriffBag[] players, final List<Integer> pack,
                        final HashMap<String, Integer> association,
                        final HashMap<Integer, Integer> penalty) {
        for (SheriffBag playerBag : players) {
            if (playerBag == getInventory().getBagToGive()) {
                continue;
            }
            if (playerBag.getBribe() == 0) {
                controlPlayer(playerBag, pack, association, penalty);
            } else {
                getInventory().addMoney(playerBag.getBribe());
                playerBag.setBribe(0);
            }
        }
    }
}
