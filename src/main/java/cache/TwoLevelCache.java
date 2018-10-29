package cache;


import cache.enums.CacheType;
import cache.enums.LevelType;
import cache.interfaces.Cache;
import cache.strategy.StrategyService;
import lombok.Getter;


@Getter
public class TwoLevelCache<K, V> implements Cache<K, V> {


    private MemLevelCache<K, V> memLevelCache;
    private FileLevelCache<K, V> fileLevelCache;
    private Integer memSize;
    private Integer fileSize;
    private StrategyService<K> strategyService;

    public TwoLevelCache(Integer memSize, Integer fileSize, CacheType cacheType) {
        this(memSize, fileSize, CacheType.get(cacheType, memSize, fileSize));
    }

    public TwoLevelCache(Integer memSize, Integer fileSize) {
        this(memSize, fileSize, CacheType.LFU);
    }

    public TwoLevelCache(Integer memSize, Integer fileSize, StrategyService strategyService) {
        this.memSize = memSize;
        this.fileSize = fileSize;
        this.strategyService = strategyService;
        memLevelCache = new MemLevelCache(memSize);
        fileLevelCache = new FileLevelCache(fileSize);
    }

    @Override
    public void put(K key, V value) {
        K deletedKey = strategyService.checkOvercrowding();

        if (deletedKey != null) {
            fileLevelCache.remove(deletedKey);
        }

        LevelType levelType = strategyService.put(key);

        if (levelType == LevelType.Memory) {
            memLevelCache.put(key, value);
        }

        if (levelType == LevelType.File) {
            fileLevelCache.put(key, value);
        }

        strategyService.caching(fileLevelCache, memLevelCache);
    }

    @Override
    public void remove(K key) {
        if (memLevelCache.containsKey(key)) {
            memLevelCache.remove(key);
        }
        if (fileLevelCache.containsKey(key)) {
            fileLevelCache.remove(key);
        }

        strategyService.remove(key);
    }

    @Override
    public V get(K key) {
        strategyService.put(key);
        strategyService.caching(fileLevelCache, memLevelCache);

        if (fileLevelCache.containsKey(key)) {
            return fileLevelCache.get(key);
        }

        if (memLevelCache.containsKey(key)) {
            return memLevelCache.get(key);
        }

        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return memLevelCache.containsKey(key) || fileLevelCache.containsKey(key);
    }

    @Override
    public boolean containsValue(V value) {
        return memLevelCache.containsValue(value) || fileLevelCache.containsValue(value);
    }

    @Override
    public void clear() {
        memLevelCache.clear();
        fileLevelCache.clear();
        strategyService.clear();
    }

    @Override
    public boolean isEmpty() {
        return memLevelCache.isEmpty() && fileLevelCache.isEmpty();
    }

    @Override
    public Integer size() {
        return memLevelCache.size() + fileLevelCache.size();
    }

    public LevelType getLevelCache(K key) {
        if (memLevelCache.containsKey(key)) return LevelType.Memory;
        if (fileLevelCache.containsKey(key)) return LevelType.File;
        return null;
    }


}
