package org.oseditor.graph;

public class EditorRuntimeException
    extends RuntimeException
{
    public EditorRuntimeException(String message)
    {
        super(message);
    }

    public EditorRuntimeException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public EditorRuntimeException(Throwable cause)
    {
        super(cause);
    }

    public EditorRuntimeException(String message, Throwable cause, boolean enableSuppression,
                                  boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
