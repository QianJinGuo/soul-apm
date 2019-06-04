package com.furioussoulk.client;

public interface Client {
    void initialize() throws ClientException;

    void shutdown();
}
