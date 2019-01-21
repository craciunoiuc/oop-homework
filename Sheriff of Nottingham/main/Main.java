package main;

import roles.Player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Clasa Main este folosita in principal pentru parcurgerea intrarilor, cat si pentru initializarea
 * datelor din program. De asemenea, in Main se Ruleaza metodele din GameExecution pentru a derula
 * jocul in ordinea etapelor de desfasurare. De asemenea la final se sorteaza si afiseaza
 * clasamentul jucatorilor.
 * @author Cezar Craciunoiu
 */
public final class Main {

    private Main() { }
    private static final class GameInputLoader {
        private final String mInputPath;

        private GameInputLoader(final String path) {
            mInputPath = path;
        }

        public GameInput load() {
            List<Integer> assetsIds = new ArrayList<>();
            List<String> playerOrder = new ArrayList<>();

            try {
                BufferedReader inStream = new BufferedReader(new FileReader(mInputPath));
                String assetIdsLine = inStream.readLine().replaceAll("[\\[\\] ']", "");
                String playerOrderLine = inStream.readLine().replaceAll("[\\[\\] ']", "");

                for (String strAssetId : assetIdsLine.split(",")) {
                    assetsIds.add(Integer.parseInt(strAssetId));
                }

                for (String strPlayer : playerOrderLine.split(",")) {
                    playerOrder.add(strPlayer);
                }
                inStream.close();


            } catch (IOException e) {
                e.printStackTrace();
            }
            return new GameInput(assetsIds, playerOrder);
        }
    }

    /**
     * Functia realizeaza o asociere de tip cheie valoare intre produse si diferite proprietati
     * ale lor. Deoarece datele nu au fost oferite la intrare ele au fost introduse direct in
     * tabele, simulandu-se citirea lor de la tastatura a valorilor ca string-uri.
     * @param prices Aici se salveaza asocierea intre Id si Profitul obtinut in urma vinderii.
     * @param penalty Aici se salveaza asocierea Id-Penalty pentru cazurile in care negustorul
     *                este sanctionat.
     * @param association Aceasta este tabela principala de asociere, aici se salveaza asocierile
     *                    de tip Nume-Id
     * @param bonus In bonus sunt salvate obtinute din expandarea bunurilor ilegale, cat si
     *              bonusurile de tip King/Queen
     */
    private static void buildMaps(final HashMap<Integer, Integer> prices,
                                  final HashMap<Integer, Integer> penalty,
                                  final HashMap<String, Integer> association,
                                  final HashMap<Integer, Pair<Integer, Integer>> bonus) {
        association.put("Apple", Integer.parseInt("0"));
        association.put("Cheese", Integer.parseInt("1"));
        association.put("Bread", Integer.parseInt("2"));
        association.put("Chicken", Integer.parseInt("3"));
        association.put("Silk", Integer.parseInt("10"));
        association.put("Pepper", Integer.parseInt("11"));
        association.put("Barrel", Integer.parseInt("12"));

        prices.put(association.get("Apple"), Integer.parseInt("2"));
        prices.put(association.get("Cheese"), Integer.parseInt("3"));
        prices.put(association.get("Bread"), Integer.parseInt("4"));
        prices.put(association.get("Chicken"), Integer.parseInt("4"));
        prices.put(association.get("Silk"), Integer.parseInt("9"));
        prices.put(association.get("Pepper"), Integer.parseInt("8"));
        prices.put(association.get("Barrel"), Integer.parseInt("7"));

        penalty.put(association.get("Apple"), Integer.parseInt("2"));
        penalty.put(association.get("Cheese"), Integer.parseInt("2"));
        penalty.put(association.get("Bread"), Integer.parseInt("2"));
        penalty.put(association.get("Chicken"), Integer.parseInt("2"));
        penalty.put(association.get("Silk"), Integer.parseInt("4"));
        penalty.put(association.get("Pepper"), Integer.parseInt("4"));
        penalty.put(association.get("Barrel"), Integer.parseInt("4"));

        bonus.put(association.get("Silk"), new Pair<>(Integer.parseInt("3"),
                  Integer.parseInt("3")));
        bonus.put(association.get("Pepper"), new Pair<>(Integer.parseInt("2"),
                  Integer.parseInt("2")));
        bonus.put(association.get("Barrel"), new Pair<>(Integer.parseInt("2"),
                  Integer.parseInt("2")));
        bonus.put(association.get("Apple"), new Pair<>(Integer.parseInt("20"),
                  Integer.parseInt("10")));
        bonus.put(association.get("Cheese"), new Pair<>(Integer.parseInt("15"),
                  Integer.parseInt("10")));
        bonus.put(association.get("Bread"), new Pair<>(Integer.parseInt("15"),
                  Integer.parseInt("10")));
        bonus.put(association.get("Chicken"), new Pair<>(Integer.parseInt("10"),
                  Integer.parseInt("5")));
    }

