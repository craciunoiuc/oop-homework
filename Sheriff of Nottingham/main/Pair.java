package main;

/**
 * Clasa Pair simuleaza clasa Pair din JavaFx deoarece aceasta nu poate fi importata in platforma
 * Vmchecker, dar, de asemenea, pentru a usura executia programului, aceasta clasa avand doar
 * lucrurile strict necesare.
 * @author Cezar Craciunoiu
 */
public final class Pair<K, V> {
    private K key;
    private V value;

    public Pair(final K key, final V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
