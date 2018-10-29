package cache.strategy;

import cache.FileLevelCache;
import cache.MemLevelCache;
import cache.enums.LevelType;

import java.util.Map;


public class LRUStrategy<K> extends StrategyService<K> {


    public LRUStrategy(Integer memorySize, Integer fileMemSize) {
        super(memorySize, fileMemSize);
    }

    @Override
    public LevelType put(K key) {
        long count = System.nanoTime();

        if (getMemoryStorage().containsKey(key)) {
            super.getMemoryStorage().put(key, count);
            return LevelType.Memory;
        }
        if (getFileStorage().containsKey(key)) {
            super.getFileStorage().put(key, count);
            return LevelType.File;
        }

        return super.put(key, count);
    }

    @Override
    public K clear() {
        long value = System.nanoTime();
        K key = null;

        for (Object entry : getFileStorage().entrySet()) {
            if ((long) ((Map.Entry) entry).getValue() < value) {
                value = (long) ((Map.Entry) entry).getValue();
                key = (K) ((Map.Entry) entry).getKey();
            }
        }

        remove(key);
        return key;
    }





}
