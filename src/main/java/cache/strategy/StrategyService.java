package cache.strategy;

import cache.FileLevelCache;
import cache.MemLevelCache;
import cache.enums.LevelType;
import cache.interfaces.Strategy;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public abstract class StrategyService<K> implements Strategy<K> {

    private Map<K, Long> memoryStorage;
    private Map<K, Long> fileStorage;
    private Integer memorySize;
    private Integer fileMemSize;

    public StrategyService(Integer memorySize, Integer fileMemSize) {
        memoryStorage = new ConcurrentHashMap<>();
        fileStorage = new ConcurrentHashMap<>();
        this.memorySize = memorySize;
        this.fileMemSize = fileMemSize;
    }

    public LevelType put(K key, Long value) {
        if (getMemoryStorage().size() >= getMemorySize()) {
            getFileStorage().put(key, value);
            return LevelType.File;
        } else {
            getMemoryStorage().put(key, value);
            return LevelType.Memory;
        }
    }

    public void clearAll() {
        getFileStorage().clear();
        getMemoryStorage().clear();
    }


    public void remove(K key) {
        if (getMemoryStorage().containsKey(key)) {
            getMemoryStorage().remove(key);
        }
        if (getFileStorage().containsKey(key)) {
            getFileStorage().remove(key);
        }
    }


    public Long get(K key) {
        if (getFileStorage().containsKey(key)) {
            return (Long) getFileStorage().get(key);
        }
        if (getMemoryStorage().containsKey(key)) {
            return (Long) getMemoryStorage().get(key);
        }

        return -1l;
    }

    abstract public LevelType put(K key);

    abstract public K clear();

    public void caching(FileLevelCache fileLevelCache, MemLevelCache memLevelCache) {
        caching(getMemoryStorage(), getFileStorage(), fileLevelCache, memLevelCache);
    }


    public void caching(Map<K, Long> memoryStorage, Map<K, Long> fileStorage, FileLevelCache fileLevelCache, MemLevelCache memLevelCache) {
        for (Map.Entry<K, Long> memEntry : memoryStorage.entrySet()) {
            for (Map.Entry<K, Long> fileEntry : fileStorage.entrySet()) {
                if (memEntry.getValue() < fileEntry.getValue()) {
                    replaceFileInMem(memoryStorage, fileStorage, fileLevelCache, memLevelCache, memEntry, fileEntry);
                    caching(memoryStorage,fileStorage,fileLevelCache,memLevelCache);
                    return;
                }
            }
        }
        setMemoryStorage(memoryStorage);
        setFileStorage(fileStorage);
    }

    protected void replaceFileInMem(Map<K, Long> memoryStorage, Map<K, Long> fileStorage, FileLevelCache fileLevelCache,
                                    MemLevelCache memLevelCache, Map.Entry<K, Long> memEntry, Map.Entry<K, Long> fileEntry) {
        memoryStorage.put(fileEntry.getKey(), fileEntry.getValue());
        memLevelCache.put(fileEntry.getKey(), fileLevelCache.get(fileEntry.getKey()));
        fileLevelCache.remove(fileEntry.getKey());
        fileStorage.remove(fileEntry.getKey());

        fileStorage.put(memEntry.getKey(), memEntry.getValue());
        fileLevelCache.put(memEntry.getKey(), memLevelCache.get(memEntry.getKey()));
        memLevelCache.remove(memEntry.getKey());
        memoryStorage.remove(memEntry.getKey());
    }


    @Override
    public K checkOvercrowding() {
        if (getMemoryStorage().size() >= getMemorySize() && getFileStorage().size() >= getFileMemSize()) {
            return clear();
        }
        return null;
    }
}