    /**
     * Deoarece s-a ales sa se foloseasca o clasa intermediara cu metode statice pentru rularea
     * logicii programului, in main doar se initializeaza datele. Fiecare etapa/metoda este rulata
     * din interiorul clasei GameExecution care contine metode denumite sugestiv, pentru a
     * modulariza executia acestuia. De asemenea, in main se realizeaza sortarea jucatorilor si
     * afisarea clasamentului final.
     * @param args In args sunt primite datele despre pachetul de carti si ordinea jucatorilor.
     */
    public static void main(final String[] args) {
        GameInputLoader gameInputLoader = new GameInputLoader(args[0]);
        GameInput gameInput = gameInputLoader.load();
        int playersNumber = gameInput.getPlayerNames().size();
        final int maxCards = 6;
        final int startingMoney = 50;
        final int rounds = 2 * playersNumber;
        final HashMap<String, Integer> assetsAssociation = new HashMap<>();
        final HashMap<Integer, Integer> assetsPrice = new HashMap<>();
        final HashMap<Integer, Integer> assetsPenalty = new HashMap<>();
        final HashMap<Integer, Pair<Integer, Integer>> assetsBonus = new HashMap<>();
        buildMaps(assetsPrice, assetsPenalty, assetsAssociation, assetsBonus);
        int currentRound = 0;
        Player[] players = GameExecution.startGame(gameInput.getPlayerNames(),
                                                 maxCards, startingMoney);
        while (currentRound < rounds) {
            String newSheriff = gameInput.getPlayerNames().get(currentRound % playersNumber);
            GameExecution.drawCards(players, gameInput.getAssetIds(), maxCards);
            GameExecution.declareAssets(newSheriff, players, maxCards, assetsAssociation);

            GameExecution.inspectBags(newSheriff, players, gameInput.getAssetIds(),
                                      assetsAssociation, assetsPenalty);
            GameExecution.sellGoods(newSheriff, players);
            currentRound++;
        }
        GameExecution.expandIllegalAssets(players, assetsAssociation, assetsBonus);
        GameExecution.giveAward(players, assetsAssociation, assetsBonus);
        int[] result = new int[playersNumber];
        int[] resultPlayer = new int[playersNumber];
        for (int i = 0; i < playersNumber; ++i) {
            resultPlayer[i] = i;
            result[i] = GameExecution.calculateScore(players[i], assetsAssociation, assetsPrice);
        }
        for (int i = 0; i < playersNumber - 1; ++i) {
            for (int j = i + 1; j < playersNumber; ++j) {
                if (result[i] < result[j]) {
                    int aux = result[i];
                    result[i] = result[j];
                    result[j] = aux;
                    aux = resultPlayer[i];
                    resultPlayer[i] = resultPlayer[j];
                    resultPlayer[j] = aux;
                }
            }
        }
        for (int i = 0; i < playersNumber; ++i) {
            System.out.println(players[resultPlayer[i]].getName().toUpperCase() + ": " + result[i]);
        }
    }
}
