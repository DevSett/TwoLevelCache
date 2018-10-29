package cache.interfaces;

import cache.enums.LevelType;

public interface Strategy<K>{

    LevelType put(K key);

    void remove(K key);

    K clear();

    void clearAll();

    Long get(K key);

    K checkOvercrowding();


}
