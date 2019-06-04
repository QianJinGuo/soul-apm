package com.furioussoulk.spi;

import com.furioussoulk.apm.core.boot.BootService;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Demo class
 *
 * @author 孙证杰
 * @email 200765821@qq.com
 * @date 2019/6/4 16:08
 */
public class TestSpi {

    @Test
    public void loadBootService(){
        ServiceLoader<BootService> load = ServiceLoader.load(BootService.class);
        Iterator<BootService> iterator = load.iterator();
        Assert.assertTrue(iterator.hasNext());
    }
}
