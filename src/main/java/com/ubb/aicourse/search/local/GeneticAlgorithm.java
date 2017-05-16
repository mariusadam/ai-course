package com.ubb.aicourse.search.local;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Marius Adam
 */
public abstract class GeneticAlgorithm<T> {
    private Metrics metrics;
    private int chromosomeLength;
    protected Random random;
    private StopFunction<T> stopFn;
    protected Function<Chromosome<T>, Double> fitnessFn;
    protected Function<List<Chromosome<T>>, Chromosome<T>> selectionFn;
    protected BiFunction<Chromosome<T>, Chromosome<T>, Chromosome<T>> crossoverFn;
    protected Function<Chromosome<T>, Chromosome<T>> mutationFn;
    private ArrayList<ProgressTracer<T>> progressTracers;
    protected double mutationProbability;

    public GeneticAlgorithm() {
        random = GaUtils.random;
        progressTracers = new ArrayList<>();
        metrics = new Metrics();
        stopFn = GaUtils.simpleStopFn();
        selectionFn = GaUtils.rouletteWheelSelection();
    }

    public void addProgressTracer(ProgressTracer<T> tracer) {
        progressTracers.add(tracer);
    }

    /**
     * @param initialPopulation   The initial population
     * @param maxIteration        The algorithm stops after the specified number of iterations
     *                            Only used if > 0L
     * @return the best/fittest chromosome found so far
     */
    public Chromosome<T> search(List<Chromosome<T>> initialPopulation, long maxIteration) {
        return search(initialPopulation, maxIteration, 0L);
    }

    /**
     *
     * @param initialPopulation   The initial population
     * @param maxIterations       Starts the genetic algorithm and stops after a specified number of
     *                            iterations. Only used if > 0L
     * @param maxTimeMilliseconds The maximum time in milliseconds that the algorithm is to run
     *                            for (approximate). Only used if > 0L.
     * @return the best/fittest chromosome found so far
     */
    public Chromosome<T> search(List<Chromosome<T>> initialPopulation,
            long maxIterations, long maxTimeMilliseconds) {
        Chromosome<T> bestChromosome;

        // Create a local copy of the population to work with
        List<Chromosome<T>> population = new ArrayList<>(initialPopulation);
        // Validate the population and setup the instrumentation
        validatePopulation(population);

        metrics.clear();
        metrics.set(Metrics.Keys.MaxIterations, maxIterations);
        metrics.set(Metrics.Keys.MaxExecutionTime, maxTimeMilliseconds);
        updateMetrics(0L, 0L);
        notifyProgressTracers();

        long startTime = System.currentTimeMillis();
        long itCount = 0;
        do {
            population = nextGeneration(population);
            bestChromosome = retrieveBestChromosome(population);

            updateMetrics(++itCount, System.currentTimeMillis() - startTime);
            notifyProgressTracers();
        } while (!stopFn.isSatisfiedBy(
                population,
                bestChromosome,
                metrics
        ));

        notifyProgressTracers();
        // return the best individual in population, according to FITNESS-FN
        return bestChromosome;
    }

    abstract protected List<Chromosome<T>> nextGeneration(List<Chromosome<T>> population);

    private Chromosome<T> retrieveBestChromosome(List<Chromosome<T>> population) {
        return new Chromosome<T>(new ArrayList<>());
    }

    private void updateMetrics(long itCount, long time) {
        metrics.set(Metrics.Keys.CurrentIteration, itCount);
        metrics.set(Metrics.Keys.ElapsedTime, time);
    }

    private void notifyProgressTracers() {
        for (ProgressTracer<T> tracer : progressTracers) {
            tracer.traceProgress(metrics);
        }
    }

    protected void validatePopulation(Collection<Chromosome<T>> population) {
        // Require at least 1 chromosome in population in order
        // for algorithm to work
        if (population.size() < 1) {
            throw new IllegalArgumentException("Must start with at least a population of size 1");
        }
        // String lengths are assumed to be of fixed size,
        // therefore ensure initial populations lengths correspond to this
        for (Chromosome<T> chromosome : population) {
            if (chromosome.length() != this.chromosomeLength) {
                throw new IllegalArgumentException("chromosome [" + chromosome
                        + "] in population is not the required length of " + this.chromosomeLength);
            }
        }
    }

    public void setChromosomeLength(int chromosomeLength) {
        this.chromosomeLength = chromosomeLength;
        crossoverFn = GaUtils.singleCutCrossOver(chromosomeLength);
    }

    public int getChromosomeLength() {
        return chromosomeLength;
    }

    public double getMutationProbability() {
        return mutationProbability;
    }

    public interface ProgressTracer<T> {
        void traceProgress(Metrics metrics);
    }
}
