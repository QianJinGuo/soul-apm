package com.furioussoulk.grpc.manager;


import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.module.Module;
import com.furioussoulk.grpc.manager.service.GRPCManagerService;

public class GRPCManagerModule extends Module {

    public static final String NAME = "gRPC_manager";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public Class[] services() {
        return new Class[]{GRPCManagerService.class};
    }
}
