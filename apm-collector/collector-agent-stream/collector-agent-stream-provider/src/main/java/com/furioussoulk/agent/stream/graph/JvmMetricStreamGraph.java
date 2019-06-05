package com.furioussoulk.agent.stream.graph;

import com.furioussoulk.apm.collector.core.module.ModuleManager;

public class JvmMetricStreamGraph {

    public static final int GC_METRIC_GRAPH_ID = 100;
    public static final int MEMORY_METRIC_GRAPH_ID = 101;
    public static final int MEMORY_POOL_METRIC_GRAPH_ID = 102;
    public static final int CPU_METRIC_GRAPH_ID = 103;
    public static final int INST_HEART_BEAT_GRAPH_ID = 104;

    private final ModuleManager moduleManager;
    private final WorkerCreateListener workerCreateListener;

    public JvmMetricStreamGraph(ModuleManager moduleManager, WorkerCreateListener workerCreateListener) {
        this.moduleManager = moduleManager;
        this.workerCreateListener = workerCreateListener;
    }

    @SuppressWarnings("unchecked")
    public void createGcMetricGraph() {
        QueueCreatorService<GCMetric> queueCreatorService = moduleManager.find(QueueModule.NAME).getService(QueueCreatorService.class);

        Graph<GCMetric> graph = GraphManager.INSTANCE.createIfAbsent(GC_METRIC_GRAPH_ID, GCMetric.class);
        graph.addNode(new GCMetricPersistenceWorker.Factory(moduleManager, queueCreatorService).create(workerCreateListener));
    }

    @SuppressWarnings("unchecked")
    public void createCpuMetricGraph() {
        QueueCreatorService<CpuMetric> queueCreatorService = moduleManager.find(QueueModule.NAME).getService(QueueCreatorService.class);

        Graph<CpuMetric> graph = GraphManager.INSTANCE.createIfAbsent(CPU_METRIC_GRAPH_ID, CpuMetric.class);
        graph.addNode(new CpuMetricPersistenceWorker.Factory(moduleManager, queueCreatorService).create(workerCreateListener));
    }

    @SuppressWarnings("unchecked")
    public void createMemoryMetricGraph() {
        QueueCreatorService<MemoryMetric> queueCreatorService = moduleManager.find(QueueModule.NAME).getService(QueueCreatorService.class);

        Graph<MemoryMetric> graph = GraphManager.INSTANCE.createIfAbsent(MEMORY_METRIC_GRAPH_ID, MemoryMetric.class);
        graph.addNode(new MemoryMetricPersistenceWorker.Factory(moduleManager, queueCreatorService).create(workerCreateListener));
    }

    @SuppressWarnings("unchecked")
    public void createMemoryPoolMetricGraph() {
        QueueCreatorService<MemoryPoolMetric> queueCreatorService = moduleManager.find(QueueModule.NAME).getService(QueueCreatorService.class);

        Graph<MemoryPoolMetric> graph = GraphManager.INSTANCE.createIfAbsent(MEMORY_POOL_METRIC_GRAPH_ID, MemoryPoolMetric.class);
        graph.addNode(new MemoryPoolMetricPersistenceWorker.Factory(moduleManager, queueCreatorService).create(workerCreateListener));
    }

    @SuppressWarnings("unchecked")
    public void createHeartBeatGraph() {
        QueueCreatorService<Instance> queueCreatorService = moduleManager.find(QueueModule.NAME).getService(QueueCreatorService.class);

        Graph<Instance> graph = GraphManager.INSTANCE.createIfAbsent(INST_HEART_BEAT_GRAPH_ID, Instance.class);
        graph.addNode(new InstHeartBeatPersistenceWorker.Factory(moduleManager, queueCreatorService).create(workerCreateListener));
    }
}
