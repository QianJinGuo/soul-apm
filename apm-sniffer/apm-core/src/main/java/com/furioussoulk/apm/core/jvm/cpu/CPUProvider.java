package com.furioussoulk.apm.core.jvm.cpu;

import com.furioussoulk.apm.core.logger.LogManager;

public enum CPUProvider {
    /**
     * instance
     */
    INSTANCE;
    private CPUMetricAccessor cpuMetricAccessor;

    CPUProvider() {
        int processorNum = ProcessorUtil.getNumberOfProcessors();
        try {
            this.cpuMetricAccessor =
                (CPUMetricAccessor)CPUProvider.class.getClassLoader().loadClass("org.skywalking.apm.agent.core.jvm.cpu.SunCpuAccessor")
                    .getConstructor(int.class).newInstance(processorNum);
        } catch (Exception e) {
            this.cpuMetricAccessor = new NoSupportedCPUAccessor(processorNum);
            ILog logger = LogManager.getLogger(CPUProvider.class);
            logger.error(e, "Only support accessing CPU metric in SUN JVM platform.");
        }
    }

    public CPU getCpuMetric() {
        return cpuMetricAccessor.getCPUMetric();
    }
}
