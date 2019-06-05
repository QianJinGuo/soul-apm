package com.furioussoulk.collector.cluster;

import com.furioussoulk.apm.collector.client.Client;
import com.furioussoulk.apm.collector.client.ClientException;

public interface DataMonitor {
    String BASE_CATALOG = "/soul-apm";

    void setClient(Client client);

    void addListener(ClusterModuleListener listener) throws ClientException;

    void register(String path, ModuleRegistration registration) throws ClientException;

    ClusterModuleListener getListener(String path);

    void createPath(String path) throws ClientException;

    void setData(String path, String value) throws ClientException;
}
