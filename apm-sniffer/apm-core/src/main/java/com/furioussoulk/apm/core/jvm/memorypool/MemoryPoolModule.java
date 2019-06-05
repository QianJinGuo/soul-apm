package com.furioussoulk.apm.core.jvm.memorypool;

import com.furioussoulk.network.proto.MemoryPool;
import com.furioussoulk.network.proto.PoolType;

import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.LinkedList;
import java.util.List;

public abstract class MemoryPoolModule implements MemoryPoolMetricAccessor {
    private List<MemoryPoolMXBean> beans;

    public MemoryPoolModule(List<MemoryPoolMXBean> beans) {
        this.beans = beans;
    }

    @Override
    public List<MemoryPool> getMemoryPoolMetricList() {
        List<MemoryPool> poolList = new LinkedList<MemoryPool>();
        for (MemoryPoolMXBean bean : beans) {
            String name = bean.getName();
            PoolType type;
            if (contains(getCodeCacheNames(), name)) {
                type = PoolType.CODE_CACHE_USAGE;
            } else if (contains(getEdenNames(), name)) {
                type = PoolType.NEWGEN_USAGE;
            } else if (contains(getOldNames(), name)) {
                type = PoolType.OLDGEN_USAGE;
            } else if (contains(getSurvivorNames(), name)) {
                type = PoolType.SURVIVOR_USAGE;
            } else if (contains(getMetaspaceNames(), name)) {
                type = PoolType.METASPACE_USAGE;
            } else if (contains(getPermNames(), name)) {
                type = PoolType.PERMGEN_USAGE;
            } else {
                continue;
            }

            MemoryUsage usage = bean.getUsage();
            poolList.add(MemoryPool.newBuilder().setType(type)
                .setInit(usage.getInit())
                .setMax(usage.getMax())
                .setCommited(usage.getCommitted())
                .setUsed(usage.getUsed())
                .build());
        }
        return poolList;
    }

    private boolean contains(String[] possibleNames, String name) {
        for (String possibleName : possibleNames) {
            if (name.equals(possibleName)) {
                return true;
            }
        }
        return false;
    }

    protected abstract String[] getPermNames();

    protected abstract String[] getCodeCacheNames();

    protected abstract String[] getEdenNames();

    protected abstract String[] getOldNames();

    protected abstract String[] getSurvivorNames();

    protected abstract String[] getMetaspaceNames();
}