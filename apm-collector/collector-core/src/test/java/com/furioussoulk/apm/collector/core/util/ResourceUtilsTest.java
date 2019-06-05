package com.furioussoulk.apm.collector.core.util;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class ResourceUtilsTest {
    @Test
    public void testGetResource() throws IOException {
        Reader read = ResourceUtils.read("TestResourceFile.txt");
        try {
            BufferedReader reader = new BufferedReader(read);
            String line1 = reader.readLine();
            Assert.assertEquals("soul-apm", line1);
        } finally {
            read.close();
        }
    }
}
