package com.furioussoulk.collector.cluster.standalone.service;

import com.furioussoulk.collector.cluster.ClusterModuleListener;
import com.furioussoulk.collector.cluster.service.ModuleListenerService;
import com.furioussoulk.collector.cluster.standalone.ClusterStandaloneDataMonitor;

public class StandaloneModuleListenerService implements ModuleListenerService {

    private final ClusterStandaloneDataMonitor dataMonitor;

    public StandaloneModuleListenerService(ClusterStandaloneDataMonitor dataMonitor) {
        this.dataMonitor = dataMonitor;
    }

    @Override public void addListener(ClusterModuleListener listener) {
        dataMonitor.addListener(listener);
    }
}
