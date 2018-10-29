package cache.enums;

import cache.strategy.*;
import cache.strategy.StrategyService;

import java.util.Map;

public enum CacheType {

    LFU,
    LRU,
    MRU;

    public static StrategyService get(CacheType cacheType, int memSize, int fileSize) {
        switch (cacheType) {
            case LRU:
                return new LRUStrategy(memSize, fileSize);
            case MRU:
                return new MRUStrategy(memSize, fileSize);
            default:
                return new LFUStrategy(memSize, fileSize);
        }
    }


    public static CacheType parse(String arg) {
        switch (arg){
            case "LRU": return LRU;
            case "MRU":return MRU;
            default:return  LFU;

        }
    }
}
