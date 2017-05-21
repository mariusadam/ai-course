package com.ubb.aicourse.lab3.search.local;

import java.util.List;

/**
 * @author Marius Adam
 */
@FunctionalInterface
public interface StopFunction<T> {
    boolean isSatisfiedBy(
            List<Chromosome<T>> currentPopulation,
            Chromosome<T> bestSoFar,
            Metrics metrics
    );
}
