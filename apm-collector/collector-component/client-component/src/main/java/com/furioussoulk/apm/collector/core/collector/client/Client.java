package com.furioussoulk.apm.collector.core.collector.client;

public interface Client {
    void initialize() throws ClientException;

    void shutdown();
}
