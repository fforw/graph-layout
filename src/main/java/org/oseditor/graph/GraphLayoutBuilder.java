package org.oseditor.graph;

/**
 * Builds and configures {@link GraphLayout}s.
 */
public class GraphLayoutBuilder
    implements GraphLayoutConfig
{
    private int iterations = 500;

    private double repulsionStart = 0.85;

    private double stepSize = 1/60.0;

    private double springConstant = 750;

    private long initialSeed =14121970;

    private DistanceFunction distanceFunction;

    private double repulsionForce = 3000;

    private FallOffCurveDefinition springFallOff;

    private FallOffCurveDefinition repulsionFallOff;

    private double distanceFactor = 1;

    @Override
    public int getIterations()
    {
        return iterations;
    }

    public GraphLayoutConfig withIterations(int iterations)
    {
        this.iterations = iterations;
        return this;
    }

    @Override
    public double getStepSize()
    {
        return stepSize;
    }

    public GraphLayoutConfig withStepSize(double stepSize)
    {
        this.stepSize = stepSize;
        return this;
    }

    @Override
    public double getSpringConstant()
    {
        return springConstant;
    }

    @Override
    public double getRepulsionForce()
    {
        return repulsionForce;
    }

    @Override
    public double getRepulsionStart()
    {
        return repulsionStart;
    }

    public GraphLayoutConfig withSpringConstant(double springConstant)
    {
        this.springConstant = springConstant;
        return this;
    }

    @Override
    public long getInitialSeed()
    {
        return initialSeed;
    }

    public GraphLayoutConfig withInitialSeed(long initialSeed)
    {
        this.initialSeed = initialSeed;
        return this;
    }

    public DistanceFunction getDistanceFunction()
    {
        return distanceFunction;
    }

    @Override
    public FallOffCurveDefinition getSpringFallOff()
    {
        return springFallOff;
    }

    @Override
    public FallOffCurveDefinition getRepulsionFallOff()
    {
        return repulsionFallOff;
    }

    @Override
    public double getDistanceFactor()
    {
        return distanceFactor;
    }

    public GraphLayoutBuilder withDistanceFunction(DistanceFunction distanceFunction)
    {
        this.distanceFunction = distanceFunction;
        return this;
    }

    public GraphLayoutBuilder withRepulsionStart(double repulsionStart)
    {
        this.repulsionStart = repulsionStart;
        return this;
    }

    public GraphLayoutBuilder withSpringFallOff(FallOffCurveDefinition springFallOff)
    {
        this.springFallOff = springFallOff;
        return this;
    }

    public GraphLayoutBuilder withSpringFallOff(double x1,double y1,double x2,double y2,double minValue, double max)
    {
        return withSpringFallOff(new FallOffCurveDefinition(x1,y1,x2,y2,minValue, max));
    }

    public GraphLayoutBuilder withRepulsionFallOff(FallOffCurveDefinition repulsionFallOff)
    {
        this.repulsionFallOff = repulsionFallOff;
        return this;
    }

    public GraphLayoutBuilder withRepulsionFallOff(double x1,double y1,double x2,double y2,double minValue, double max)
    {
        return withRepulsionFallOff(new FallOffCurveDefinition(x1,y1,x2,y2,minValue, max));
    }

    public GraphLayoutBuilder withRepulsionForce(double repulsionForce)
    {
        this.repulsionForce = repulsionForce;
        return this;
    }

    public GraphLayoutBuilder withRepulsionFallOffDistance(double repulsionDistance)
    {
        this.repulsionFallOff = new FallOffCurveDefinition(0.5,1, 0.75, 0.01, 0, repulsionDistance);
        return this;
    }

    public GraphLayoutBuilder withSpringFallOffDistance(double springDistance)
    {
        this.springFallOff = new FallOffCurveDefinition(0.5, 1, 0.7, 0.5, 0.05, springDistance);
        return this;
    }

    public GraphLayoutBuilder withDistanceFactor(double distanceFactor)
    {
        this.distanceFactor = distanceFactor;

        return this;
    }

    public GraphLayout buildFor(DirectedGraph graph)
    {

        return new GraphLayout(graph, this);
    }
}
