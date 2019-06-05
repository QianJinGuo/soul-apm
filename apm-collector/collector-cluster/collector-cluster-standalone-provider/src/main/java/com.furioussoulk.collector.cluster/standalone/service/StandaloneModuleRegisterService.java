package com.furioussoulk.collector.cluster.standalone.service;

import com.furioussoulk.collector.cluster.ModuleRegistration;
import com.furioussoulk.collector.cluster.service.ModuleRegisterService;
import com.furioussoulk.collector.cluster.standalone.ClusterStandaloneDataMonitor;

public class StandaloneModuleRegisterService implements ModuleRegisterService {

    private final ClusterStandaloneDataMonitor dataMonitor;

    public StandaloneModuleRegisterService(ClusterStandaloneDataMonitor dataMonitor) {
        this.dataMonitor = dataMonitor;
    }

    @Override public void register(String moduleName, String providerName, ModuleRegistration registration) {
        String path = "/" + moduleName + "/" + providerName;
        dataMonitor.register(path, registration);
    }
}
