package com.furioussoulk.apm.core.jvm.cpu;

import com.furioussoulk.network.proto.*;

public abstract class CPUMetricAccessor {
    private long lastCPUTimeNs;
    private long lastSampleTimeNs;
    private final int cpuCoreNum;

    public CPUMetricAccessor(int cpuCoreNum) {
        this.cpuCoreNum = cpuCoreNum;
    }

    protected void init() {
        lastCPUTimeNs = this.getCpuTime();
        this.lastSampleTimeNs = System.nanoTime();
    }

    protected abstract long getCpuTime();

    public CPU getCPUMetric() {
        long cpuTime = this.getCpuTime();
        long cpuCost = cpuTime - lastCPUTimeNs;
        long now = System.nanoTime();

        CPU.Builder cpuBuilder = CPU.newBuilder();
        return cpuBuilder.setUsagePercent(cpuCost * 1.0d / ((now - lastSampleTimeNs) * cpuCoreNum)).build();
    }
}
