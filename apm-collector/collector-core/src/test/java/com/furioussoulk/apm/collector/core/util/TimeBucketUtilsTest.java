package com.furioussoulk.apm.collector.core.util;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.TimeZone;

public class TimeBucketUtilsTest {
    @Before
    public void setup() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }

    @After
    public void teardown() {

    }

    @Test
    public void testGetInfoFromATimestamp() {
        long timeMillis = 1509521745220L;
        Assert.assertArrayEquals(new long[] {
            20171101153545L,
            20171101153544L,
            20171101153543L,
            20171101153542L,
            20171101153541L
        }, TimeBucketUtils.INSTANCE.getFiveSecondTimeBuckets(TimeBucketUtils.INSTANCE.getSecondTimeBucket(timeMillis)));
        Assert.assertEquals(20171101153545L, TimeBucketUtils.INSTANCE.getSecondTimeBucket(timeMillis));
        Assert.assertEquals(201711011535L, TimeBucketUtils.INSTANCE.getMinuteTimeBucket(timeMillis));
        Assert.assertEquals(201711011500L, TimeBucketUtils.INSTANCE.getHourTimeBucket(timeMillis));
        Assert.assertEquals(201711010000L, TimeBucketUtils.INSTANCE.getDayTimeBucket(timeMillis));
    }
}
