package main;

import roles.Player;
import strategies.Base;
import strategies.Bribe;
import strategies.Greedy;
import inventory.SheriffBag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * In clasa GameExecution este implementata, cu ajutorul mai multor metode statice, logica
 * problemei, in ordinea etapelor date in cerinta. Cu ajutorul acestora se pare intr-un mod lizibil
 * efortul de calcul.
 * @author Cezar Craciunoiu
 */
final class GameExecution {

    private GameExecution() {
    }

    /**
     * Metoda startGame instantiaza obiectele corespunzatoare fiecarei strategii. Deoarece
     * strategiile Greedy si Bribe mostenesc strategia de tip Base, si, pana la urma, toate
     * strategiile ce s-ar mai introduce in joc ar avea la baza pe Base, vectorul de jucatori este
     * de tip Base.
     * @param players Lista cu numele jucatorilor.
     * @param maxCards Numarul maxim de carti din mana unui jucator.
     * @param startMoney Suma initiala a jucatorilor.
     * @return Se returneaza un vector cu toti jucatorii din meciul curent.
     */
    static Player[] startGame(final List<String> players, final int maxCards,
                              final int startMoney) {
        int i = 0;
        Player[] strategies = new Base[players.size()];
        while (i != players.size()) {
            String playerName = players.get(i);
            if (playerName.equals("greedy")) {
                strategies[i] = new Greedy(maxCards, startMoney);
            }
            if (playerName.equals("basic")) {
                strategies[i] = new Base(maxCards, startMoney);
            }
            if (playerName.equals("bribed")) {
                strategies[i] = new Bribe(maxCards, startMoney);
            }
            i++;
        }
        return strategies;
    }

    /**
     * Metoda ce trage carti la rand pentru fiecare jucator(in ordinea de la citire) pana cand
     * fiecare are maxCards carti.
     * @param players Vectorul de jucatori din meciul actual.
     * @param cardPack Lista de carti ramase in joc in pachet.
     * @param maxCards Numarul maxim de carti din mana.
     */
    static void drawCards(final Player[] players, final List<Integer> cardPack,
                          final int maxCards) {
        Integer[] aux = new Integer[maxCards];
        for (Player player : players) {
            int j = 0;
            while (j < maxCards - player.getInventory().getNumberOfCards()) {
                aux[j] = cardPack.remove(0);
                j++;
            }
            player.fillHand(aux, maxCards);
        }
    }

    /**
     * Metoda ce apeleaza metoda specifica fiecarui obiect atat timp cat acesta nu se afla in
     * rolul de serif.
     * @param sheriff String unde este retinut cine este seriful in runda curenta.
     * @param players Vectorul de jucatori.
     * @param maxCards Numarul de carti maxim de mana unui jucator.
     * @param association Tabela de asociere Nume-Id.
     */
    static void declareAssets(final String sheriff, final Player[] players, final int maxCards,
                              final HashMap<String, Integer> association) {
        for (Player player : players) {
            if (sheriff.equals(player.getName())) {
                continue;
            }
            player.putInBag(maxCards, association);
        }
    }

    /**
     * Metoda ce apeleaza metoda din clasa obiectului ce are rolul de serif in acea runda,
     * avandu-se grija sa nu se inspecteze si pe el insusi.
     * @param sheriff String ce retine denumirea serifului din acea runda.
     * @param players Vectorul cu  jucatori.
     * @param cardPack Lista de carti ramase in pachet.
     * @param association Asocierea de tip Nume-Id.
     * @param penalty Asocierea de tip Id-Penalty.
     */
    static void inspectBags(final String sheriff, final Player[] players,
                            final List<Integer> cardPack,
                            final HashMap<String, Integer> association,
                            final HashMap<Integer, Integer> penalty) {
        int sheriffId = -1;
        for (int i = 0; i < players.length; ++i) {
            if (players[i].getName().equals(sheriff)) {
                   sheriffId = i;
                   break;
            }
        }
        for (int i = 0; i < players.length; ++i) {
            if (i == sheriffId) {
                continue;
            }
            SheriffBag[] aux = new SheriffBag[1];
            aux[0] = players[i].getInventory().getBagToGive();
            players[sheriffId].control(aux, cardPack, association, penalty);
        }
    }

