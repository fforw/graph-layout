package org.oseditor.graph;

/**
 * Calculates the ideal distance between node a and node b.
 *
 * Delivers the start values to be optimized.
 */
public interface DistanceFunction
{
    int getDistance(GraphNode a, GraphNode b);
}
