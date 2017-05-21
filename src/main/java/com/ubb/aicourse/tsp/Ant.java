package com.ubb.aicourse.tsp;

/**
 * @author Marius Adam
 */
public class Ant {
    private int[] permutation;
    private double cost;

    public int[] getPermutation() {
        return permutation;
    }

    public void setPermutation(int[] permutation) {
        this.permutation = permutation;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
