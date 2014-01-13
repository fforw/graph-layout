package org.oseditor.graph;

import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains the basic definition of a directed graph.
 *
 * The {@link GraphLayout} can update this data structure.
 *
 * @see GraphLayoutBuilder
 * @see GraphLayout
 */
public class DirectedGraph
{
    private Map<String, GraphNode> nodes;
    private List<Edge> edges;

    public Map<String, GraphNode> getNodes()
    {
        return nodes;
    }

    @JSONTypeHint(GraphNode.class)
    public void setNodes(Map<String, GraphNode> nodes)
    {
        this.nodes = nodes;
    }

    public List<Edge> getEdges()
    {
        return edges;
    }

    public void addNode(GraphNode node)
    {
        if (nodes == null)
        {
            nodes = new HashMap<String, GraphNode>();
        }

        this.nodes.put(node.getId(), node);
    }

    public void addEdge(Edge edge)
    {
        if (edges == null)
        {
            edges = new ArrayList<Edge>();
        }

        this.edges.add(edge);
    }

    @JSONProperty(priority = -1)
    @JSONTypeHint(Edge.class)
    public void setEdges(List<Edge> edges)
    {
        this.edges = edges;
    }
}
