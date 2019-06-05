package com.furioussoulk.collector.cluster.service;

import com.furioussoulk.apm.collector.core.module.Service;
import com.furioussoulk.collector.cluster.ModuleRegistration;

public interface ModuleRegisterService extends Service {
    void register(String moduleName, String providerName, ModuleRegistration registration);
}
