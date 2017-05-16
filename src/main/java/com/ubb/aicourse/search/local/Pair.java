package com.ubb.aicourse.search.local;

/**
 * @author Marius Adam
 */
public class Pair<T> {
    private Chromosome<T> firstChild;

    private Chromosome<T> secondChild;

    public Pair(Chromosome<T> firstChild, Chromosome<T> secondChild) {
        this.firstChild = firstChild;
        this.secondChild = secondChild;
    }

    public Chromosome<T> getFirstChild() {
        return firstChild;
    }

    public Chromosome<T> getSecondChild() {
        return secondChild;
    }
}
