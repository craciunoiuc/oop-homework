    package strategies;

import roles.Player;
import inventory.Bag;
import inventory.SheriffBag;
import main.Pair;

import java.util.HashMap;
import java.util.List;

/**
 * Clasa Base contine strategia principala de joc, care este mostenita de celelalte 2 clase care
 * implementeaza strategia Greedy si Bribe. Clasa Base implementeaza metodele din interfata
 * Player care contine metodele de baza care ar trebui sa le aiba fiecare jucator.
 * @author Cezar Craciunoiu
 */
public class Base implements Player {
    private final Bag inventory;
    private String name;

    public Base(final int maxCards, final int startingMoney) {
        name = "basic";
        inventory = new Bag(maxCards, startingMoney);
    }

    /**
     * Sunt mutate cate carti este nevoie din pachetul de carti in inventar (pana cand se ajunge
     * la numarul maxim de carti). Este o metoda de baza, fiecare comerciant trebuia sa poata sa
     * traga carti.
     * @param cards Pachetul de carti de unde sunt trase.
     * @param maxCards Numarul maxim de carti din mana.
     */
    public final void fillHand(final Integer[] cards, final int maxCards) {
        int j = 0;
        for (int i = inventory.getNumberOfCards(); i < inventory.getAssets().length; ++i) {
            inventory.getAssets()[i] = cards[j];
            j++;
        }
        inventory.setNumberOfCards(maxCards);
    }

    /**
     * Metoda putInBag este una de baza (fiecare comerciant isi pune bunuri pentru a fi controlate)
     * dar modul de umplere al sacului poate sa difere, de aceea se foloseste metoda calculateBag.
     * @param maxCards Numarul maxim de carti din mana.
     * @param association Tabela de asociere Nume-Id.
     */
    public void putInBag(final int maxCards, final HashMap<String, Integer> association) {
        inventory.getBagToGive().setActualAssets(calculateBag(maxCards, association));
    }

    /**
     * Aceasta metoda creeaza vectorul de perechi Id-NumarDeProduse ce urmeaza sa fie pus in sac,
     * folosindu-se de 3 alte metode calcFrequency, removeOneAsset si removeAssets.
     * Fiind strategia de baza, vectorul de perechi va avea mereu dimensiunea 1
     * (un singur tip de bunuri).
     * @param maxCards Numarul maxim de carti din mana.
     * @param association Tabela de asociere Nume-Id.
     * @return Returneaza un vector de perechi de tip Id-NumarDeProduse ce urmeaza sa fie puse
     * in sac.
     */
    public Pair[] calculateBag(final int maxCards, final HashMap<String, Integer> association) {
        final int assetType = calcFrequency(association);

        if (assetType >= association.get("Silk")) {
            removeOneAsset(assetType);
            Pair[] result = new Pair[1];
            result[0] = new Pair<>(assetType, 1);
            inventory.setDeclaredAssets(0);
            inventory.setNumberOfDeclared(1);
            return result;
        }
        removeAssets(assetType);
        Pair[] result = new Pair[1];
        inventory.setDeclaredAssets(assetType);
        inventory.setNumberOfDeclared(maxCards - inventory.getNumberOfCards());
        result[0] = new Pair<>(assetType, maxCards - inventory.getNumberOfCards());
        return result;
    }

    /**
     * Metoda elimina toate elementele de cu un Id anume din inventar.
     * @param assetType Id-ul produselor ce se doresc eliminate.
     */
    final void removeAssets(final int assetType) {
        int numberOfCards = inventory.getNumberOfCards();
        for (int i = 0; i < numberOfCards; ++i) {
            if (numberOfCards == 1) {
                break;
            }
            if (inventory.getAssets()[i] == assetType) {
                for (int j = i; j < numberOfCards - 1; ++j) {
                    inventory.getAssets()[j] = inventory.getAssets()[j + 1];
                }
                i--;
                numberOfCards--;
            }
        }
        inventory.setNumberOfCards(numberOfCards);
    }

