package com.furioussoulk.collector.cluster.standalone;

import com.furioussoulk.apm.collector.client.Client;
import com.furioussoulk.apm.collector.client.ClientException;
import com.furioussoulk.apm.collector.core.exception.CollectorException;
import com.furioussoulk.collector.cluster.ClusterModuleListener;
import com.furioussoulk.collector.cluster.DataMonitor;
import com.furioussoulk.collector.cluster.ModuleRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClusterStandaloneDataMonitor implements DataMonitor {

    private final Logger logger = LoggerFactory.getLogger(ClusterStandaloneDataMonitor.class);

//    private H2Client client;

    private Map<String, ClusterModuleListener> listeners;
    private Map<String, ModuleRegistration> registrations;

    public ClusterStandaloneDataMonitor() {
        listeners = new LinkedHashMap<>();
        registrations = new LinkedHashMap<>();
    }

    @Override
    public void setClient(Client client) {
//        this.client = (H2Client)client;
    }

    @Override
    public void addListener(ClusterModuleListener listener) {
        String path = BASE_CATALOG + listener.path();
        logger.info("listener path: {}", path);
        listeners.put(path, listener);
    }

    @Override
    public ClusterModuleListener getListener(String path) {
        path = BASE_CATALOG + path;
        return listeners.get(path);
    }

    @Override
    public void register(String path, ModuleRegistration registration) {
        registrations.put(BASE_CATALOG + path, registration);
    }

    @Override
    public void createPath(String path) throws ClientException {

    }

    @Override
    public void setData(String path, String value) throws ClientException {
        if (listeners.containsKey(path)) {
            listeners.get(path).addAddress(value);
            listeners.get(path).serverJoinNotify(value);
        }
    }

    public void start() throws CollectorException {
        Iterator<Map.Entry<String, ModuleRegistration>> entryIterator = registrations.entrySet().iterator();
        while (entryIterator.hasNext()) {
            Map.Entry<String, ModuleRegistration> next = entryIterator.next();
            ModuleRegistration.Value value = next.getValue().buildValue();
            String contextPath = value.getContextPath() == null ? "" : value.getContextPath();
            setData(next.getKey(), value.getHostPort() + contextPath);
        }
    }
}
