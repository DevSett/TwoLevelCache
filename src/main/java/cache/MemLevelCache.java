package cache;

import cache.interfaces.Cache;
import lombok.Getter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class MemLevelCache<K,V> implements Cache<K,V> {

    private Integer maxSize;
    private Map<K,V> storage;

    public MemLevelCache(Integer maxSize) {
        this.maxSize = maxSize;
        this.storage = new ConcurrentHashMap<>();
    }


    @Override
    public void put(K key, V value) {
        storage.put(key,value);
    }

    @Override
    public void remove(K key) {
        storage.remove(key);
    }

    @Override
    public V get(K key) {
        return storage.get(key);
    }

    @Override
    public boolean containsKey(K key){
        return storage.containsKey(key);
    }

    @Override
    public boolean containsValue(V value) {
        return storage.containsValue(value);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public boolean isEmpty() {
        return storage.isEmpty();
    }

    @Override
    public Integer size() {
        return storage.size();
    }
}
