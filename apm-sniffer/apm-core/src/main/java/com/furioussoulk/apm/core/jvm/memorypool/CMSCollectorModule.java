
package com.furioussoulk.apm.core.jvm.memorypool;

import java.lang.management.MemoryPoolMXBean;
import java.util.List;

public class CMSCollectorModule extends MemoryPoolModule {
    public CMSCollectorModule(List<MemoryPoolMXBean> beans) {
        super(beans);
    }

    @Override protected String[] getPermNames() {
        return new String[] {"CMS Perm Gen", "Compressed Class Space"};
    }

    @Override protected String[] getCodeCacheNames() {
        return new String[] {"Code Cache"};
    }

    @Override protected String[] getEdenNames() {
        return new String[] {"Par Eden Space"};
    }

    @Override protected String[] getOldNames() {
        return new String[] {"CMS Old Gen"};
    }

    @Override protected String[] getSurvivorNames() {
        return new String[] {"Par Survivor Space"};
    }

    @Override protected String[] getMetaspaceNames() {
        return new String[] {"Metaspace"};
    }
}
