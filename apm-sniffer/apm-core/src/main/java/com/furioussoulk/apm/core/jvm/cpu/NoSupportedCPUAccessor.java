package com.furioussoulk.apm.core.jvm.cpu;

public class NoSupportedCPUAccessor extends CPUMetricAccessor {
    public NoSupportedCPUAccessor(int cpuCoreNum) {
        super(cpuCoreNum);
    }

    @Override
    protected long getCpuTime() {
        return 0;
    }
}