    /**
     * Metoda verifica "ce a ramas" dupa executia metodei de inspectare are pungilor fiecarui
     * comerciant. Mai intai se adauga inapoi la bani suma data la mita(daca este negativa inseamna
     * ca a fost penalizat comerciantul), apoi se trece prin vectorul de Pair din punga ce a fost
     * inspectata si se adauga la scor fiecare obiect ce a ramas in aceasta.
     * @param sheriff Seriful in runda actuala.
     * @param players Vectorul de obiecte jucator.
     */
    static void sellGoods(final String sheriff, final Player[] players) {
        for (Player player : players) {
            if (player.getName().equals(sheriff)) {
                continue;
            }
            int bribe = player.getInventory().getBagToGive().getBribe();
            if (bribe != 0) {
                player.getInventory().addMoney(bribe);
                player.getInventory().getBagToGive().setBribe(0);
            }
            Pair[] remainingAssets = player.getInventory().getBagToGive().getActualAssets();
            for (Pair asset : remainingAssets) {
                if (asset != null && (int) asset.getKey() != -1) {
                    int numberOfGoods = (int) asset.getValue();
                    while (numberOfGoods > 0) {
                        HashMap<Integer, Integer> aux = player.getInventory().getScore();
                        if (aux.containsKey((Integer) asset.getKey())) {
                            aux.put((Integer) asset.getKey(),
                                    aux.get((Integer) asset.getKey()) + 1);
                        } else {
                            aux.put((Integer) asset.getKey(), 1);
                        }
                        numberOfGoods--;
                    }
                }
            }
        }
    }

    /**
     * Metoda expandIllegalAssets se foloseste de o variabila auxiliara pentru a acorda bonusurile
     * obtinute in urma vanzarii bunurilor ilegale. Pentru fiecare tip de obiect se verifica
     * cate obiecte din acest tip sunt si se adauga la scor. Se folosesc doi vectori de String
     * in care sunt retinute pentru fiecare bun ilegal tipul de bunuri legale primite ca bonus.
     * @param players Vectorul de obiecte de jucatori.
     * @param association Asocierea Nume-Id.
     * @param bonus Asocierea Id-Bonus. Unde bonusul e o pereche King/Queen + bonusurile ilegale.
     */
    static void expandIllegalAssets(final Player[] players,
                                    final HashMap<String, Integer> association,
                                    final HashMap<Integer, Pair<Integer, Integer>> bonus) {
        HashMap<Integer, Integer> aux;
        String[] assets = {"Silk", "Pepper", "Barrel"};
        String[] assetsRewards = {"Cheese", "Chicken", "Bread"};
        for (Player player : players) {
            aux = player.getInventory().getScore();
            for (int j = 0; j < assets.length; ++j) {
                int count = 0;
                if (aux.containsKey(association.get(assets[j]))) {
                    count = aux.get(association.get(assets[j]));
                }
                if (aux.containsKey(association.get(assetsRewards[j]))) {
                    aux.replace(association.get(assetsRewards[j]),
                                bonus.get(association.get(assets[j])).getKey()
                                * count + aux.get(association.get(assetsRewards[j])));
                } else {
                    aux.put(association.get(assetsRewards[j]),
                            bonus.get(association.get(assets[j])).getKey() * count);
                }
            }
        }
    }

