package org.oseditor.graph;


public class QueueEmptyException
    extends EditorRuntimeException
{
    private static final long serialVersionUID = 5692561109088975136L;

    public QueueEmptyException()
    {
        super("Queue is empty");
    }
}
