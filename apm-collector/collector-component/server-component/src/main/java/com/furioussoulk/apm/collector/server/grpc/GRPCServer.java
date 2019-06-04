package com.furioussoulk.apm.collector.server.grpc;

import com.furioussoulk.apm.collector.server.Server;
import com.furioussoulk.apm.collector.server.ServerException;
import com.furioussoulk.apm.collector.server.ServerHandler;
import io.grpc.netty.NettyServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

public class GRPCServer implements Server {

    private final Logger logger = LoggerFactory.getLogger(GRPCServer.class);

    private final String host;
    private final int port;
    private io.grpc.Server server;
    private NettyServerBuilder nettyServerBuilder;

    public GRPCServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public String hostPort() {
        return host + ":" + port;
    }

    @Override
    public String serverClassify() {
        return "Google-RPC";
    }

    @Override
    public void initialize() throws ServerException {
        InetSocketAddress address = new InetSocketAddress(host, port);
        nettyServerBuilder = NettyServerBuilder.forAddress(address);
        logger.info("Server started, host {} listening on {}", host, port);
    }

    @Override
    public void start() throws ServerException {
        try {
            server = nettyServerBuilder.build();
            server.start();
        } catch (IOException e) {
            throw new GRPCServerException(e.getMessage(), e);
        }
    }

    @Override
    public void addHandler(ServerHandler handler) {
        nettyServerBuilder.addService((io.grpc.BindableService) handler);
    }
}
