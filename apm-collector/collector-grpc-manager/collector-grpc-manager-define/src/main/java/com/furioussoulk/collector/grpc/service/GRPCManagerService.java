package com.furioussoulk.collector.grpc.service;

import com.furioussoulk.apm.collector.core.module.Service;
import com.furioussoulk.apm.collector.server.Server;

public interface GRPCManagerService extends Service {
    Server createIfAbsent(String host, int port);
}
