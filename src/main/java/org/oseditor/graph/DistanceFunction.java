package org.oseditor.graph;

import java.util.List;

/**
 * Calculates the ideal distance between node a and node b.
 *
 * Delivers the start values to be optimized.
 */
public interface DistanceFunction
{
    int getDistance(GraphNode a, GraphNode b, List<Integer> edgeIndexesA, List<Integer> edgeIndexesB);
}
