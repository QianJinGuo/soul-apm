package com.furioussoulk.apm.core.jvm.memorypool;

import com.furioussoulk.network.proto.MemoryPool;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.util.List;

public enum MemoryPoolProvider {
    /**
     * instance
     */
    INSTANCE;

    private MemoryPoolMetricAccessor metricAccessor;
    private List<MemoryPoolMXBean> beans;

    MemoryPoolProvider() {
        beans = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean bean : beans) {
            String name = bean.getName();
            MemoryPoolMetricAccessor accessor = findByBeanName(name);
            if (accessor != null) {
                metricAccessor = accessor;
                break;
            }
        }
        if (metricAccessor == null) {
            metricAccessor = new UnknownMemoryPool();
        }
    }

    public List<MemoryPool> getMemoryPoolMetricList() {
        return metricAccessor.getMemoryPoolMetricList();
    }

    private MemoryPoolMetricAccessor findByBeanName(String name) {
        if (name.indexOf("PS") > -1) {
            //Parallel (Old) collector ( -XX:+UseParallelOldGC )
            return new ParallelCollectorModule(beans);
        } else if (name.indexOf("CMS") > -1) {
            // CMS collector ( -XX:+UseConcMarkSweepGC )
            return new CMSCollectorModule(beans);
        } else if (name.indexOf("G1") > -1) {
            // G1 collector ( -XX:+UseG1GC )
            return new G1CollectorModule(beans);
        } else if (name.equals("Survivor Space")) {
            // Serial collector ( -XX:+UseSerialGC )
            return new SerialCollectorModule(beans);
        } else {
            // Unknown
            return null;
        }
    }
}
