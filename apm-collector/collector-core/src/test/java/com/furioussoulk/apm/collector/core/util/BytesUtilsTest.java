
package com.furioussoulk.apm.collector.core.util;

import org.junit.Assert;
import org.junit.Test;

public class BytesUtilsTest {
    @Test
    public void testLong2Bytes() {
        Assert.assertEquals(655390L, BytesUtils.bytes2Long(BytesUtils.long2Bytes(655390L)));
    }
}
