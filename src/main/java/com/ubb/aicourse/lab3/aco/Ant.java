package com.ubb.aicourse.lab3.aco;

/**
 * @author Marius Adam
 */
public class Ant {
    private int[]  permutation;

    public Ant(int[] permutation, double fitness) {
        this.permutation = permutation;
        this.fitness = fitness;
    }

    private double fitness;

    public int[] getPermutation() {
        return permutation;
    }

    public void setPermutation(int[] permutation) {
        this.permutation = permutation;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

}
