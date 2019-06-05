

package com.furioussoulk.apm.core.jvm.memorypool;

import java.lang.management.MemoryPoolMXBean;
import java.util.List;

/**
 * @author wusheng
 */
public class ParallelCollectorModule extends MemoryPoolModule {

    public ParallelCollectorModule(List<MemoryPoolMXBean> beans) {
        super(beans);
    }

    @Override protected String[] getPermNames() {
        return new String[] {"PS Perm Gen", "Compressed Class Space"};
    }

    @Override protected String[] getCodeCacheNames() {
        return new String[] {"Code Cache"};
    }

    @Override protected String[] getEdenNames() {
        return new String[] {"PS Eden Space"};
    }

    @Override protected String[] getOldNames() {
        return new String[] {"PS Old Gen"};
    }

    @Override protected String[] getSurvivorNames() {
        return new String[] {"PS Survivor Space"};
    }

    @Override protected String[] getMetaspaceNames() {
        return new String[] {"Metaspace"};
    }
}
