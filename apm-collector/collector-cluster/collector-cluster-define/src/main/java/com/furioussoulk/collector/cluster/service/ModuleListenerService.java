package com.furioussoulk.collector.cluster.service;

import com.furioussoulk.apm.collector.core.module.Service;
import com.furioussoulk.collector.cluster.ClusterModuleListener;

public interface ModuleListenerService extends Service {
    void addListener(ClusterModuleListener listener);
}
