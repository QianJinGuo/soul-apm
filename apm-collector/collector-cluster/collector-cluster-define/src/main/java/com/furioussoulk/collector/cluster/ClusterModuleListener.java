package com.furioussoulk.collector.cluster;

import java.util.HashSet;
import java.util.Set;

public abstract class ClusterModuleListener {

    private Set<String> addresses;

    public ClusterModuleListener() {
        addresses = new HashSet<>();
    }

    public abstract String path();

    public final void addAddress(String address) {
        addresses.add(address);
    }

    public final void removeAddress(String address) {
        addresses.remove(address);
    }

    public final Set<String> getAddresses() {
        return addresses;
    }

    public abstract void serverJoinNotify(String serverAddress);

    public abstract void serverQuitNotify(String serverAddress);
}
