package roles;

import inventory.SheriffBag;

import java.util.HashMap;
import java.util.List;

/**
 * Interfata contine toate semnaturile metodelor de baza ce trebuie sa le aiba un serif.
 * @author Cezar Craciunoiu
 */
public interface Sheriff {

    void control(SheriffBag[] bag, List<Integer> pack, HashMap<String,
                 Integer> association, HashMap<Integer, Integer> penalty);

    void removeOneAsset(int asset);

}
