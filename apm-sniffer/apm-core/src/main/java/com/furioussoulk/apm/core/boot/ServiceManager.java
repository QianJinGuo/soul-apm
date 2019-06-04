package com.furioussoulk.apm.core.boot;

import com.furioussoulk.apm.core.logger.LogManager;
import com.furioussoulk.apm.core.logger.api.ILogger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

public enum ServiceManager {
    /**
     * instance
     */
    INSTANCE;

    private static final ILogger logger = LogManager.getLogger(ServiceManager.class);
    private Map<Class, BootService> bootedServices = new HashMap<Class, BootService>();

    public void boot() {
        bootedServices = loadAllServices();

        beforeBoot();
        startup();
        afterBoot();
    }


    private Map<Class, BootService> loadAllServices() {
        HashMap<Class, BootService> bootedServices = new HashMap<Class, BootService>();
        Iterator<BootService> serviceIterator = load().iterator();
        while (serviceIterator.hasNext()) {
            BootService bootService = serviceIterator.next();
            bootedServices.put(bootService.getClass(), bootService);
        }
        return bootedServices;
    }

    private void beforeBoot() {
        for (BootService service : bootedServices.values()) {
            try {
                service.beforeBoot();
            } catch (Throwable e) {
                logger.error(e, "ServiceManager try to pre-start [{}] fail.", service.getClass().getName());
            }
        }
    }

    private void startup() {
        for (BootService service : bootedServices.values()) {
            try {
                service.boot();
            } catch (Throwable e) {
                logger.error(e, "ServiceManager try to start [{}] fail.", service.getClass().getName());
            }
        }
    }

    private void afterBoot() {
        for (BootService service : bootedServices.values()) {
            try {
                service.afterBoot();
            } catch (Throwable e) {
                logger.error(e, "Service [{}] AfterBoot process fails.", service.getClass().getName());
            }
        }
    }

    private ServiceLoader<BootService> load() {
        return ServiceLoader.load(BootService.class);
    }
}
