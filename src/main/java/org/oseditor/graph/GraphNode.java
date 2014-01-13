package org.oseditor.graph;

import org.svenson.DynamicProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Node in the graph definition structure.
 *
 * @param <T> optional type of user data to store.
 */
public class GraphNode<T> implements DynamicProperties
{
    private T data;
    private  String id;

    private double x, y, width, height;
    private HashMap<String, Object> props;

    public GraphNode(String id, double width, double height)
    {
        this(id,width,height, null);
    }
    public GraphNode(String id, double width, double height, T userData)

    {
        this();

        this.id = id;
        this.width = width;
        this.height = height;
        this.data = userData;
    }

    public GraphNode()
    {

    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }

    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public double getWidth()
    {
        return width;
    }

    public void setWidth(double width)
    {
        this.width = width;
    }

    public double getHeight()
    {
        return height;
    }

    public void setHeight(double height)
    {
        this.height = height;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    @Override
    public void setProperty(String name, Object value)
    {
        if (props == null)
        {
            props = new HashMap<String, Object>();
        }

        props.put(name,value);
    }

    @Override
    public Object getProperty(String name)
    {
        if (props == null)
        {
            return null;
        }

        return props.get(name);
    }

    @Override
    public Set<String> propertyNames()
    {
        if (props == null)
        {
            return Collections.emptySet();
        }
        return props.keySet();
    }

    @Override
    public boolean hasProperty(String name)
    {
        if (props == null)
        {
            return false;
        }
        return props.containsKey(name);
    }

    @Override
    public Object removeProperty(String name)
    {
        if (props == null)
        {
            return null;
        }
        return props.remove(name);
    }
}
