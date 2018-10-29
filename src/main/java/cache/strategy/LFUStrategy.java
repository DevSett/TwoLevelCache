package cache.strategy;

import cache.FileLevelCache;
import cache.MemLevelCache;
import cache.enums.LevelType;

import java.util.HashMap;
import java.util.Map;

public class LFUStrategy<K> extends StrategyService<K> {

    public LFUStrategy(Integer memorySize, Integer fileMemSize) {
        super(memorySize, fileMemSize);
    }

    public LevelType put(K key) {
        long count = 0;

        if (getMemoryStorage().containsKey(key)) {
            count = (Long) getMemoryStorage().get(key) + 1;
            super.getMemoryStorage().put(key, count);
            return LevelType.Memory;
        }
        if (getFileStorage().containsKey(key)) {
            count = (Long) getFileStorage().get(key) + 1;
            super.getFileStorage().put(key, count);
            return LevelType.File;
        }

        return super.put(key, count);
    }

    @Override
    public K clear() {
        long value = Long.MAX_VALUE;
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
