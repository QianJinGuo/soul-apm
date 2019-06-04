package com.furioussoulk.grpc.manager.service;

import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.module.Service;
import com.furioussoulk.server.Server;

public interface GRPCManagerService extends Service {
    Server createIfAbsent(String host, int port);
}
