package roles;

import inventory.Bag;
import main.Pair;

import java.util.List;

/**
 * Interfata se foloseste de cele 2 interfete Merchant si Sheriff pentru a a forma un "schelet" de
 * semnaturi obligatorii pentru orice jucator ce se doreste implementat.
 * @author Cezar Craciunoiu
 */
public interface Player extends Merchant, Sheriff {

    void addToPack(Pair asset, List<Integer> pack);

    void fillHand(Integer[] cards, int maxCards);

    String getName();

    void setName(String name);

    Bag getInventory();
}
