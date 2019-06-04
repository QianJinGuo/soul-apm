package com.furioussoulk.grpc.manager;

import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.exception.ServiceNotProvidedException;
import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.module.Module;
import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.module.ModuleProvider;
import com.furioussoulk.grpc.manager.service.GRPCManagerService;
import com.furioussoulk.grpc.manager.service.GRPCManagerServiceImpl;
import com.furioussoulk.server.ServerException;
import com.furioussoulk.server.grpc.GRPCServer;
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
