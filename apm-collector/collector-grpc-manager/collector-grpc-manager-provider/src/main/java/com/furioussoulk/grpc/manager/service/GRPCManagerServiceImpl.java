package com.furioussoulk.grpc.manager.service;

import com.furioussoulk.server.Server;
import com.furioussoulk.server.ServerException;
import com.furioussoulk.server.grpc.GRPCServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class GRPCManagerServiceImpl implements GRPCManagerService {

    private final Logger logger = LoggerFactory.getLogger(GRPCManagerServiceImpl.class);

    private final Map<String, GRPCServer> servers;

    public GRPCManagerServiceImpl(Map<String, GRPCServer> servers) {
        this.servers = servers;
    }

    @Override
    public Server createIfAbsent(String host, int port) {
        String id = host + String.valueOf(port);
        if (servers.containsKey(id)) {
            return servers.get(id);
        } else {
            GRPCServer server = new GRPCServer(host, port);
            try {
                server.initialize();
            } catch (ServerException e) {
                logger.error(e.getMessage(), e);
            }
            servers.put(id, server);
            return server;
        }
    }
}
