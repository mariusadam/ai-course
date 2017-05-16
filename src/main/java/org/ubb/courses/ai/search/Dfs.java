package org.ubb.courses.ai.search;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marius Adam
 */
public class Dfs implements SearchMethod {
    private List<State> visited;
    private State initialState;
    private State solution;

    @Override
    public State solve(State initialState) {
        visited = new ArrayList<>();
        this.initialState = initialState;
        solution = null;
        solveRecursive(initialState);
        return solution;
    }

    private void solveRecursive(State current) {
        if (current.isSolution(initialState)) {
            solution = current;
        }
//        System.out.println(current);
        visited.add(current);
        for (State state : current.getSuccessors()) {
            if (!visited.contains(state) && state.isValid()) {
                solveRecursive(state);
            }
        }
    }
}
