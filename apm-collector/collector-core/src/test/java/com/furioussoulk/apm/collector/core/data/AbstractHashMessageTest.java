package com.furioussoulk.apm.collector.core.data;

import org.junit.Assert;
import org.junit.Test;

public class AbstractHashMessageTest {
    public class NewMessage extends AbstractHashMessage {
        public NewMessage() {
            super("key");
        }
    }

    @Test
    public void testHash() {
        NewMessage message = new NewMessage();
        Assert.assertEquals("key".hashCode(), message.getHashCode());
    }
}
