package org.oseditor.graph;


public class QueueElementNotFound
    extends EditorRuntimeException
{
    public QueueElementNotFound(int priority, int value)
    {
        super("No combination of value " + value + " and priority " + priority + " found in queue.");
    }
}
