package com.ubb.aicourse.lab3.search.local;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marius Adam
 */
public class GenerationalGenAlg<T> extends GeneticAlgorithm<T> {
    public GenerationalGenAlg() {
        super();
    }

    @Override
    protected List<Chromosome<T>> nextGeneration(List<Chromosome<T>> population) {
        // new_population <- empty set
        List<Chromosome<T>> newPopulation = new ArrayList<>(population.size());

        // regenerate only half of the population
        // this could also be parameterized
        for (int i = 0; i < population.size(); i++) {
            Chromosome<T> x = selectionFn.apply(population);
            Chromosome<T> y = selectionFn.apply(population);
            // child <- REPRODUCE(x, y)
            Chromosome<T> child = crossoverFn.apply(x, y);
            // if (small random probability) then child <- MUTATE(child)
            if (random.nextDouble() <= mutationProbability) {
                child = mutationFn.apply(child);
            }
            // add child to new_population
            newPopulation.add(child);
        }

        return newPopulation;
    }

    @Override
    public String toString() {
        return "Generational evolutive algorithm";
    }
}
