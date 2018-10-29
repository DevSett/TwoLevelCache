package cache.strategy;

import cache.AbstractTest;
import cache.TwoLevelCache;
import cache.enums.CacheType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class LRUStrategyTest extends AbstractTest {
    @Before
    public void init() {
        keys = new ArrayList<>();
        values = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            keys.add("key" + i);
            values.add(BigDecimal.valueOf(i));
        }


        cache = new TwoLevelCache<>(2, 5,  CacheType.LRU);
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
