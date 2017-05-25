package com.ubb.aicourse.lab3.search.local;

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
    protected Random                                                  random;
    protected Function<Chromosome<T>, Double>                         fitnessFn;
    protected Function<List<Chromosome<T>>, Chromosome<T>>            selectionFn;
    protected BiFunction<Chromosome<T>, Chromosome<T>, Chromosome<T>> crossoverFn;
    protected Function<Chromosome<T>, Chromosome<T>>                  mutationFn;
    protected double                                                  mutationProbability;
    private   Metrics                                                 metrics;
    private   int                                                     chromosomeLength;
    private   StopFunction<T>                                         stopFn;
    private   ArrayList<ProgressTracer<T>>                            progressTracers;
    private   List<Chromosome<T>>                                     initialPopulation;
    private   long                                                    maxIterations;
    private long maxTimeMilliseconds;

    public GeneticAlgorithm() {
        random = GaUtils.random;
        progressTracers = new ArrayList<>();
        metrics = new Metrics();
        stopFn = GaUtils.simpleStopFn();
        selectionFn = GaUtils.rouletteWheelSelection();
    }

    public void setInitialPopulation(List<Chromosome<T>> initialPopulation) {
        validatePopulation(initialPopulation);
        this.initialPopulation = initialPopulation;
    }

    public void setMaxIterations(long maxIterations) {
        this.maxIterations = maxIterations;
    }

    public void setMaxTimeMilliseconds(long maxTimeMilliseconds) {
        this.maxTimeMilliseconds = maxTimeMilliseconds;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public void setSelectionFn(Function<List<Chromosome<T>>, Chromosome<T>> selectionFn) {
        this.selectionFn = selectionFn;
    }

    public void setCrossoverFn(BiFunction<Chromosome<T>, Chromosome<T>, Chromosome<T>> crossoverFn) {
        this.crossoverFn = crossoverFn;
    }

    public void setMutationFn(Function<Chromosome<T>, Chromosome<T>> mutationFn) {
        this.mutationFn = mutationFn;
    }

    public void setStopFn(StopFunction<T> stopFn) {
        this.stopFn = stopFn;
    }

    public void setProgressTracers(ArrayList<ProgressTracer<T>> progressTracers) {
        this.progressTracers = progressTracers;
    }

    public void addProgressTracer(ProgressTracer<T> tracer) {
        progressTracers.add(tracer);
    }

    /**
     * @return the best/fittest chromosome found so far
     */
    public Chromosome<T> search() {
        return doSearch();
    }


    private Chromosome<T> doSearch() {
        Chromosome<T> bestChromosome = null, temp;

        // Create a local copy of the population to work with
        List<Chromosome<T>> population = new ArrayList<>(initialPopulation);

        metrics.clear();
        metrics.set(Metrics.Keys.MaxIterations, maxIterations);
        metrics.set(Metrics.Keys.MaxExecutionTime, maxTimeMilliseconds);
        updateMetrics(0L, 0L);
        notifyProgressTracers();

        population.forEach(chromosome -> chromosome.setFitness(fitnessFn.apply(chromosome)));
        long startTime = System.currentTimeMillis();
        long itCount = 0;
        do {
            population = nextGeneration(population);
            population.forEach(chromosome -> chromosome.setFitness(fitnessFn.apply(chromosome)));
            temp = retrieveBestChromosome(population);
            if (bestChromosome == null || bestChromosome.getFitness() > temp.getFitness()) {
                bestChromosome = temp;
                metrics.set(Metrics.Keys.BestChromosomeIteration, itCount);
                metrics.set(Metrics.Keys.BestChromosomeTime, System.currentTimeMillis() - startTime);
            }

            updateMetrics(++itCount, System.currentTimeMillis() - startTime);
            notifyProgressTracers();
        } while (!stopFn.isSatisfiedBy(population, bestChromosome, metrics));
        metrics.set(Metrics.Keys.TotalExecutionTime, System.currentTimeMillis() - startTime);
        metrics.set(Metrics.Keys.TotalIterations, itCount - 1);
        notifyProgressTracers();
        // return the best individual in population, according to FITNESS-FN
        return bestChromosome;
    }

    public void setFitnessFn(Function<Chromosome<T>, Double> fitnessFn) {
        this.fitnessFn = fitnessFn;
    }

    public int getChromosomeLength() {
        return chromosomeLength;
    }

    public void setChromosomeLength(int chromosomeLength) {
        this.chromosomeLength = chromosomeLength;
        crossoverFn = GaUtils.singleCutCrossOver(chromosomeLength);
    }

    public double getMutationProbability() {
        return mutationProbability;
    }

    public void setMutationProbability(double mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }

    abstract protected List<Chromosome<T>> nextGeneration(List<Chromosome<T>> population);

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
                throw new IllegalArgumentException("chromosome [" + chromosome + "] in population is not the required length of " + this.chromosomeLength);
            }
        }
    }

    private Chromosome<T> retrieveBestChromosome(List<Chromosome<T>> population) {
        Chromosome<T> best = null;
        for (Chromosome<T> c : population) {
            if (best == null) {
                best = c;
            } else if (best.getFitness() > c.getFitness()) {
                best = c;
            }
        }

        return best;
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

    public interface ProgressTracer<T> {
        void traceProgress(Metrics metrics);
    }
}
