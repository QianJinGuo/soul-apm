package com.furioussoulk.apm.core.dictionary;

import com.furioussoulk.network.proto.Application;
import com.furioussoulk.network.proto.ApplicationMapping;
import com.furioussoulk.network.proto.ApplicationRegisterServiceGrpc;
import com.furioussoulk.network.proto.KeyWithIntegerValue;
import io.netty.util.internal.ConcurrentSet;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.furioussoulk.apm.core.conf.Config.Dictionary.APPLICATION_CODE_BUFFER_SIZE;


/**
 * Map of application id to application code, which is from the collector side.
 *
 */
public enum ApplicationDictionary {
    /**
     * instance
     */
    INSTANCE;
    private Map<String, Integer> applicationDictionary = new ConcurrentHashMap<String, Integer>();
    private Set<String> unRegisterApplications = new ConcurrentSet<String>();

    public PossibleFound find(String applicationCode) {
        Integer applicationId = applicationDictionary.get(applicationCode);
        if (applicationId != null) {
            return new Found(applicationId);
        } else {
            if (applicationDictionary.size() + unRegisterApplications.size() < APPLICATION_CODE_BUFFER_SIZE) {
                unRegisterApplications.add(applicationCode);
            }
            return new NotFound();
        }
    }

    public void syncRemoteDictionary(
        ApplicationRegisterServiceGrpc.ApplicationRegisterServiceBlockingStub applicationRegisterServiceBlockingStub) {
        if (unRegisterApplications.size() > 0) {
            ApplicationMapping applicationMapping = applicationRegisterServiceBlockingStub.register(
                Application.newBuilder().addAllApplicationCode(unRegisterApplications).build());
            if (applicationMapping.getApplicationCount() > 0) {
                for (KeyWithIntegerValue keyWithIntegerValue : applicationMapping.getApplicationList()) {
                    unRegisterApplications.remove(keyWithIntegerValue.getKey());
                    applicationDictionary.put(keyWithIntegerValue.getKey(), keyWithIntegerValue.getValue());
                }
            }
        }
    }
}
