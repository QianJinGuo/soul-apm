package com.furioussoulk.apm.collector.core.module;

public class TestModule extends Module {
    @Override public String name() {
        return "Test";
    }

    @Override public Class<? extends Service>[] services() {
        return new Class[0];
    }
}
