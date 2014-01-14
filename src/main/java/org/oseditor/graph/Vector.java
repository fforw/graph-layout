package org.oseditor.graph;

public final class Vector
{
    private double x;
    private double y;

    public Vector()
    {
        this(0, 0);
    }

    public Vector(double v, double v1)
    {
        this.x = x;
        this.y = y;
    }

    public Vector add(double v, double v1)
    {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector add(Vector that)
    {
        this.x += that.x;
        this.y += that.y;
        return this;
    }

    public Vector subtract(double v, double v1)
    {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vector subtract(Vector that)
    {
        this.x -= that.x;
        this.y -= that.y;
        return this;
    }

    public double length()
    {
        return Math.sqrt(x*x+y*y);
    }

    public Vector norm()
    {
        return norm(1);
    }

    public Vector norm(double newLength)
    {
        return scale(newLength/length());
    }

    private Vector scale(double s)
    {
        this.x *= s;
        this.y *= s;
        return this;
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
}
