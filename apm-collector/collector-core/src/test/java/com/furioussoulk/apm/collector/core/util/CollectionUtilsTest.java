package com.furioussoulk.apm.collector.core.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CollectionUtilsTest {
    @Test
    public void testList() {
        List<String> list = new LinkedList<>();
        Assert.assertTrue(CollectionUtils.isEmpty(list));
        Assert.assertFalse(CollectionUtils.isNotEmpty(list));
    }

    @Test
    public void testMap() {
        Map<String, String> map = new HashMap<>();
        Assert.assertTrue(CollectionUtils.isEmpty(map));
        Assert.assertFalse(CollectionUtils.isNotEmpty(map));
    }

    @Test
    public void testArray() {
        String[] array = new String[] {"abc"};
        Assert.assertTrue(CollectionUtils.isNotEmpty(array));
    }
}
