package com.furioussoulk.apm.core.jvm.memorypool;

import com.furioussoulk.network.proto.MemoryPool;

import java.util.List;

public interface MemoryPoolMetricAccessor {
    List<MemoryPool> getMemoryPoolMetricList();
}
