package org.oseditor.graph;

/**
 * Represents an edge in a usually directed graph.
 *
 */
public class Edge
{
    private String  from, to;

    public Edge()
    {
        this("","");
    }

    public Edge(String from, String to)
    {
        if (from == null)
        {
            throw new IllegalArgumentException("from can't be null");
        }

        if (to == null)
        {
            throw new IllegalArgumentException("to can't be null");
        }

        this.from = from;
        this.to = to;
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public String getTo()
    {
        return to;
    }

    public void setTo(String to)
    {
        this.to = to;
    }

    @Override
    public int hashCode()
    {
        return 37 + from.hashCode() + to.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (obj instanceof Edge)
        {
            Edge that = (Edge)obj;
            return
                ( from.equals(that.from) && to.equals(that.to)) ||
                ( from.equals(that.to) && to.equals(that.from));
        }
        return false;
    }
}
