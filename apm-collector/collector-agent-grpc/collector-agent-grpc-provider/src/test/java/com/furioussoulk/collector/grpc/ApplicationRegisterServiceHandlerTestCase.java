package com.furioussoulk.collector.grpc;

import com.furioussoulk.network.proto.Application;
import com.furioussoulk.network.proto.ApplicationMapping;
import com.furioussoulk.network.proto.ApplicationRegisterServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationRegisterServiceHandlerTestCase {

    private final Logger logger = LoggerFactory.getLogger(ApplicationRegisterServiceHandlerTestCase.class);

    private ApplicationRegisterServiceGrpc.ApplicationRegisterServiceBlockingStub stub;

    @Test
    public void testRegister() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 11800).usePlaintext(true).build();
        stub = ApplicationRegisterServiceGrpc.newBlockingStub(channel);

        Application application = Application.newBuilder().addApplicationCode("test141").build();
        ApplicationMapping mapping = stub.register(application);
        logger.debug(mapping.getApplication(0).getKey() + ", " + mapping.getApplication(0).getValue());
    }
}
