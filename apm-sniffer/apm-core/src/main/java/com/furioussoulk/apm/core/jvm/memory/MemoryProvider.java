package com.furioussoulk.apm.core.jvm.memory;

import com.furioussoulk.network.proto.Memory;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.LinkedList;
import java.util.List;

public enum MemoryProvider {
    /**
     * instance
     */
    INSTANCE;
    private final MemoryMXBean memoryMXBean;

    MemoryProvider() {
        this.memoryMXBean = ManagementFactory.getMemoryMXBean();
    }

    public List<Memory> getMemoryMetricList() {
        List<Memory> memoryList = new LinkedList<Memory>();

        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        Memory.Builder heapMemoryBuilder = Memory.newBuilder();
        heapMemoryBuilder.setIsHeap(true);
        copy(heapMemoryBuilder, heapMemoryUsage);
        memoryList.add(heapMemoryBuilder.build());

        MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
        Memory.Builder nonHeapMemoryBuilder = Memory.newBuilder();
        heapMemoryBuilder.setIsHeap(false);
        copy(nonHeapMemoryBuilder, nonHeapMemoryUsage);
        memoryList.add(nonHeapMemoryBuilder.build());

        return memoryList;
    }


    private Memory.Builder copy(Memory.Builder target, MemoryUsage source){
        target.setInit(source.getInit());
        target.setUsed(source.getUsed());
        target.setCommitted(source.getCommitted());
        target.setMax(source.getMax());
        return target;
    }
}
