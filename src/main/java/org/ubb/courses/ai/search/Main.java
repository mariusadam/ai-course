package org.ubb.courses.ai.search;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Marius Adam
 */
public class Main extends RuntimeException {
    public static void main(String[] args) {

        SearchMethod searchMethod = new GreedyBestFirstSearch();
        Set<Individual> left = new HashSet<>();
        left.add(new Cannibal("c1"));
        left.add(new Cannibal("c2"));
        left.add(new Cannibal("c3"));
        left.add(new Missionary("m1"));
        left.add(new Missionary("m2"));
        left.add(new Missionary("m3"));

        State initial = new StateImpl(
                BoatPosition.LEFT_SHORE,
                left,
                new HashSet<>(),
                null,
                2
        );

        printState(searchMethod.solve(initial));
    }

    private static void printState(State state) {
        if (state == null) {
            return;
        }

        printState(state.getParent());
        System.out.println(state);
    }
}
