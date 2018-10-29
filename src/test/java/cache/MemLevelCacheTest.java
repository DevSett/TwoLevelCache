package cache;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class MemLevelCacheTest extends AbstractTest{

    @Before
    public void init() {
        keys = new ArrayList<>();
        values = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            keys.add("key"+i);
            values.add(BigDecimal.valueOf(i));
        }

       cache = new MemLevelCache<>(5);
    }


    @Test
    public void getPutRemove() {
      super.getPutRemove();
    }

}
