package com.furioussoulk.apm.collector.core.module;

import com.furioussoulk.apm.collector.core.exception.DuplicateProviderException;
import com.furioussoulk.apm.collector.core.exception.ModuleNotFoundException;
import com.furioussoulk.apm.collector.core.exception.ProviderNotFoundException;
import com.furioussoulk.apm.collector.core.exception.ServiceNotProvidedException;
import org.junit.Assert;
import org.junit.Test;

public class ModuleManagerTest {
    @Test
    public void testInit() throws ServiceNotProvidedException, ModuleNotFoundException, ProviderNotFoundException, DuplicateProviderException {
        ApplicationConfiguration configuration = new ApplicationConfiguration();
        configuration.addModule("Test").addProviderConfiguration("TestModule-Provider", null);
        configuration.addModule("BaseA").addProviderConfiguration("P-A", null);
        configuration.addModule("BaseB").addProviderConfiguration("P-B", null);

        ModuleManager manager = new ModuleManager();
        manager.init(configuration);

        BaseModuleA.ServiceABusiness1 serviceABusiness1 = manager.find("BaseA").provider().getService(BaseModuleA.ServiceABusiness1.class);
        Assert.assertTrue(serviceABusiness1 != null);
    }
}
