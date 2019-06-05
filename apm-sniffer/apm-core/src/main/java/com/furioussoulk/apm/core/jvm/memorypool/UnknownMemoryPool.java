package com.furioussoulk.apm.core.jvm.memorypool;

import com.furioussoulk.network.proto.MemoryPool;
import com.furioussoulk.network.proto.PoolType;

import java.util.LinkedList;
import java.util.List;

/**
 * @author wusheng
 */
public class UnknownMemoryPool implements MemoryPoolMetricAccessor {
    @Override
    public List<MemoryPool> getMemoryPoolMetricList() {
        List<MemoryPool> poolList = new LinkedList<MemoryPool>();
        poolList.add(MemoryPool.newBuilder().setType(PoolType.CODE_CACHE_USAGE).build());
        poolList.add(MemoryPool.newBuilder().setType(PoolType.NEWGEN_USAGE).build());
        poolList.add(MemoryPool.newBuilder().setType(PoolType.OLDGEN_USAGE).build());
        poolList.add(MemoryPool.newBuilder().setType(PoolType.SURVIVOR_USAGE).build());
        poolList.add(MemoryPool.newBuilder().setType(PoolType.PERMGEN_USAGE).build());
        poolList.add(MemoryPool.newBuilder().setType(PoolType.METASPACE_USAGE).build());
        return new LinkedList<MemoryPool>();
    }
}
