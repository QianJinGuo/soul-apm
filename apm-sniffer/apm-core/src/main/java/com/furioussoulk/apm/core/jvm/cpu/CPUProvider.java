package com.furioussoulk.apm.core.jvm.cpu;

import com.furioussoulk.apm.core.logger.LogManager;
import com.furioussoulk.apm.core.logger.api.ILogger;
import com.furioussoulk.apm.core.os.ProcessorUtil;
import com.furioussoulk.network.proto.CPU;

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
                (CPUMetricAccessor)CPUProvider.class.getClassLoader().loadClass("com.furioussoulk.apm.core.jvm.cpu.SunCpuAccessor")
                    .getConstructor(int.class).newInstance(processorNum);
        } catch (Exception e) {
            this.cpuMetricAccessor = new NoSupportedCPUAccessor(processorNum);
            ILogger logger = LogManager.getLogger(CPUProvider.class);
            logger.error(e, "Only support accessing CPU metric in SUN JVM platform.");
        }
    }

    public CPU getCpuMetric() {
        return cpuMetricAccessor.getCPUMetric();
    }
}
