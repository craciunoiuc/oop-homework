package strategies;

import main.Pair;

import java.util.HashMap;

/**
 * Clasa Bribe este din mult epuncte de vedere asemanatoare cu cea Base, cum ar fi modul de
 * inspectie al sacilor, de aceea aceasta extinde clasa Base. De asemenea unele metode din clasa
 * Bribe se folosesc de metode mostenite din clasa Base.
 * @author Cezar Craciunoiu
 */
public final class Bribe extends Base {

    public Bribe(final int maxCards, final int startingMoney) {
        super(maxCards, startingMoney);
        setName("bribed");
    }

    /**
     * Metoda functioneaza la fel ca cea din Base, dar foloseste alta metoda de calcul.
     * @param maxCards Numarul maxim de carti din mana.
     * @param association Tabela de asociere Nume-Id.
     */
    @Override
    public void putInBag(final int maxCards, final HashMap<String, Integer> association) {
        getInventory().getBagToGive().setActualAssets(bribeCalculateBag(maxCards, association));
    }

    /**
     * Metoda implementeaza strategia negustorului de tip bribe. Aceasta verifica daca exista
     * numarul de bunuri necesar sau daca exista fondurile necesare. In primul caz in care nu sunt
     * bani sau bunuri ilegale se utilizeaza strategia Basic. In cazul 2 in care sunt 2 carti sau
     * mai putine sau nu sunt destui bani se adauga decat maxim 2 carti si se pun 5 bani. In
     * ultimul caz se pun 10 bani si se cauta toate cartile ilegale in ordine descrescatoare
     * a profitului. Se construieste un vector de Pair in functie de cate tipuri diferite de
     * bunuri ilegale au fost gasite. Metoda se foloseste de variabila contor pentru a face
     * atribuirea valorilor in vectorul rezultat, pentru a se folosi o dimensiune corecta.
     * @param maxCards Numarul maxim de carti din mana.
     * @param association Tabela de asociere Nume-Id.
     * @return Returneaza perechile de tip Id-NumarDeProduse rezultate.
     */
    private Pair[] bribeCalculateBag(final int maxCards,
                                     final HashMap<String, Integer> association) {
        final int minMoney = 5;
        final int maxBagSize = 3;
        if (getInventory().getMoney() < minMoney || numberOfIllegals(association) == 0) {
            return calculateBag(maxCards, association);
        }
        Pair[] result;
        if (numberOfIllegals(association) <= 2 || getInventory().getMoney() < minMoney * 2) {
            getInventory().getBagToGive().setBribe(minMoney);
            getInventory().setMoney(getInventory().getMoney() - minMoney);
            int duplicate = findIllegal(association);
            removeOneAsset(findIllegal(association));
            if (duplicate == findIllegal(association)) {
                result = new Pair[1];
                result[0] = new Pair<>(findIllegal(association), 2);
                getInventory().setNumberOfDeclared(2);
            } else {
                if (findIllegal(association) == -1) {
                    result = new Pair[1];
                    result[0] = new Pair<>(duplicate, 1);
                    getInventory().setNumberOfDeclared(1);
                } else {
                    result = new Pair[2];
                    result[0] = new Pair<>(duplicate, 1);
                    result[1] = new Pair<>(findIllegal(association), 1);
                    getInventory().setNumberOfDeclared(2);
                }
            }
            removeOneAsset(findIllegal(association));
            getInventory().setDeclaredAssets(0);
            return result;
        } else {
            getInventory().getBagToGive().setBribe(minMoney * 2);
            getInventory().setMoney(getInventory().getMoney() - (minMoney * 2));
            int contor = maxCards - 1;
            final int contorStart = contor;
            int[] resultAux = new int[maxBagSize];
            getInventory().setDeclaredAssets(0);
            while (contor > 0 && findIllegal(association) != -1) {
                resultAux[findIllegal(association) % maxBagSize]++;
                removeOneAsset(findIllegal(association));
                contor--;
            }
            getInventory().setNumberOfDeclared(contorStart - contor);
            getInventory().setDeclaredAssets(0);
            contor = 0;
            for (int it : resultAux) {
                if (it != 0) {
                    contor++;
                }
            }
            result = new Pair[contor];
            if (contor == maxBagSize) {
                result = new Pair[maxBagSize];
                result[0] = new Pair<>(association.get("Silk"), resultAux[1]);
                result[1] = new Pair<>(association.get("Pepper"), resultAux[2]);
                result[2] = new Pair<>(association.get("Barrel"), resultAux[0]);
            } else {
                if (contor == 1) {
                    result = new Pair[1];
                    if (resultAux[0] > 0) {
                        result[0] = new Pair<>(association.get("Barrel"), resultAux[0]);
                    }
                    if (resultAux[1] > 0) {
                        result[0] = new Pair<>(association.get("Silk"), resultAux[1]);
                    }
                    if (resultAux[2] > 0) {
                        result[0] = new Pair<>(association.get("Pepper"), resultAux[2]);
                    }
                } else {
                    result = new Pair[2];
                    int i = 0;
                    if (resultAux[0] > 0) {
                        result[i] = new Pair<>(association.get("Barrel"), resultAux[0]);
                        i++;
                    }
                    if (resultAux[1] > 0) {
                        result[i] = new Pair<>(association.get("Silk"), resultAux[1]);
                        i++;
                    }
                    if (resultAux[2] > 0) {
                        result[i] = new Pair<>(association.get("Pepper"), resultAux[2]);
                    }
                }
            }
            return result;
        }

    }

    /**
     * Metoda numara cartile care au Id-ul mai mare ca cel al lui "Silk".
     * @param association Tabela de asociere Nume-Id.
     * @return Returneaza numarul de produse ilegale din mana.
     */
    private int numberOfIllegals(final HashMap<String, Integer> association) {
        int count = 0;
        for (int i = 0; i < getInventory().getNumberOfCards(); ++i) {
            if (getInventory().getAssets()[i] >= association.get("Silk")) {
                count++;
            }
        }
        return count;
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
}
