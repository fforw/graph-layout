package org.oseditor.graph;

/**
 * Configuration for a {@link GraphLayout}.
 */
public interface GraphLayoutConfig
{
    int getIterations();

    double getStepSize();

    double getSpringConstant();

    double getRepulsionForce();

    double getRepulsionStart();

    long getInitialSeed();

    DistanceFunction getDistanceFunction();

    FallOffCurveDefinition getSpringFallOff();

    FallOffCurveDefinition getRepulsionFallOff();

    double getDistanceFactor();
}
