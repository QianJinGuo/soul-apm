package com.furioussoulk.apm.collector.core.util;

import org.junit.Assert;
import org.junit.Test;

public class ObjectUtilsTest {
    @Test
    public void testNullObject() {
        Object o = new Object();
        Assert.assertTrue(ObjectUtils.isNotEmpty(o));
    }
}
