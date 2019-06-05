package com.furioussoulk.apm.collector.core.define;

import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;

public class DefinitionLoaderTest {
    @Test
    public void testLoad() {
        TestDefineFile define = new TestDefineFile();
        DefinitionLoader<ServiceInterface> definitionLoader = new DefinitionLoader<>(ServiceInterface.class, define);
        Iterator<ServiceInterface> iterator = definitionLoader.iterator();
        if (iterator.hasNext()) {
            ServiceInterface service = iterator.next();
            Assert.assertTrue(service instanceof ServiceInterface);
            Assert.assertTrue(service instanceof ServiceImpl);
        }
    }
}
