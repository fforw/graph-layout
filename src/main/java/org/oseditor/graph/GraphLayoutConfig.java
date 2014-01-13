package org.oseditor.graph;

/**
 * Configuration for a {@link GraphLayout}.
 */
public interface GraphLayoutConfig
{
    int getIterations();

    double getStepSize();

    double getSpringConstant();

    int getInitialSeed();

    DistanceFunction getDistanceFunction();

    double getMinDistance();

    double getMinForce();
}