    /**
     * Toti comerciantii ar trebui sa poata sa elimine bunuri de la ei din sac(macar 1). Metoda
     * este asemanatoare cu cea removeAssets, aceasta eliminand doar 1 bun de un tip o data.
     * @param assetType Id-ul bunului de eliminat.
     */
    public final void removeOneAsset(final int assetType) {
        int numberOfCards = inventory.getNumberOfCards();
        for (int i = 0; i < numberOfCards; ++i) {
            if (inventory.getAssets()[i] == assetType) {
                for (int j = i; j < numberOfCards - 1; ++j) {
                    inventory.getAssets()[j] = inventory.getAssets()[j + 1];
                }
                numberOfCards--;
                break;
            }
        }
        inventory.setNumberOfCards(numberOfCards);
    }

    /**
     * Metoda cauta mai intai cel mai frecvent bun care este legal (Id mai mic decat "Chicken").
     * Daca nu se gaseste nici un bun legal se adauga un bun ilegal. Daca doua bunuri au acelasi
     * profit si aceeasi frecventa se returneaza primul gasit (in cazul de fata "Bread" si
     * "Chicken").
     * @param association Tabela de asociere Nume-Id.
     * @return returneaza cel mai frecvent element.
     */
    final int calcFrequency(final HashMap<String, Integer> association) {
        int[] aux = new int[association.get("Barrel") + 1];
        for (int i = 0; i < inventory.getAssets().length; ++i) {
            aux[inventory.getAssets()[i]]++;
        }
        int max = -1;
        int type = -1;
        for (int i = 0; i <= association.get("Chicken"); ++i) {
            if (aux[i] != 0 && aux[i] >= max) {
                max = aux[i];
                type = i;
            }
        }
        if (max == -1) {
            if (aux[association.get("Silk")] != 0) {
                return association.get("Silk");
            } else {
                return (aux[association.get("Pepper")] != 0) ? association.get("Pepper")
                        : association.get("Barrel");
            }
        }
        if (max == aux[association.get("Chicken")] && max == aux[association.get("Bread")]
                && aux[association.get("Bread")] != 0) {
            for (int i = 0; i < inventory.getAssets().length; ++i) {
                if (inventory.getAssets()[i] == (int) association.get("Bread")
                        || inventory.getAssets()[i] == (int) association.get("Chicken")) {
                    return inventory.getAssets()[i];
                }
            }
        }
        return type;
    }

    /**
     * Metoda ce trebuie sa fie implementata de toti jucatorii, deoarece fiecare trebuie sa fie
     * serif la un moment dat. Aceasta apeleaza la randul ei metoda controlPlayer pentru a controla
     * fiecare jucator in parte.
     * @param players Vectorul de saci ai jucatorilor ce urmeaza sa fie controlati.
     * @param pack Pachetul de carti unde o sa se aduce cartile ce nu respecta regula.
     * @param association Tabela de asociere Nume-Id.
     * @param penalty Tabela de asociere Penalty-Id.
     */
    public void control(final SheriffBag[] players, final List<Integer> pack,
                        final HashMap<String, Integer> association,
                        final HashMap<Integer, Integer> penalty) {
        for (SheriffBag playerBag : players) {
            controlPlayer(playerBag, pack, association, penalty);
        }
    }

