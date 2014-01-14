package org.oseditor.graph;

import org.junit.Test;
import org.svenson.JSONParser;

import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class GraphLayoutTest
{
    // same graph as media/test-graph.svg
    private static final String TEST_GRAPH_JSON = "{\"nodes\":{\"A\":{\"id\":\"A\",\"width\":20,\"height\":20," +
        "\"color\":\"#00f\"},\"B\":{\"id\":\"B\",\"width\":20,\"height\":20,\"color\":\"#0f0\"},\"C\":{\"id\":\"C\"," +
        "\"width\":20,\"height\":20,\"color\":\"#f00\"},\"D\":{\"id\":\"D\",\"width\":30,\"height\":30," +
        "\"color\":\"#f0f\"},\"E\":{\"id\":\"E\",\"width\":20,\"height\":20,\"color\":\"#ff0\"},\"F\":{\"id\":\"F\"," +
        "\"width\":20,\"height\":20,\"color\":\"#0ff\"},\"G\":{\"id\":\"G\",\"width\":20,\"height\":20," +
        "\"color\":\"#333\"},\"H\":{\"id\":\"H\",\"width\":20,\"height\":20,\"color\":\"#666\"},\"I\":{\"id\":\"I\"," +
        "\"width\":20,\"height\":20,\"color\":\"#999\"},\"J\":{\"id\":\"J\",\"width\":20,\"height\":20," +
        "\"color\":\"#ccc\"},\"K\":{\"id\":\"K\",\"width\":20,\"height\":20,\"color\":\"#080\"}}," +
        "\"edges\":[{\"from\":\"A\",\"to\":\"B\"},{\"from\":\"A\",\"to\":\"C\"},{\"from\":\"A\",\"to\":\"D\"}," +
        "{\"from\":\"B\",\"to\":\"D\"},{\"from\":\"B\",\"to\":\"F\"},{\"from\":\"D\",\"to\":\"F\"},{\"from\":\"D\"," +
        "\"to\":\"C\"},{\"from\":\"D\",\"to\":\"G\"},{\"from\":\"C\",\"to\":\"E\"},{\"from\":\"F\",\"to\":\"G\"}," +
        "{\"from\":\"G\",\"to\":\"H\"},{\"from\":\"H\",\"to\":\"I\"},{\"from\":\"I\",\"to\":\"J\"},{\"from\":\"I\"," +
        "\"to\":\"K\"},{\"from\":\"J\",\"to\":\"K\"}]}";

    @Test
    public void testFindDistances() throws Exception
    {
        DirectedGraph graphData = JSONParser.defaultJSONParser().parse(DirectedGraph.class, TEST_GRAPH_JSON);

        GraphLayout layout = new GraphLayoutBuilder()
            .withDistanceFunction(new DistanceFunction()
            {
                @Override
                public int getDistance(GraphNode a, GraphNode b, List<Integer> edgeIndexesA, List<Integer> edgeIndexesB)
                {
                    return 1;
                }
            }).buildFor(graphData);

        int[] distance = layout.distances[0];

        assertThat("Distance from A to A is 0", distance[0], is(0));
        assertThat("Distance from A to E is 2", distance[4], is(2));
        assertThat("Distance from A to K is 5", distance[10], is(5));
        assertThat("Distance from A to G is 2", distance[6], is(2));

        distance = layout.distances[4];
        assertThat("Distance from E to E is 0", distance[4], is(0));
        assertThat("Distance from E to A is 2", distance[0], is(2));
        assertThat("Distance from E to K is 6", distance[10], is(6));
        assertThat("Distance from E to F is 3", distance[5], is(3));

        assertThat("All Integer.MAX resolved", layout.areAllNodesConnected(), is(true));
    }
}
