package com.furioussoulk.apm.collector.core.graph;

import com.furioussoulk.apm.core.exception.NodeNotFoundException;

import java.util.concurrent.ConcurrentHashMap;

public class GraphNodeFinder {
    private Graph graph;

    GraphNodeFinder(Graph graph) {
        this.graph = graph;
    }

    /**
     * Find an exist node to build the graph.
     *
     * @param handlerId    of specific node in graph.
     * @param outputClass  of the found node
     * @param <NODEOUTPUT> type of given output class
     * @return Node instance.
     */
    public <NODEOUTPUT> Node<?, NODEOUTPUT> findNode(int handlerId, Class<NODEOUTPUT> outputClass) {
        ConcurrentHashMap<Integer, Node> graphNodeIndex = graph.getNodeIndex();
        Node node = graphNodeIndex.get(handlerId);
        if (node == null) {
            throw new NodeNotFoundException("Can't find node with handlerId="
                    + handlerId
                    + " in graph[" + graph.getId() + "]");
        }
        return node;
    }

    public Next findNext(int handlerId) {
        ConcurrentHashMap<Integer, Node> graphNodeIndex = graph.getNodeIndex();
        Node node = graphNodeIndex.get(handlerId);
        if (node == null) {
            throw new NodeNotFoundException("Can't find node with handlerId="
                    + handlerId
                    + " in graph[" + graph.getId() + "]");
        }
        return node.getNext();
    }
}
