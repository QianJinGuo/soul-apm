package com.furioussoulk.collector.grpc.manager;

import com.furioussoulk.apm.collector.core.module.Module;
import com.furioussoulk.collector.grpc.service.GRPCManagerService;

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
