package com.furioussoulk.apm.collector.core.module;

import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;

public class ApplicationConfigurationTest {
    @Test
    public void testBuildConfig() {
        ApplicationConfiguration configuration = new ApplicationConfiguration();
        Properties p1 = new Properties();
        p1.setProperty("p1", "value1");
        p1.setProperty("p2", "value2");
        Properties p2 = new Properties();
        p2.setProperty("prop1", "value1-prop");
        p2.setProperty("prop2", "value2-prop");
        configuration.addModule("MO-1").addProviderConfiguration("MO-1-P1", p1).addProviderConfiguration("MO-1-P2", p2);

        Assert.assertArrayEquals(new String[] {"MO-1"}, configuration.moduleList());
        Assert.assertEquals("value2-prop", configuration.getModuleConfiguration("MO-1").getProviderConfiguration("MO-1-P2").getProperty("prop2"));
        Assert.assertEquals(p1, configuration.getModuleConfiguration("MO-1").getProviderConfiguration("MO-1-P1"));
    }
}
