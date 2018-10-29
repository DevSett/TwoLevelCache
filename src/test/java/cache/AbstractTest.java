package cache;

import cache.interfaces.Cache;
import jdk.nashorn.internal.objects.annotations.Getter;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public abstract class AbstractTest {

    protected Cache<String, BigDecimal> cache;

    protected List<String> keys;
    protected List<BigDecimal> values;


    public void getPutRemove() {
        cache.put(keys.get(0),values.get(0));
        Assert.assertEquals(values.get(0), cache.get(keys.get(0)));

        cache.remove(keys.get(0));
        Assert.assertNull(cache.get(keys.get(0)));
    }

}
