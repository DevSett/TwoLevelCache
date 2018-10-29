package cache;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class FileLevelCacheTest extends AbstractTest{

    @Before
    public void init() {
        keys = new ArrayList<>();
        values = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            keys.add("key"+i);
            values.add(BigDecimal.valueOf(i));
        }

        cache = new FileLevelCache<>(5);
    }


    @Test
    public void getPutRemove() {
        super.getPutRemove();
    }
}
