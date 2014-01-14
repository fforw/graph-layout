package org.oseditor.graph;

public class FallOffHelper
{
    private Vector o0 = new Vector();
    private Vector o1 = new Vector();
    private Vector o2 = new Vector();

    private final static double X0 = 0;
    private final static double X1 = 1;
    private final static double X3 = 0;

    public double fallOff(FallOffCurveDefinition def, double current)
    {
        double max = def.getMax();
        double minValue = def.getMinValue();
        if (current > max)
        {
            return minValue;
        }

        current = current / max;

        double x1 = def.getX1();
        double y1 = def.getY1();
        double x2 = def.getX2();
        double y2 = def.getY2();

        double y3 = minValue;

        Vector a = bezierPoint(o0, X0, X0, x1, y1, current);
        Vector b = bezierPoint(o1, x1, y1, x2, y2, current);

        Vector c = bezierPoint(o2, x2, y2, X3, y3, current);

        Vector ab = bezierPoint(o0, a, b, current);
        Vector bc = bezierPoint(o1, b, c, current);

        double result = bezierPoint(o0, ab, bc, current).getY();

        if (result < 0)
            return 0;
        if (result > 1)
            return 1;
        return result;
    }

    private Vector bezierPoint(Vector vector, double x0, double y0, double x1, double y1, double t)
    {
        double x = x0 + (x1 - x0) * t;
        double y = y0 + (y1 - y0) * t;

        vector.setX(x);
        vector.setY(y);

        return vector;
    }

    private Vector bezierPoint(Vector vector, Vector v0, Vector v1, double t)
    {
        double x0 = v0.getX();
        double y0 = v0.getY();
        double x = x0 + (v1.getX() - x0) * t;
        double y = y0 + (v1.getY() - y0) * t;

        vector.setX(x);
        vector.setY(y);

        return vector;
    }
}
