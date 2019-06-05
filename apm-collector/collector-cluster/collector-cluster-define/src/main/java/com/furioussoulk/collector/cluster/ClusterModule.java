package com.furioussoulk.collector.cluster;

import com.furioussoulk.apm.collector.core.module.Module;
import com.furioussoulk.apm.collector.core.module.Service;
import com.furioussoulk.collector.cluster.service.ModuleListenerService;
import com.furioussoulk.collector.cluster.service.ModuleRegisterService;

public class ClusterModule extends Module {

    public static final String NAME = "cluster";

    @Override public String name() {
        return NAME;
    }

    @Override public Class<? extends Service>[] services() {
        return new Class[] {ModuleListenerService.class, ModuleRegisterService.class};
    }
}
