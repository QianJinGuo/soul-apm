package com.furioussoulk.apm.collector.core.module;

import com.furioussoulk.apm.collector.core.exception.ServiceNotProvidedException;

import java.util.Properties;

public class ModuleBProvider extends ModuleProvider {
    @Override public String name() {
        return "P-B";
    }

    @Override public Class<? extends Module> module() {
        return BaseModuleB.class;
    }

    @Override public void prepare(Properties config) throws ServiceNotProvidedException {
        this.registerServiceImplementation(BaseModuleB.ServiceBBusiness1.class, new ModuleBBusiness1Impl());
        this.registerServiceImplementation(BaseModuleB.ServiceBBusiness2.class, new ModuleBBusiness2Impl());
    }

    @Override public void start(Properties config) throws ServiceNotProvidedException {
    }

    @Override public void notifyAfterCompleted() throws ServiceNotProvidedException {

    }

    @Override public String[] requiredModules() {
        return new String[0];
    }
}
