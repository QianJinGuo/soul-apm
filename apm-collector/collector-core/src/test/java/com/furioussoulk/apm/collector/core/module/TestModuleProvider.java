package com.furioussoulk.apm.collector.core.module;

import com.furioussoulk.apm.collector.core.exception.ServiceNotProvidedException;

import java.util.Properties;

public class TestModuleProvider extends ModuleProvider {
    @Override public String name() {
        return "TestModule-Provider";
    }

    @Override public Class<? extends Module> module() {
        return TestModule.class;
    }

    @Override public void prepare(Properties config) {

    }

    @Override public void start(Properties config) {

    }

    @Override public void notifyAfterCompleted() throws ServiceNotProvidedException {

    }

    @Override public String[] requiredModules() {
        return new String[] {"BaseA", "BaseB"};
    }
}
