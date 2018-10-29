package cache.strategy;

import cache.FileLevelCache;
import cache.MemLevelCache;
import cache.enums.LevelType;

import java.util.Map;

public class MRUStrategy<K> extends StrategyService<K> {


    public MRUStrategy(Integer memorySize, Integer fileMemSize) {
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
            if ((long) ((Map.Entry) entry).getValue() <= value) {
                key = (K) ((Map.Entry) entry).getKey();
                remove(key);
                return key;
            }
        }

        return null;
    }

    public void caching(Map<K, Long> memoryStorage, Map<K, Long> fileStorage, FileLevelCache fileLevelCache, MemLevelCache memLevelCache) {
        for (Map.Entry<K, Long> memEntry : memoryStorage.entrySet()) {
            for (Map.Entry<K, Long> fileEntry : fileStorage.entrySet()) {
                if (memEntry.getValue() > fileEntry.getValue()) {
                    replaceFileInMem(memoryStorage, fileStorage, fileLevelCache, memLevelCache, memEntry, fileEntry);
                    caching(memoryStorage, fileStorage, fileLevelCache, memLevelCache);
                    return;
                }
            }
        }
        setMemoryStorage(memoryStorage);
        setFileStorage(fileStorage);
    }
}
