package org.ubb.courses.ai.search;

import java.util.*;

/**
 * @author Marius Adam
 */
public class GreedyBestFirstSearch extends InformedSearch {
    @Override
    public State solve(State initialState) {
        Queue<State> states = new PriorityQueue<>(
                Comparator.comparing(this::computeCost).reversed()
        );
        List<State> visitedStates = new ArrayList<>();
        State solution = null;

        states.add(initialState);
        while (!states.isEmpty()) {
            State current = states.remove();
//            states.clear();
            if (visitedStates.contains(current)) {
                continue;
            }
            visitedStates.add(current);

            if (current.isSolution(initialState)) {
                solution = current;
                break;
            }

            states.addAll(current.getSuccessors());
        }

        return solution;
    }
}
