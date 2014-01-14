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
    extends AbstractForceSolver
{
    private static Logger log = LoggerFactory.getLogger(GraphLayout.class);

    private static final double TAU = Math.PI * 2;

    final DirectedGraph data;
    final Random random;
    final double pushStartTime;
    String[] nodeIds;
    List<Integer>[] edges;

    final GraphLayoutConfig config;
    int[][] distances;

    int[] subgraph;

    GraphLayout(DirectedGraph data, GraphLayoutConfig cfg)
    {

        super(data.getNodes().size(), cfg.getStepSize());


        if (data == null)
        {
            throw new IllegalArgumentException("data can't be null");
        }

        this.data = data;

        if (cfg.getRepulsionFallOff() == null)
        {
            throw new EditorRuntimeException("No repulsion fall off defined");
        }
        if (cfg.getSpringFallOff() == null)
        {
            throw new EditorRuntimeException("No spring fall off defined");
        }

        this.config = cfg;

        distances = new int[nodeCount][];
        subgraph = new int[nodeCount];

        this.random = new Random(cfg.getInitialSeed());

        nodeIds = new String[nodeCount];

        fillNodeIds();
        copyState(data.getNodes());
        fillEdges(data.getEdges());
        calculateNodeDistances();

        pushStartTime = config.getIterations() * config.getStepSize() * config.getRepulsionStart();
    }

    private void copyState(Map<String, GraphNode> nodes)
    {
        double[] stateData = getInternalState().getData();

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
        return this.config.getDistanceFunction().getDistance(nodeA, nodeB, edges[idx], edges[idx2]);
    }

    private void calculateNodeDistances()
    {
        for (int startIndex = 0; startIndex < nodeCount; startIndex++)
        {

            int[] dist = new int[nodeCount];

            PriorityQueue queue = fillQueue(startIndex, dist);


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
                    int newDistance = (int) ((dist[index] * this.config.getDistanceFactor()) + this.getDistance(index, toIdx));
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

    private PriorityQueue fillQueue(int startIndex, int[] dist)
    {
        PriorityQueue queue = new PriorityQueue(nodeCount);
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
        return queue;
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
        double[] data = getInternalState().getData();

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

    double[] force = new double[2];

    @Override
    protected State simulate(State state, double time, double delta)
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
                    x = Math.cos(angle)/100000;
                    y = Math.sin(angle)/100000;
                    distance = 0.00001;

                    angle += 0.5;
                }
                else
                {
                    distance = Math.sqrt(x * x + y * y);
                }

                int targetDistance = nodeDistance[j];

                double force = forces( i, j, distance, targetDistance, time);

                if (force != 0)
                {
                    double factorToLength = (force * delta) / distance;
                    x *= factorToLength;
                    y *= factorToLength;

                    // force
                    derivativeXY[componentIndexJ] += x;
                    derivativeXY[componentIndexJ + 1] += y;

                    // counter-force
                    derivativeXY[componentIndexI] -= x;
                    derivativeXY[componentIndexI + 1] -= y;
                }
            }
        }

        return derivative;
    }

    private FallOffHelper fallOffHelper = new FallOffHelper();

    /**
     * The actual forces calculation.
     *
     * @param nodeIndexA        Node index of first node
     * @param nodeIndexB        Node index of second node
     * @param distance          Distance between nodes
     * @param targetDistance    target distance according to distance calculation
     * @param time              current time (will be a multiple of {@link GraphLayoutConfig#getStepSize()})
     * @return force to apply to both nodes
     */
    protected double forces(int nodeIndexA, int nodeIndexB, double distance, int targetDistance, double time)
    {
        double force = 0;
        if (targetDistance < Integer.MAX_VALUE)
        {
            double factor = fallOffHelper.fallOff(this.config.getSpringFallOff(), distance);
            force += -(config.getSpringConstant() * factor) * (distance - targetDistance);
        }

        if (time > pushStartTime)
        {
            double factor = fallOffHelper.fallOff(this.config.getRepulsionFallOff(), distance);
            force += this.config.getRepulsionForce() * factor;
        }
        return force;
    }
}

