package org.oseditor.graph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GraphLayout
{
    private static Logger log = LoggerFactory.getLogger(GraphLayout.class);

    private static final double TAU = Math.PI * 2;
    private final GraphLayoutConfig config;
    private final DirectedGraph data;
    private final Random random;
    private final int nodeCount;
    String[] nodeIds;
    State state;
    int[][] distances;
    List<Integer>[] edges;
    private double angle = 0;

    GraphLayout(DirectedGraph data, GraphLayoutConfig cfg)
    {
        if (data == null)
        {
            throw new IllegalArgumentException("data can't be null");
        }

        this.config = cfg;
        this.data = data;

        this.random = new Random(cfg.getInitialSeed());

        nodeCount = data.getNodes().size();

        nodeIds = new String[nodeCount];
        state = new State(nodeCount);
        distances = new int[nodeCount][];

        fillNodeIds();
        copyState(data.getNodes());
        fillEdges(data.getEdges());
        calculateNodeDistances();
    }

    private void copyState(Map<String, GraphNode> nodes)
    {
        double[] stateData = state.getData();

        edges = new List[nodeCount * 2];

        for (int i = 0, j = 0; i < nodeCount; i++)
        {
            String id = this.nodeIds[i];
            GraphNode node = nodes.get(id);
            stateData[j++] = node.getX();
            stateData[j++] = node.getY();

            edges[i] = new ArrayList<Integer>();
        }
    }

    private void fillEdges(List<Edge> edgeData)
    {
        for (Edge e : edgeData)
        {
            int fromIdx = findNodeIndex(e.getFrom());
            int toIdx = findNodeIndex(e.getTo());
            edges[fromIdx].add(toIdx);
            edges[toIdx].add(fromIdx);
        }

        log.info("Edges: {}", Arrays.toString(edges));
    }

    private void fillNodeIds()
    {
        ArrayList<String> nodeIds = new ArrayList<String>(data.getNodes().keySet());
        Collections.sort(nodeIds);

        this.nodeIds = nodeIds.toArray(new String[nodeIds.size()]);

        log.info("node ids: {}", nodeIds);
    }

    /**
     * Returns the index the node with his id has in the internal state.
     *
     * @param id
     * @return
     */
    public int findNodeIndex(String id)
    {
        int i = Arrays.binarySearch(nodeIds, id);
        if (i < 0)
        {
            throw new NodeNotFoundException("No node with the id '" + id + "' exists.");
        }

        return i;
    }

    int getDistance(int idx, int idx2)
    {

        Map<String, GraphNode> nodes = this.data.getNodes();

        GraphNode nodeA = nodes.get(nodeIds[idx]);
        GraphNode nodeB = nodes.get(nodeIds[idx2]);
        return this.config.getDistanceFunction().getDistance(nodeA, nodeB);
    }

    private void calculateNodeDistances()
    {

        for (int startIndex = 0; startIndex < nodeCount; startIndex++)
        {
            PriorityQueue queue = new PriorityQueue(nodeCount);

            int[] dist = new int[nodeCount];

            for (int i = 0; i < nodeCount; i++)
            {
                int distance;
                if (i == startIndex)
                {
                    distance = 0;
                }
                else
                {
                    // shortcut: if we already calculated the distances from a node,
                    // we can just look up the distance to the current node
                    // this doesn't reduce complexity, but at least minimizes queue
                    // operations at minimal cost
                    //
                    // we have calculated the distance because when the node index is smaller
                    // since we calculate in increasing node order
                    if (i < startIndex)
                    {
                        distance = distances[i][startIndex];
                    }
                    else
                    {
                        // Unknown distance function from source to v
                        distance = Integer.MAX_VALUE;
                    }
                }

                dist[i] = distance;
                queue.add(distance, i);
            }


            while (!queue.isEmpty())
            {
                int index = queue.popMinimalValue();

                if (dist[index] == Integer.MAX_VALUE)
                {
                    // all remaining vertices are inaccessible from source
                    break;
                }

                for (int toIdx : edges[index])
                {
                    int newDistance = dist[index] + this.getDistance(index, toIdx);
                    int oldDistance = dist[toIdx];
                    if (newDistance < oldDistance)
                    {
                        dist[toIdx] = newDistance;
                        queue.lowerPriority(toIdx, oldDistance, newDistance);
                    }
                }
            }

            distances[startIndex] = dist;
        }
    }

    public GraphLayoutConfig getConfig()
    {
        return config;
    }

    /**
     * Writes the resulting coordinates from the layout process back into the DirectedGraph data structure
     * und return the current axis-aligned bounding box of the graph.
     *
     * @return axis-aligned bounding box of the graph
     */
    public AABB update()
    {
        Map<String, GraphNode> nodes = this.data.getNodes();
        double[] data = this.state.getData();

        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;

        for (int i = 0, j = 0; i < nodeCount; i++)
        {
            String id = nodeIds[i];
            GraphNode graphNode = nodes.get(id);
            double x = data[j++];
            double y = data[j++];

            graphNode.setX(x);
            graphNode.setY(y);

            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
        }

        return new AABB(minX, minY, maxX - minX, maxY - minY);
    }

    /**
     * Do one RK4 simulation step. Step size is defined by
     * the "stepSize" option.
     */
    public void simulate()
    {
        double delta = this.config.getStepSize();

        State a = new State(nodeCount);
        State b = this.evaluate(state, delta * 0.5, a);
        State c = this.evaluate(state, delta * 0.5, b);
        State d = this.evaluate(state, delta, c);

        b.add(c).scale(2);
        d.add(a).add(b).scale(1.0/6);

        this.state.add(d.scale(delta));
    }

    private State evaluate(State initialState, double delta, State derivative)
    {
        State state = derivative.clone().scale(delta).add(initialState);

        return this.simulate(state, delta);
    }

    private State simulate(State state, double delta)
    {
        State derivative = new State(nodeCount);

        if (delta == 0)
        {
            // Nothing happens in zero time..
            return derivative;
        }

        double[] stateXY = state.getData();
        double[] derivativeXY = derivative.getData();
        for (int i = 0; i < nodeCount; i++)
        {
            int componentIndexI = i*2;

            double nodeX = stateXY[componentIndexI];
            double nodeY = stateXY[componentIndexI + 1];

            int[] nodeDistance = distances[i];
            for (int j = i + 1; j < nodeCount; j++)
            {
                int componentIndexJ = j*2;
                double x = stateXY[componentIndexJ] - nodeX;
                double y = stateXY[componentIndexJ + 1] - nodeY;

                double distance;
                if (x == 0 && y == 0)
                {
                    x = Math.cos(angle);
                    y = Math.sin(angle);
                    distance = 1;

                    angle += 0.5;
                }
                else
                {
                    distance = Math.sqrt(x * x + y * y);
                }

                int targetDistance = nodeDistance[j];

                if (targetDistance < Integer.MAX_VALUE)
                {
                    double force = -config.getSpringConstant() * (distance - targetDistance);

                    double factorToLength = (force * delta) / distance;
                    x *= factorToLength;
                    y *= factorToLength;

                    derivativeXY[componentIndexJ] += x;
                    derivativeXY[componentIndexJ + 1] += y;

                    derivativeXY[componentIndexI] -= x;
                    derivativeXY[componentIndexI + 1] -= y;
                }
            }
        }

        return derivative;
    }

    /**
     * Exposes the internal simulation state. Normally you should not touch this unless you want
     * to animate the intermediary animation steps.
     *
     * @return
     */
    public State getInternalState()
    {
        return state;
    }


    public String getIdForIndex(int index)
    {
        return nodeIds[index];
    }

    public boolean areAllNodesConnected()
    {
        for (int i=0; i < 11; i++)
        {
            for (int j=0; j < 11; j++)
            {
                if (distances[i][j] == Integer.MAX_VALUE)
                {
                    return false;
                }
            }
        }

        return true;
    }


    public String getDistanceDebugInfo()
    {

        StringBuilder sb = new StringBuilder();


        for (int i=0; i < nodeCount; i++)
        {
            int[] dist = distances[i];

            sb.append("Distances from ").append(nodeIds[i]).append(" to:\n");

            for (int j=0; j < nodeCount; j++)
            {
                int distance = dist[j];

                sb.append(nodeIds[j]).append(": ").append(distance).append("\n");
            }
            sb.append("\n");
        }

        for (int i=0; i < nodeCount; i++)
        {
            for (int j=0; j < nodeCount; j++)
            {
                if (distances[i][j] != distances[j][i])
                {
                    sb.append("Distance from " + i + " to " + j + " is not the same as back.\n");
                }
            }
        }


        return sb.toString();
    }
}

