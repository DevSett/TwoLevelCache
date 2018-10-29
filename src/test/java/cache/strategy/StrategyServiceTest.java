package cache.strategy;


import cache.AbstractTest;
import cache.TwoLevelCache;
import cache.enums.LevelType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.*;

public class StrategyServiceTest extends AbstractTest {

    @Before
    public void init() {
        keys = new ArrayList<>();
        values = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            keys.add("key" + i);
            values.add(BigDecimal.valueOf(i));
        }

        StrategyService<String> strategyService = new StrategyService<String>(2, 5) {

            public LevelType put(String key) {
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
            public String clear() {
                long value = Long.MAX_VALUE;
                String key = null;

                for (Object entry : getFileStorage().entrySet()) {
                    if ((long) ((Map.Entry) entry).getValue() < value) {
                        value = (long) ((Map.Entry) entry).getValue();
                        key = (String) ((Map.Entry) entry).getKey();
                    }
                }

                remove(key);
                return key;
            }
        };

        cache = new TwoLevelCache<>(2, 5, strategyService);
    }

    @Test
    public void getPutRemove() {
        for (int i = 0; i < keys.size(); i++) {
            cache.put(keys.get(i), values.get(i));
            if (i == 6) {
                cache.get(keys.get(0));
                cache.get(keys.get(1));
                cache.get(keys.get(2));
                cache.get(keys.get(3));
            }
        }

        for (int i = 0; i < 4; i++) {
            Assert.assertNotNull(cache.get(keys.get(i)));
        }
    }
}
