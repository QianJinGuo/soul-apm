package com.furioussoulk.collector.jetty.manager.service;

import com.furioussoulk.apm.collector.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JettyManagerServiceImpl implements JettyManagerService {

    private final Logger logger = LoggerFactory.getLogger(JettyManagerServiceImpl.class);

    private final Map<String, JettyServer> servers;

    public JettyManagerServiceImpl(Map<String, JettyServer> servers) {
        this.servers = servers;
    }

    @Override public Server createIfAbsent(String host, int port, String contextPath) {
        String id = host + String.valueOf(port);
        if (servers.containsKey(id)) {
            return servers.get(id);
        } else {
            JettyServer server = new JettyServer(host, port, contextPath);
            try {
                server.initialize();
            } catch (ServerException e) {
                logger.error(e.getMessage(), e);
            }
            servers.put(id, server);
            return server;
        }
    }

    @Override public void addHandler(String host, int port, ServerHandler serverHandler) {
        String id = host + String.valueOf(port);
        if (servers.containsKey(id)) {
            servers.get(id).addHandler(serverHandler);
        } else {
            throw new UnexpectedException("Please create server before add server handler.");
        }
    }
}
