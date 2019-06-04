package com.furioussoulk.apm.collector.client;

public interface Client {
    void initialize() throws ClientException;

    void shutdown();
}
