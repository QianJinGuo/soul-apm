package com.furioussoulk.collector.boot;

import com.furioussoulk.apm.collector.core.exception.ModuleNotFoundException;
import com.furioussoulk.apm.collector.core.exception.ProviderNotFoundException;
import com.furioussoulk.apm.collector.core.exception.ServiceNotProvidedException;
import com.furioussoulk.apm.collector.core.module.ApplicationConfiguration;
import com.furioussoulk.apm.collector.core.module.ModuleManager;
import com.furioussoulk.collector.boot.config.ApplicationConfigLoader;
import com.furioussoulk.collector.boot.config.ConfigFileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CollectorBootStartUp {

    public static void main(String[] args) {
        final Logger logger = LoggerFactory.getLogger(CollectorBootStartUp.class);

        ApplicationConfigLoader configLoader = new ApplicationConfigLoader();
        ModuleManager manager = new ModuleManager();
        try {
            ApplicationConfiguration applicationConfiguration = configLoader.load();
            manager.init(applicationConfiguration);
        } catch (ConfigFileNotFoundException | ModuleNotFoundException | ProviderNotFoundException | ServiceNotProvidedException e) {
            logger.error(e.getMessage(), e);
        }

        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
        }
    }
}
