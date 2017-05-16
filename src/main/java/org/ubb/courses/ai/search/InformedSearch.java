package org.ubb.courses.ai.search;

/**
 * @author Marius Adam
 */
public abstract class InformedSearch implements SearchMethod {
    protected Integer computeCost(State state) {
        return state.priority();
    }
}
