package com.furioussoulk.apm.core.jvm.memorypool;

import java.lang.management.MemoryPoolMXBean;
import java.util.List;

public class G1CollectorModule extends MemoryPoolModule {
    public G1CollectorModule(List<MemoryPoolMXBean> beans) {
        super(beans);
    }

    @Override protected String[] getPermNames() {
        return new String[] {"G1 Perm Gen", "Compressed Class Space"};
    }

    @Override protected String[] getCodeCacheNames() {
        return new String[] {"Code Cache"};
    }

    @Override protected String[] getEdenNames() {
        return new String[] {"G1 Eden Space"};
    }

    @Override protected String[] getOldNames() {
        return new String[] {"G1 Old Gen"};
    }

    @Override protected String[] getSurvivorNames() {
        return new String[] {"G1 Survivor Space"};
    }

    @Override protected String[] getMetaspaceNames() {
        return new String[] {"Metaspace"};
    }
}
