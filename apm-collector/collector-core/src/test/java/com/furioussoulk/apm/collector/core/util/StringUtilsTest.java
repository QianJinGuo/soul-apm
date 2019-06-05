package com.furioussoulk.apm.collector.core.util;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest {
    @Test
    public void testEmptyString() {
        Assert.assertTrue(StringUtils.isEmpty(null));
        Assert.assertTrue(StringUtils.isEmpty(""));

        Assert.assertTrue(StringUtils.isNotEmpty("abc"));
    }
}
