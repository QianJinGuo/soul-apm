package com.furioussoulk.apm.collector.core.module;

import com.furioussoulk.apm.collector.core.exception.ServiceNotProvidedException;

import java.util.Properties;

public class ModuleAProvider extends ModuleProvider {
    @Override public String name() {
        return "P-A";
    }

    @Override public Class<? extends Module> module() {
        return BaseModuleA.class;
    }

    @Override public void prepare(Properties config) throws ServiceNotProvidedException {
        this.registerServiceImplementation(BaseModuleA.ServiceABusiness1.class, new ModuleABusiness1Impl());
        this.registerServiceImplementation(BaseModuleA.ServiceABusiness2.class, new ModuleABusiness2Impl());
    }

    @Override public void start(Properties config) throws ServiceNotProvidedException {
    }

    @Override public void notifyAfterCompleted() throws ServiceNotProvidedException {

    }

    @Override public String[] requiredModules() {
        return new String[0];
    }
}
