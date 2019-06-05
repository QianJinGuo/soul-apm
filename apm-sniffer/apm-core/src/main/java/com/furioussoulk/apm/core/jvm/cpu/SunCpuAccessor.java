package com.furioussoulk.apm.core.jvm.cpu;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;

public class SunCpuAccessor extends CPUMetricAccessor {
    private final OperatingSystemMXBean osMBean;

    public SunCpuAccessor(int cpuCoreNum) {
        super(cpuCoreNum);
        this.osMBean = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
        this.init();
    }

    @Override
    protected long getCpuTime() {
        return osMBean.getProcessCpuTime();
    }
}
