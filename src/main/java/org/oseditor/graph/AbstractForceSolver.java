package org.oseditor.graph;

/**
 * Base class for classes that need to solve a conflicting set of forces on simple
 * position objects.
 *
 */
public abstract class AbstractForceSolver
{
    final int nodeCount;
    private final double delta;
    private double time;
    double angle = 0;
    private State state;

    public AbstractForceSolver( int nodeCount, double stepSize)
    {
        this.nodeCount = nodeCount;
        this.delta = stepSize;
        state = new State(nodeCount);
        time = 0;
    }

    protected State evaluate(State initialState, State derivative, double time, double delta)
    {
        State state = derivative.clone().scale(delta).add(initialState);

        return this.simulate(state, time, delta);
    }

    /**
     * Do one RK4 simulation step. Step size is defined by
     * the "stepSize" option.
     */
    public void simulate()
    {
        State nd = new State(nodeCount);
        State a = this.evaluate(state, nd, time, 0);
        State b = this.evaluate(state,  a, time, delta * 0.5);
        State c = this.evaluate(state,  b, time, delta * 0.5);
        State d = this.evaluate(state,  c, time, delta);

        b.add(c).scale(2);
        d.add(a).add(b).scale(1.0/6);

        this.state.add(d.scale(delta));

        time += delta;
    }

    protected abstract State simulate(State state, double time, double delta);

    /**
     * Exposes the internal simulation state. Normally you should not touch this unless you want
     * to animate the intermediary animation steps.
     *
     * @return
     */
    public State getInternalState()
    {
        return state;
    }
}
