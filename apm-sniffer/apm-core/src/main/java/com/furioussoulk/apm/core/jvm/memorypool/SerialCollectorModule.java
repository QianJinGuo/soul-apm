

package com.furioussoulk.apm.core.jvm.memorypool;

import java.lang.management.MemoryPoolMXBean;
import java.util.List;

/**
 * @author wusheng
 */
public class SerialCollectorModule extends MemoryPoolModule {
    public SerialCollectorModule(List<MemoryPoolMXBean> beans) {
        super(beans);
    }

    @Override protected String[] getPermNames() {
        return new String[] {"Perm Gen", "Compressed Class Space"};
    }

    @Override protected String[] getCodeCacheNames() {
        return new String[] {"Code Cache"};
    }

    @Override protected String[] getEdenNames() {
        return new String[] {"Eden Space"};
    }

    @Override protected String[] getOldNames() {
        return new String[] {"Tenured Gen"};
    }

    @Override protected String[] getSurvivorNames() {
        return new String[] {"Survivor Space"};
    }

    @Override protected String[] getMetaspaceNames() {
        return new String[] {"Metaspace"};
    }
}
