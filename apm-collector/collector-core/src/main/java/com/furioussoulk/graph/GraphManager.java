package com.furioussoulk.graph;

import com.furioussoulk.exception.GraphNotFoundException;

import java.util.HashMap;
import java.util.Map;

public enum GraphManager {
    INSTANCE;


    private Map<Integer, Graph> allGraphs = new HashMap<>();

    /**
     * Create a stream process graph.
     *
     * @param graphId represents a graph, which is used for finding it.
     * @return
     */
    public synchronized <INPUT> Graph<INPUT> createIfAbsent(int graphId) {
        if (!allGraphs.containsKey(graphId)) {
            Graph graph = new Graph(graphId);
            allGraphs.put(graphId, graph);
            return graph;
        } else {
            return allGraphs.get(graphId);
        }
    }

    public Graph findGraph(int graphId) {
        Graph graph = allGraphs.get(graphId);
        if (graph == null) {
            throw new GraphNotFoundException("Graph id=" + graphId + " not found in this GraphManager");
        }
        return graph;
    }

    public void reset() {
        allGraphs.clear();
    }
}
