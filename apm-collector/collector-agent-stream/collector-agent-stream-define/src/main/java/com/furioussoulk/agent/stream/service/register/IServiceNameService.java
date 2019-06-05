package com.furioussoulk.agent.stream.service.register;

import com.furioussoulk.apm.collector.core.module.Service;

public interface IServiceNameService extends Service {
    int getOrCreate(int applicationId, String serviceName);
}
