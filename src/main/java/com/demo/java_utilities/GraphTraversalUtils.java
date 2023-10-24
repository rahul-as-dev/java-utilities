package com.demo.java_utilities;

import java.util.*;

public class GraphTraversalUtils<V> {

    private Map<V, List<Edge<V>>> adjacencyList;

    public GraphTraversalUtils() {
        this.adjacencyList = new HashMap<>();
    }

    /**
     * Add a vertex to the graph.
     *
     * @param vertex the vertex to add
     */
    public void addVertex(V vertex) {
        if (!adjacencyList.containsKey(vertex)) {
            adjacencyList.put(vertex, new ArrayList<>());
        }
    }

    /**
     * Add an undirected edge between two vertices with a given weight.
     *
     * @param source   the source vertex of the edge
     * @param destination the destination vertex of the edge
     * @param weight   the weight of the edge
     */
    public void addUndirectedEdge(V source, V destination, double weight) {
        addDirectedEdge(source, destination, weight);
        addDirectedEdge(destination, source, weight);
    }

    /**
     * Add a directed edge between two vertices with a given weight.
     *
     * @param source      the source vertex of the edge
     * @param destination the destination vertex of the edge
     * @param weight      the weight of the edge
     */
    public void addDirectedEdge(V source, V destination, double weight) {
        if (!adjacencyList.containsKey(source)) {
            addVertex(source);
        }
        if (!adjacencyList.containsKey(destination)) {
            addVertex(destination);
        }
        adjacencyList.get(source).add(new Edge<>(destination, weight));
    }

    /**
     * Perform Depth-First Search (DFS) on the graph.
     *
     * @param start the starting vertex for DFS
     * @return list of vertices in DFS order
     */
    public List<V> dfs(V start) {
        List<V> dfsOrder = new ArrayList<>();
        Set<V> visited = new HashSet<>();
        dfsRecursive(start, visited, dfsOrder);
        return dfsOrder;
    }

    /**
     * Helper method for performing DFS recursively.
     *
     * @param vertex    the current vertex for DFS
     * @param visited   set of visited vertices
     * @param dfsOrder  list to store vertices in DFS order
     */
    private void dfsRecursive(V vertex, Set<V> visited, List<V> dfsOrder) {
        visited.add(vertex);
        dfsOrder.add(vertex);
        for (Edge<V> neighbor : adjacencyList.getOrDefault(
            vertex,
            Collections.emptyList()
        )) {
            if (!visited.contains(neighbor.getDestination())) {
                dfsRecursive(neighbor.getDestination(), visited, dfsOrder);
            }
        }
    }

    /**
     * Perform Breadth-First Search (BFS) on the graph.
     *
     * @param start the starting vertex for BFS
     * @return list of vertices in BFS order
     */
    public List<V> bfs(V start) {
        List<V> bfsOrder = new ArrayList<>();
        Queue<V> queue = new LinkedList<>();
        Set<V> visited = new HashSet<>();

        queue.offer(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            V vertex = queue.poll();
            bfsOrder.add(vertex);
            for (Edge<V> neighbor : adjacencyList.getOrDefault(
                vertex,
                Collections.emptyList()
            )) {
                if (!visited.contains(neighbor.getDestination())) {
                    queue.offer(neighbor.getDestination());
                    visited.add(neighbor.getDestination());
                }
            }
        }

        return bfsOrder;
    }
}
