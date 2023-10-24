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

    /**
     * Perform Dijkstra's algorithm to find the shortest paths from a source vertex to all other vertices.
     * Assumes non-negative weights for edges.
     *
     * @param source the source vertex
     * @return a map of vertices to their shortest distance from the source
     */
    public Map<V, Double> dijkstra(V source) {
        Map<V, Double> shortestDistances = new HashMap<>();
        PriorityQueue<VertexDistance<V>> priorityQueue = new PriorityQueue<>(
            Comparator.comparing(VertexDistance::getDistance)
        );
        Set<V> visited = new HashSet<>();

        // Initialize distances
        for (V vertex : adjacencyList.keySet()) {
            shortestDistances.put(vertex, Double.POSITIVE_INFINITY);
        }
        shortestDistances.put(source, 0.0);
        priorityQueue.offer(new VertexDistance<>(source, 0.0));

        while (!priorityQueue.isEmpty()) {
            V current = priorityQueue.poll().getVertex();
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            for (Edge<V> neighbor : adjacencyList.getOrDefault(
                current,
                Collections.emptyList()
            )) {
                V next = neighbor.getDestination();
                double newDistance =
                    shortestDistances.get(current) + neighbor.getWeight();
                if (newDistance < shortestDistances.get(next)) {
                    shortestDistances.put(next, newDistance);
                    priorityQueue.offer(
                        new VertexDistance<>(next, newDistance)
                    );
                }
            }
        }

        return shortestDistances;
    }

    /**
     * A-star (A*) search algorithm to find the shortest path from start to goal vertex.
     * Uses a heuristic function for estimating the cost from current to goal vertex.
     *
     * @param start the starting vertex
     * @param goal  the goal vertex
     * @param heuristic a heuristic function estimating cost from current to goal
     * @return list of vertices representing the shortest path from start to goal
     */
    public List<V> aStar(V start, V goal, Heuristic<V> heuristic) {
        Map<V, Double> gScore = new HashMap<>();
        Map<V, Double> fScore = new HashMap<>();
        Map<V, V> cameFrom = new HashMap<>();
        PriorityQueue<VertexDistance<V>> priorityQueue = new PriorityQueue<>(
            Comparator.comparing(VertexDistance::getDistance)
        );
        Set<V> visited = new HashSet<>();

        gScore.put(start, 0.0);
        fScore.put(start, heuristic.calculate(start, goal));
        priorityQueue.offer(new VertexDistance<>(start, fScore.get(start)));

        while (!priorityQueue.isEmpty()) {
            V current = priorityQueue.poll().getVertex();
            if (current.equals(goal)) {
                return reconstructPath(cameFrom, current);
            }
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            for (Edge<V> neighbor : adjacencyList.getOrDefault(
                current,
                Collections.emptyList()
            )) {
                V next = neighbor.getDestination();
                double tentativeGScore =
                    gScore.getOrDefault(current, Double.POSITIVE_INFINITY) +
                    neighbor.getWeight();
                if (
                    tentativeGScore <
                    gScore.getOrDefault(next, Double.POSITIVE_INFINITY)
                ) {
                    cameFrom.put(next, current);
                    gScore.put(next, tentativeGScore);
                    fScore.put(
                        next,
                        gScore.get(next) + heuristic.calculate(next, goal)
                    );
                    priorityQueue.offer(
                        new VertexDistance<>(next, fScore.get(next))
                    );
                }
            }
        }

        return Collections.emptyList(); // No path found
    }

    /**
     * Reconstructs the path from start to goal using the cameFrom map.
     *
     * @param cameFrom map containing the previous vertex for each vertex in the path
     * @param current  the current vertex
     * @return list of vertices representing the reconstructed path from start to goal
     */
    private List<V> reconstructPath(Map<V, V> cameFrom, V current) {
        List<V> path = new ArrayList<>();
        while (cameFrom.containsKey(current)) {
            path.add(current);
            current = cameFrom.get(current);
        }
        Collections.reverse(path);
        return path;
    }

    /**
     * Represents an edge in the graph with a destination vertex and weight.
     *
     * @param <V> the type of vertex in the graph
     */
    private static class Edge<V> {

        private V destination;
        private double weight;

        public Edge(V destination, double weight) {
            this.destination = destination;
            this.weight = weight;
        }

        public V getDestination() {
            return destination;
        }

        public double getWeight() {
            return weight;
        }
    }

    /**
     * Represents a vertex and its distance used in priority queues.
     *
     * @param <V> the type of vertex in the graph
     */
    private static class VertexDistance<V> {

        private V vertex;
        private double distance;

        public VertexDistance(V vertex, double distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        public V getVertex() {
            return vertex;
        }

        public double getDistance() {
            return distance;
        }
    }

    /**
     * Represents a heuristic function for A* algorithm.
     *
     * @param <V> the type of vertex in the graph
     */
    public interface Heuristic<V> {
        double calculate(V current, V goal);
    }
}
