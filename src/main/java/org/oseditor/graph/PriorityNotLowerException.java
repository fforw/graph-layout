package org.oseditor.graph;

public class PriorityNotLowerException
    extends EditorRuntimeException
{
    private static final long serialVersionUID = 515155340721049687L;

    public PriorityNotLowerException(int priority, int notLower)
    {
        super(notLower + " is not lower than " + priority);
    }
}
