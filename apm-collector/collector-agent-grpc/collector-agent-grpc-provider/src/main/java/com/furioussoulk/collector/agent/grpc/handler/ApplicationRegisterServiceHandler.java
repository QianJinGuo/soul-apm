package com.furioussoulk.collector.agent.grpc.handler;

import com.furioussoulk.apm.collector.server.grpc.GRPCHandler;
import com.furioussoulk.network.proto.ApplicationRegisterServiceGrpc;
import com.google.protobuf.ProtocolStringList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationRegisterServiceHandler
        extends ApplicationRegisterServiceGrpc.ApplicationRegisterServiceImplBase
        implements GRPCHandler {

    private final Logger logger = LoggerFactory.getLogger(ApplicationRegisterServiceHandler.class);

    private final IApplicationIDService applicationIDService;

    public ApplicationRegisterServiceHandler(ModuleManager moduleManager) {
        applicationIDService = moduleManager.find(AgentStreamModule.NAME).getService(IApplicationIDService.class);
    }

    @Override public void register(Application request, StreamObserver<ApplicationMapping> responseObserver) {
        logger.debug("register application");
        ProtocolStringList applicationCodes = request.getApplicationCodeList();

        ApplicationMapping.Builder builder = ApplicationMapping.newBuilder();
        for (int i = 0; i < applicationCodes.size(); i++) {
            String applicationCode = applicationCodes.get(i);
            int applicationId = applicationIDService.getOrCreate(applicationCode);

            if (applicationId != 0) {
                KeyWithIntegerValue value = KeyWithIntegerValue.newBuilder().setKey(applicationCode).setValue(applicationId).build();
                builder.addApplication(value);
            }
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
