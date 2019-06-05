package com.furioussoulk.agent.stream;

import com.furioussoulk.agent.stream.graph.JvmMetricStreamGraph;
import com.furioussoulk.apm.collector.core.module.ModuleManager;

public class AgentStreamBootStartup {

    private final ModuleManager moduleManager;
    private final WorkerCreateListener workerCreateListener;

    public AgentStreamBootStartup(ModuleManager moduleManager) {
        this.moduleManager = moduleManager;
        this.workerCreateListener = new WorkerCreateListener();
    }

    public void start() {
        createJVMGraph();
        createRegisterGraph();
        createTraceGraph();

        PersistenceTimer timer = new PersistenceTimer();
        timer.start(moduleManager, workerCreateListener.getPersistenceWorkers());

    }

    private void createJVMGraph() {
        JvmMetricStreamGraph jvmMetricStreamGraph = new JvmMetricStreamGraph(moduleManager, workerCreateListener);
        jvmMetricStreamGraph.createCpuMetricGraph();
        jvmMetricStreamGraph.createGcMetricGraph();
        jvmMetricStreamGraph.createMemoryMetricGraph();
        jvmMetricStreamGraph.createMemoryPoolMetricGraph();
        jvmMetricStreamGraph.createHeartBeatGraph();
    }

    private void createRegisterGraph() {
        RegisterStreamGraph registerStreamGraph = new RegisterStreamGraph(moduleManager, workerCreateListener);
        registerStreamGraph.createApplicationRegisterGraph();
        registerStreamGraph.createInstanceRegisterGraph();
        registerStreamGraph.createServiceNameRegisterGraph();
    }

    private void createTraceGraph() {
        TraceStreamGraph traceStreamGraph = new TraceStreamGraph(moduleManager, workerCreateListener);
        traceStreamGraph.createSegmentStandardizationGraph();
        traceStreamGraph.createGlobalTraceGraph();
        traceStreamGraph.createInstPerformanceGraph();
        traceStreamGraph.createNodeComponentGraph();
        traceStreamGraph.createNodeMappingGraph();
        traceStreamGraph.createNodeReferenceGraph();
        traceStreamGraph.createServiceEntryGraph();
        traceStreamGraph.createServiceReferenceGraph();
        traceStreamGraph.createSegmentGraph();
        traceStreamGraph.createSegmentCostGraph();
    }
}
