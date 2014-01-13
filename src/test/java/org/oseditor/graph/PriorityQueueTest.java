package org.oseditor.graph;

import org.junit.Test;
import org.oseditor.graph.PriorityQueue;
import org.oseditor.graph.QueueElementNotFound;
import org.oseditor.graph.QueueEmptyException;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class PriorityQueueTest
{
    @Test
    public void thatQueueOperationsWork()
    {
        PriorityQueue pq = createTestQueue();

        assertThat(pq.size(), is(5));
        assertThat(state(pq), is("ABCDE"));

        assertThat(pq.popMinimalValue(), is((int)'A'));
        assertThat(pq.size(), is(4));
        assertThat(state(pq), is("BCDE"));

        pq.lowerPriority((int)'D', 4, -1);

        assertThat(pq.size(), is(4));
        assertThat(state(pq), is("DBCE"));
        pq.lowerPriority((int) 'E', 5, 0);

        assertThat(state(pq), is("DEBC"));
    }

    private PriorityQueue createTestQueue()
    {
        PriorityQueue pq = new PriorityQueue(5);
        assertThat(pq.size(), is(0));
        assertThat(pq.isEmpty(), is(true));

        pq.add(3, 'C');
        pq.add(1, 'A');
        pq.add(2, 'B');
        pq.add(5, 'E');
        pq.add(4, 'D');
        return pq;
    }

    @Test(expected = QueueEmptyException.class)
    public void thatEmptyQueuesDoNotPop()
    {
        new PriorityQueue(1).popMinimalValue();
    }

    @Test(expected = QueueElementNotFound.class)
    public void thatWrongLowerDoesNotWork()
    {
        createTestQueue().lowerPriority(100, 10000, 9999);
    }

    @Test(expected = QueueElementNotFound.class)
    public void lowerPriorityDoesNotAcceptHigher()
    {
        PriorityQueue testQueue = createTestQueue();
        testQueue.lowerPriority((int) 'E', 2, 1);
    }

    private String state(PriorityQueue pq)
    {
        StringBuilder sb = new StringBuilder();

        for (int i=0; i < pq.size(); i++)
        {
            sb.append((char)pq.getValue(i));
        }

        return sb.toString();
    }

}
