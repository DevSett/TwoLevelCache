package cache.interfaces;

public interface Cache<K,V> {

    void put(K key, V value);
    void remove(K key);
    V get(K key);
    boolean containsKey(K key);
    boolean containsValue(V value);
    void clear();
    boolean isEmpty();
    Integer size();

}
