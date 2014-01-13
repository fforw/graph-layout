package org.oseditor.graph;

public class NodeNotFoundException
    extends EditorRuntimeException
{
    private static final long serialVersionUID = -8376282757052980559L;

    public NodeNotFoundException(String message)
    {
        super(message);
    }
}
