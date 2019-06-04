package com.furioussoulk.collector.stream.worker.collector.stream.worker.graph;

import com.furioussoulk.collector.stream.worker.collector.stream.worker.exception.PotentialCyclicGraphException;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author 孙证杰
 * @date 2019/6/3 13:29
 */
public final class Graph<INPUT>{
    private int id;
    private WayToNode entryWay;
    private ConcurrentHashMap<Integer, Node> nodeIndex = new ConcurrentHashMap<>();

    Graph(int id) {
        this.id = id;
    }

    public void start(INPUT input) {
        entryWay.in(input);
    }

    public <OUTPUT> Node<INPUT, OUTPUT> addNode(NodeProcessor<INPUT, OUTPUT> nodeProcessor) {
        return addNode(new DirectWay(nodeProcessor));
    }

    public <OUTPUT> Node<INPUT, OUTPUT> addNode(WayToNode<INPUT, OUTPUT> entryWay) {
        synchronized (this) {
            this.entryWay = entryWay;
            this.entryWay.buildDestination(this);
            return entryWay.getDestination();
        }
    }

    void checkForNewNode(Node node) {
        int nodeId = node.getHandler().id();
        if (nodeIndex.containsKey(nodeId)) {
            throw new PotentialCyclicGraphException("handler="
                    + node.getHandler().getClass().getName()
                    + " already exists in graph[" + id + "]");
        }
        nodeIndex.put(nodeId, node);
    }

    public GraphNodeFinder toFinder() {
        return new GraphNodeFinder(this);
    }

    ConcurrentHashMap<Integer, Node> getNodeIndex() {
        return nodeIndex;
    }

    int getId() {
        return id;
    }
}
