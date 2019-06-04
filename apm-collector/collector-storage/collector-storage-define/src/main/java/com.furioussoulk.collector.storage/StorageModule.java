package com.furioussoulk.collector.storage;

import com.furioussoulk.collector.stream.worker.collector.stream.worker.core.module.Module;

import java.util.ArrayList;
import java.util.List;

public class StorageModule extends Module {

    public static final String NAME = "storage";

    @Override public String name() {
        return NAME;
    }

    @Override public Class[] services() {
        List<Class> classes = new ArrayList<>();
//        classes.add(IBatchDAO.class);

        addCacheDAO(classes);
/*        addRegisterDAO(classes);
        addPersistenceDAO(classes);
        addUiDAO(classes);*/

        return classes.toArray(new Class[] {});
    }

    private void addCacheDAO(List<Class> classes) {
//        classes.add(IApplicationCacheDAO.class);
//        classes.add(IInstanceCacheDAO.class);
//        classes.add(IServiceNameCacheDAO.class);
    }

   /* private void addRegisterDAO(List<Class> classes) {
        classes.add(IApplicationRegisterDAO.class);
        classes.add(IInstanceRegisterDAO.class);
        classes.add(IServiceNameRegisterDAO.class);
    }

    private void addPersistenceDAO(List<Class> classes) {
        classes.add(ICpuMetricPersistenceDAO.class);
        classes.add(IGCMetricPersistenceDAO.class);
        classes.add(IMemoryMetricPersistenceDAO.class);
        classes.add(IMemoryPoolMetricPersistenceDAO.class);

        classes.add(IGlobalTracePersistenceDAO.class);
        classes.add(IInstPerformancePersistenceDAO.class);
        classes.add(INodeComponentPersistenceDAO.class);
        classes.add(INodeMappingPersistenceDAO.class);
        classes.add(INodeReferencePersistenceDAO.class);
        classes.add(ISegmentCostPersistenceDAO.class);
        classes.add(ISegmentPersistenceDAO.class);
        classes.add(IServiceEntryPersistenceDAO.class);
        classes.add(IServiceReferencePersistenceDAO.class);

        classes.add(IInstanceHeartBeatPersistenceDAO.class);
    }

    private void addUiDAO(List<Class> classes) {
        classes.add(IInstanceUIDAO.class);

        classes.add(ICpuMetricUIDAO.class);
        classes.add(IGCMetricUIDAO.class);
        classes.add(IMemoryMetricUIDAO.class);
        classes.add(IMemoryPoolMetricUIDAO.class);

        classes.add(IGlobalTraceUIDAO.class);
        classes.add(IInstPerformanceUIDAO.class);
        classes.add(INodeComponentUIDAO.class);
        classes.add(INodeMappingUIDAO.class);
        classes.add(INodeReferenceUIDAO.class);
        classes.add(ISegmentCostUIDAO.class);
        classes.add(ISegmentUIDAO.class);
        classes.add(IServiceEntryUIDAO.class);
        classes.add(IServiceReferenceUIDAO.class);
    }*/
}
