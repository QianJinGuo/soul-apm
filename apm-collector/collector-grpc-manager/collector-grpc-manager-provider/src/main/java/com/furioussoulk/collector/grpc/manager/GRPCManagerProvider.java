package com.furioussoulk.collector.grpc.manager;

import com.furioussoulk.apm.core.exception.ServiceNotProvidedException;
import com.furioussoulk.apm.collector.core.module.Module;
import com.furioussoulk.apm.collector.core.module.ModuleProvider;
import com.furioussoulk.collector.grpc.service.GRPCManagerServiceImpl;
import com.furioussoulk.collector.grpc.service.GRPCManagerService;
import com.furioussoulk.apm.collector.server.ServerException;
import com.furioussoulk.apm.collector.server.grpc.GRPCServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class GRPCManagerProvider extends ModuleProvider {

    private final Logger logger = LoggerFactory.getLogger(GRPCManagerProvider.class);

    private Map<String, GRPCServer> servers = new HashMap<>();

    @Override public String name() {
        return "gRPC";
    }

    @Override public Class<? extends Module> module() {
        return GRPCManagerModule.class;
    }

    @Override public void prepare(Properties config) throws ServiceNotProvidedException {
        this.registerServiceImplementation(GRPCManagerService.class, new GRPCManagerServiceImpl(servers));
    }

    @Override public void start(Properties config) throws ServiceNotProvidedException {

    }

    @Override public void notifyAfterCompleted() throws ServiceNotProvidedException {
        servers.values().forEach(server -> {
            try {
                server.start();
            } catch (ServerException e) {
                logger.error(e.getMessage(), e);
            }
        });
    }

    @Override public String[] requiredModules() {
        return new String[0];
    }
}
