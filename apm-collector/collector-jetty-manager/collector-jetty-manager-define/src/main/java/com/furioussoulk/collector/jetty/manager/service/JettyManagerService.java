package com.furioussoulk.collector.jetty.manager.service;


import com.furioussoulk.apm.collector.core.module.Service;
import com.furioussoulk.apm.collector.server.Server;
import com.furioussoulk.apm.collector.server.ServerHandler;

public interface JettyManagerService extends Service {
    Server createIfAbsent(String host, int port, String contextPath);

    void addHandler(String host, int port, ServerHandler serverHandler);
}
