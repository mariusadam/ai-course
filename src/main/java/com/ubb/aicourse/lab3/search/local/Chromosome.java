package com.ubb.aicourse.lab3.search.local;

import java.util.Collections;
import java.util.List;

/**
 * @author Marius Adam
 */
public class Chromosome<T> {
    private List<T> representation;

    private double fitness;
    private boolean evaluated;

    public Chromosome(List<T> representation) {
        this.representation = Collections.unmodifiableList(representation);
        evaluated = false;
    }

    public List<T> getRepresentation() {
        return representation;
    }

    public int length() {
        return representation.size();
    }

    public void setFitness(double fitness) {
        if (isEvaluated()) {
            throw new RuntimeException("Fitness already set.");
        }
        evaluated = true;
        this.fitness = fitness;
    }

    public Double getFitness() {
        return fitness;
    }

    public boolean isEvaluated() {
        return evaluated;
    }
}