    /**
     * Metoda selecteaza castigatorii bonusului de final. Mai intai cauta primul maxim si ofera
     * King la toti cei care au numar maxim, apoi cauta al doilea maxim si ofera bonusul de Queen
     * acestora. Cei care nu au fost marcati pana la final nu primesc nici un bonus. In marcarea
     * cu King/Queen se tine cont si de cazul in care 2 jucatori au acelasi numar de produse.
     * @param players Vectorul de jucatori.
     * @param numberOfProducts numarul de produse legale.
     * @return Returneaza un vector de int ce contine {-1, 0, 1}, adica Queen/Nimic/King.
     */
    private static int[] selectWinners(final Player[] players, final int numberOfProducts) {
        int[] playersAwards = new int[players.length];
        int max1;
        int max2;
        max1 = 0;
        for (int i = 0; i < playersAwards.length; ++i) {
            playersAwards[i] = 0;
        }
        for (Player player : players) {
            if (player.getInventory().getScore().containsKey(numberOfProducts)) {
                if (player.getInventory().getScore().get(numberOfProducts) > max1) {
                    max1 = player.getInventory().getScore().get(numberOfProducts);
                }
            }
        }
        for (int i = 0; i < players.length; ++i) {
            if (players[i].getInventory().getScore().containsKey(numberOfProducts)) {
                if (players[i].getInventory().getScore().get(numberOfProducts) == max1) {
                    playersAwards[i] = 1;
                }
            } else {
                if (max1 == 0) {
                    playersAwards[i] = 1;
                }
            }
        }
        if (max1 != 0) {
            max2 = 0;
            for (Player player : players) {
                if (player.getInventory().getScore().containsKey(numberOfProducts)) {
                    if (player.getInventory().getScore().get(numberOfProducts) > max2
                            && max1 > player.getInventory().getScore().get(numberOfProducts)) {
                        max2 = player.getInventory().getScore().get(numberOfProducts);
                    }
                }
            }
            for (int i = 0; i < players.length; ++i) {
                if (players[i].getInventory().getScore().containsKey(numberOfProducts)) {
                    if (players[i].getInventory().getScore().get(numberOfProducts) == max2) {
                        if (playersAwards[i] != 1) {
                            playersAwards[i] = -1;
                        }
                    }
                } else {
                    if (max2 == 0) {
                        if (playersAwards[i] != 1) {
                            playersAwards[i] = -1;
                        }
                    }
                }
            }
        }
        return playersAwards;
    }

    /**
     * Se foloseste de metoda selectWinner pentru a oferi bonusurile de King si Queen. La bonusul
     * de Queen se observa ca toate bonusurile sunt la fel ca cel de King-Chicken inafara de
     * Queen-Chicken unde se ofera cu 5 mai putin.
     * @param players Vectorul de jucatori.
     * @param association Tabela de asociere Nume-Id.
     * @param bonus Asocierea Id-Bonus. Unde bonusul e o pereche King/Queen + bonusurile ilegale.
     */
    static void giveAward(final Player[] players, final HashMap<String, Integer> association,
                          final HashMap<Integer, Pair<Integer, Integer>> bonus) {
        int[] playersAwards;
        String[] legalAssets = {"Apple", "Cheese", "Bread", "Chicken"};
        final int maxNumberOfProducts = legalAssets.length - 1;
        int numberOfProducts = maxNumberOfProducts;
        while (numberOfProducts >= 0) {
            playersAwards = selectWinners(players, numberOfProducts);
            for (int i = 0; i < players.length; ++i) {
                if (playersAwards[i] == 1) {
                    players[i].getInventory().addMoney(bonus.get(association.get(
                            legalAssets[numberOfProducts])).getKey());
                } else {
                    if (playersAwards[i] == -1) {
                            players[i].getInventory().addMoney(bonus.get(association.get(
                                    legalAssets[numberOfProducts])).getValue());
                    }
                }
            }
            numberOfProducts--;
        }
    }

    /**
     * Metoda trece prin toate cheile din tabela de asociere Nume-Id si aduna la rezultat daca
     * este gasita o valoare.
     * @param player Un jucator din vectorul de jucatori.
     * @param association Tabela de asociere Nume-Id.
     * @param prices Tabela de asociere Id-Pret.
     * @return Scorul obtinut in urma calcularii scorului.
     */
    static int calculateScore(final Player player, final HashMap<String, Integer> association,
                              final HashMap<Integer, Integer> prices) {
        List<String> keyList = new ArrayList<>(association.keySet());
        int result = player.getInventory().getMoney();
        for (String key : keyList) {
            if (player.getInventory().getScore().containsKey(association.get(key))) {
                result += player.getInventory().getScore().get(association.get(key))
                        * prices.get(association.get(key));
            }
        }
        return result;
    }
}
