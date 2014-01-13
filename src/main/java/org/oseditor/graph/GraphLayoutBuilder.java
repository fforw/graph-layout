package org.oseditor.graph;

/**
 * Builds and configures {@link GraphLayout}s.
 */
public class GraphLayoutBuilder
    implements GraphLayoutConfig
{
    private int iterations = 1000;

    private double stepSize = 1/100.0;

    private double springConstant = 100;

    private int initialSeed =14121970;

    private DistanceFunction distanceFunction;

    private double minDistance = 0;

    private double minForce = springConstant / 2;

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

    public GraphLayoutConfig withSpringConstant(double springConstant)
    {
        this.springConstant = springConstant;
        return this;
    }

    @Override
    public int getInitialSeed()
    {
        return initialSeed;
    }

    public GraphLayoutConfig withInitialSeed(int initialSeed)
    {
        this.initialSeed = initialSeed;
        return this;
    }

    public DistanceFunction getDistanceFunction()
    {
        return distanceFunction;
    }

    @Override
    public double getMinDistance()
    {
        return minDistance;
    }

    @Override
    public double getMinForce()
    {
        return minForce;
    }

    public GraphLayoutBuilder withMinDistance(double minDistance)
    {
        this.minDistance = minDistance;
        return this;
    }

    public GraphLayoutBuilder withMinForce(double minForce)
    {
        this.minForce = minForce;
        return this;
    }

    public GraphLayoutBuilder withDistanceFunction(DistanceFunction distanceFunction)
    {
        this.distanceFunction = distanceFunction;
        return this;
    }

    public GraphLayout buildFor(DirectedGraph graph)
    {
        return new GraphLayout(graph, this);
    }
}
