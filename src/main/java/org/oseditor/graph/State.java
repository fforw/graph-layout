package org.oseditor.graph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encapsulates the internal node state or a derivative of it.
 */
public final class State
    implements Cloneable
{
    private static Logger log = LoggerFactory.getLogger(State.class);


    private double[] data;

    public State(int size)
    {
        data = new double[size * 2];
    }

    public State add(State that)
    {
        for (int i=0; i < data.length; i++)
        {
            data[i] += that.data[i];
        }
        return this;
    }

    public State scale(double s)
    {
        for (int i=0; i < data.length; i++)
        {
            data[i] *= s;
        }
        return this;
    }

    public State clone()
    {
        try
        {
            State clone = (State) super.clone();
            int len = this.data.length;
            clone.data = new double[len];
            System.arraycopy(this.data, 0, clone.data, 0, len);

            return clone;

        }
        catch (CloneNotSupportedException e)
        {
            throw new EditorRuntimeException(e);
        }

    }

    public int size()
    {
        return data.length / 2;
    }

    public double[] getData()
    {
        return data;
    }

    @Override
    public String toString()
    {
        int count = data.length;
        String cut = "";
        if (count > 50)
        {
            count = 50;
            cut = " ...";
        }
        StringBuilder sb = new StringBuilder();
        for (int i=0; i < count; i+=2)
        {
            sb.append(data[i]);
            sb.append(",");
            sb.append(data[i+1]);
            sb.append(" ");
        }

        return super.toString() + ": " + sb.toString() + cut + " ( " + data.length + " )" ;
    }
}
