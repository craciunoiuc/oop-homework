package roles;

import java.util.HashMap;

/**
 * Interfata contine semnaturile metodelor ce sunt strict necesare unui negustor.
 * @author Cezar Craciunoiu
 */
public interface Merchant {

    void putInBag(int maxCards, HashMap<String, Integer> association);
}