    /**
     * Metoda cauta bunuri ilegale si bunuri nedeclarate in sacul oferit. Mai intai se verifica
     * daca in sac se afla un singur tip de obiect. Daca da se verifica daca este legal bunul pus
     * sau nu. Daca seriful sau comerciantul trebuie sa plateasca se utilizeaza spatiul din bribe.
     * Daca sunt mai multe de un singur tip se trece prin vector si se procedeaza la fel pentru
     * fiecare element (Se elimina cele legale nedeclarate sau ilegale) cu diferenta ca seriful
     * nu mai ofera bani pentru produsele legale. Se observa ca penalizarea este aceeasi pentru
     * toate produsele legale/ la fel pentru toate cele ilegale.
     * @param player Sacul unui comerciant ce urmeaza sa fie verificat.
     * @param pack Pachetul cu cartile ramase.
     * @param association Tabela de asociere Nume-Id.
     * @param penalty Tabela de asociere Id-Penalty.
     */
    final void controlPlayer(final SheriffBag player, final List<Integer> pack,
                             final HashMap<String, Integer> association,
                             final HashMap<Integer, Integer> penalty) {
        Pair[] aux = player.getActualAssets();
        if (aux.length == 1) {
            if ((int) aux[0].getKey() < association.get("Silk")
                    && (int) aux[0].getValue() == player.getNumberOfDeclared()) {
                player.addBribe(player.getNumberOfDeclared()
                                * penalty.get(association.get("Apple")));
                inventory.addMoney(-(player.getNumberOfDeclared()
                                   * penalty.get(association.get("Apple"))));
            } else {
                if ((int) aux[0].getKey() >= association.get("Silk")) {
                    inventory.addMoney(player.getNumberOfDeclared()
                                       * penalty.get(association.get("Silk")));
                    player.addBribe(-player.getNumberOfDeclared()
                                    * penalty.get(association.get("Silk")));
                    addToPack(aux[0], pack);
                    aux[0] = new Pair<>(aux[0].getKey(), 0);
                } else {
                    if ((int) aux[0].getValue() < association.get("Silk")
                            && (int) aux[0].getValue() != player.getNumberOfDeclared()) {
                        inventory.addMoney(((int) aux[0].getValue()
                                            - player.getNumberOfDeclared())
                                            * penalty.get(association.get("Apple")));
                        player.addBribe(-(((int) aux[0].getValue()
                                        - player.getNumberOfDeclared())
                                        * penalty.get(association.get("Apple"))));
                        addToPack(aux[0], pack);
                        aux[0] = new Pair<>(aux[0].getKey(), player.getNumberOfDeclared());
                    }
                }
            }
        } else {
            for (int j = 0; j < aux.length; ++j) {
                if (aux[j] != null && player.getDeclaredAssets() == (int) aux[j].getKey()) {
                    continue;
                }
                if (aux[j] != null && (int) aux[j].getKey() >= association.get("Silk")) {
                    inventory.addMoney((int) aux[j].getValue()
                                       * penalty.get(association.get("Silk")));
                    player.addBribe(-(int) aux[j].getValue()
                                    * penalty.get(association.get("Silk")));
                    addToPack(aux[j], pack);
                    aux[j] = new Pair<>(aux[j].getKey(), 0);
                } else {
                    if (aux[j] != null && (int) aux[j].getKey() >= 0 && j != 0) {
                        inventory.addMoney((int) aux[j].getValue()
                                           * penalty.get(association.get("Apple")));
                        player.addBribe((-(int) aux[j].getValue()
                                        * penalty.get(association.get("Apple"))));
                        addToPack(aux[j], pack);
                        aux[j] = new Pair<>(aux[j].getKey(), 0);
                    }
                }
            }
        }
        player.setActualAssets(aux);
    }

    /**
     * Metoda adauga o pereche Bun-Numar inapoi in pachetul de carti.
     * @param asset Bunuri ce urmeaza sa fie adaugate in pachet.
     * @param pack Pachetul de carti unde urmeaza sa fie adaugate.
     */
    public final void addToPack(final Pair asset, final List<Integer> pack) {
        if (asset == null) {
            return;
        }
        Integer assetType = (Integer) asset.getKey();
        Integer assetCount = (Integer) asset.getValue();
        while (assetCount > 0) {
            pack.add(assetType);
            assetCount--;
        }
    }

    public final String getName() {
        return name;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final Bag getInventory() {
        return inventory;
    }
}
