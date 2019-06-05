package com.furioussoulk.collector.jetty.manager;

import com.furioussoulk.apm.collector.core.module.Module;
import com.furioussoulk.collector.jetty.manager.service.JettyManagerService;

public class JettyManagerModule extends Module {

    public static final String NAME = "jetty_manager";

    @Override public String name() {
        return NAME;
    }

    @Override public Class[] services() {
        return new Class[] {JettyManagerService.class};
    }
}
