package com.furioussoulk.collector.cluster.standalone;

import com.furioussoulk.apm.collector.core.exception.CollectorException;
import com.furioussoulk.apm.collector.core.exception.ServiceNotProvidedException;
import com.furioussoulk.apm.collector.core.exception.UnexpectedException;
import com.furioussoulk.apm.collector.core.module.Module;
import com.furioussoulk.apm.collector.core.module.ModuleProvider;
import com.furioussoulk.collector.cluster.ClusterModule;
import com.furioussoulk.collector.cluster.service.ModuleListenerService;
import com.furioussoulk.collector.cluster.service.ModuleRegisterService;
import com.furioussoulk.collector.cluster.standalone.service.StandaloneModuleListenerService;
import com.furioussoulk.collector.cluster.standalone.service.StandaloneModuleRegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ClusterModuleStandaloneProvider extends ModuleProvider {

    private final Logger logger = LoggerFactory.getLogger(ClusterModuleStandaloneProvider.class);

    private static final String URL = "url";
    private static final String USER_NAME = "user_name";

    //    private H2Client h2Client;
    private ClusterStandaloneDataMonitor dataMonitor;

    @Override
    public String name() {
        return "standalone";
    }

    @Override
    public Class<? extends Module> module() {
        return ClusterModule.class;
    }

    @Override
    public void prepare(Properties config) throws ServiceNotProvidedException {
        this.dataMonitor = new ClusterStandaloneDataMonitor();

        final String url = config.getProperty(URL);
        final String userName = config.getProperty(USER_NAME);
//        h2Client = new H2Client(url, userName, Const.EMPTY_STRING);
//        this.dataMonitor.setClient(h2Client);

        this.registerServiceImplementation(ModuleListenerService.class, new StandaloneModuleListenerService(dataMonitor));
        this.registerServiceImplementation(ModuleRegisterService.class, new StandaloneModuleRegisterService(dataMonitor));
    }

    @Override
    public void start(Properties config) throws ServiceNotProvidedException {
//        try {
//            h2Client.initialize();
//        } catch (H2ClientException e) {
//            logger.error(e.getMessage(), e);
//        }
    }

    @Override
    public void notifyAfterCompleted() throws ServiceNotProvidedException {
        try {
            dataMonitor.start();
        } catch (CollectorException e) {
            throw new UnexpectedException(e.getMessage());
        }
    }

    @Override
    public String[] requiredModules() {
        return new String[0];
    }
}
