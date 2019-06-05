package com.furioussoulk.agent.stream.service.register;

import com.furioussoulk.apm.collector.core.module.Service;

public interface IApplicationIDService extends Service {
    int getOrCreate(String applicationCode);
}
