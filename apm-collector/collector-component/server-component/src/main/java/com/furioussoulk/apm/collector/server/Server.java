package com.furioussoulk.apm.collector.server;

public interface Server {

    String hostPort();

    String serverClassify();

    void initialize() throws ServerException;

    void start() throws ServerException;

    void addHandler(ServerHandler handler);
}
