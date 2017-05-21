package com.ubb.aicourse.lab3.aco;

import com.ubb.aicourse.lab3.search.local.Metrics;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

/**
 * @author Marius Adam
 */
public class AntColonyOptimisation {
    private final static Random random = new SecureRandom();

    private Integer[]  blankSpacesLength;
    private String[]   words;
    private int        maxIt;
    private int        antsCount;
    private double     decayFactor;
    private double     heuristicCoeff;
    private double     historyCoeff;
    private double     pheromoneRewardFactor;
    private double[][] pheromone;
    private Metrics    metrics;

    public AntColonyOptimisation(String[] words, Integer[] blankSpacesLength) {
        this.blankSpacesLength = blankSpacesLength;
        this.words = words;
        this.pheromone = new double[words.length][words.length];
        this.metrics = new Metrics();
    }

    public void setMaxIt(int maxIt) {
        this.maxIt = maxIt;
    }

    public void setAntsCount(int antsCount) {
        this.antsCount = antsCount;
    }

    public void setDecayFactor(double decayFactor) {
        this.decayFactor = decayFactor;
    }

    public void setHeuristicCoeff(double heuristicCoeff) {
        this.heuristicCoeff = heuristicCoeff;
    }

    public void setHistoryCoeff(double historyCoeff) {
        this.historyCoeff = historyCoeff;
    }

    public void setPheromoneRewardFactor(double pheromoneRewardFactor) {
        this.pheromoneRewardFactor = pheromoneRewardFactor;
    }

    public String[] search() {
        initializePheromone();
        Ant bestAnt = null;
        Ant[] ants = new Ant[antsCount];

        metrics.clear();
        long startTime = System.currentTimeMillis();
        int itCount = 0;
        for (; itCount < maxIt; itCount++) {

            for (int j = 0; j < antsCount; j++) {
                int[] perm = generatePermutation();
                double fitness = computeFitness(perm);
                Ant candidate = new Ant(perm, fitness);

                if (bestAnt == null || bestAnt.getFitness() > candidate.getFitness()) {
                    bestAnt = candidate;
                    metrics.set(Metrics.Keys.BestChromosomeIteration, itCount + 1);
                    metrics.set(Metrics.Keys.BestChromosomeTime, System.currentTimeMillis() - startTime);
                    metrics.set(Metrics.Keys.BestChromosomeFitness, bestAnt.getFitness());
                }
                ants[j] = candidate;
            }

            applyPheromoneDecay();
            updatePheromone(ants);
        }

        metrics.set(Metrics.Keys.TotalExecutionTime, System.currentTimeMillis() - startTime);
        metrics.set(Metrics.Keys.TotalIterations, itCount);

        return decodeSolution(bestAnt);
    }

    public Metrics getMetrics() {
        return metrics;
    }

    private void updatePheromone(Ant[] ants) {
        for (Ant a : ants) {
            int[] perm = a.getPermutation();
            for (int i = 0; i < perm.length; i++) {
                int nextWordIndex = perm[(i + 1) % perm.length];
                int currentWordIndex = perm[i];
                pheromone[currentWordIndex][nextWordIndex] += pheromoneRewardFactor / a.getFitness();
                pheromone[nextWordIndex][currentWordIndex] += pheromoneRewardFactor / a.getFitness();
            }
        }
    }

    private void applyPheromoneDecay() {
        modifyPheromone(currentPhero -> currentPhero * (1 - decayFactor));
    }

    private double computeFitness(int[] permutation) {
        double fitness = 1;
        for (int i = 0; i < permutation.length; i++) {
            fitness += Math.abs(words[permutation[i]].length() - blankSpacesLength[i]);
        }

        return fitness;
    }

    private int[] generatePermutation() {
        int count = 0;
        int[] permutation = new int[words.length];
        boolean[] permutationFlag = new boolean[words.length];
        int lastWordIndex = random.nextInt(words.length);
        permutation[count++] = lastWordIndex;
        permutationFlag[lastWordIndex] = true;

        for (; count < words.length; count++) {
            List<Choice> choices = computeChoices(permutation, permutationFlag, lastWordIndex, count);
            lastWordIndex = selectNextWordIndex(choices);
            permutation[count] = lastWordIndex;
            permutationFlag[lastWordIndex] = true;
        }

        return permutation;
    }

    private int selectNextWordIndex(List<Choice> choices) {
        double sum = choices
                .stream()
                .mapToDouble(Choice::getProbability)
                .reduce(0, (partialSum, prob) -> partialSum + prob);
        if (sum == 0) {
            return choices.get(random.nextInt(choices.size())).getWordIndex();
        }

        double spot = random.nextDouble();
        for (Choice c : choices) {
            spot -= c.getProbability() / sum;
            if (spot <= 0) {
                return c.getWordIndex();
            }
        }

        return choices.get(choices.size() - 1).getWordIndex();
    }

    private List<Choice> computeChoices(int[] permutation, boolean[] visited, int lastWordIndex, int position) {
        List<Choice> choices = new ArrayList<>();

        for (int i = 0; i < words.length; i++) {
            if (visited[i]) {
                continue;
            }

            // just to respect the standard algorithm, but cannot do any heuristics
            double heuristicFactor = Math.pow(localSearch(lastWordIndex, i, position), heuristicCoeff);
            double historicalFactor = Math.pow(pheromone[lastWordIndex][i], historyCoeff);

            choices.add(new Choice(i, heuristicFactor * historicalFactor));
        }

        return choices;
    }

    private double localSearch(int lastWordIndex, int next, int position) {
        double v = 1;
        v += Math.abs(words[lastWordIndex].length() - blankSpacesLength[position - 1]);
        v += Math.abs(words[next].length() - blankSpacesLength[position]);
        return 1.0 / v;
    }

    private String[] decodeSolution(Ant ant) {
        String[] result = new String[words.length];
        int count = 0;
        for (int i : ant.getPermutation()) {
            result[count++] = words[i];
        }

        return result;
    }

    private void initializePheromone() {
        modifyPheromone(currentPhero -> pheromoneRewardFactor);
    }

    private void modifyPheromone(Function<Double, Double> modificationFn) {
        for (int i = 0; i < pheromone.length; i++) {
            for (int j = 0; j < pheromone[i].length; j++) {
                pheromone[i][j] = modificationFn.apply(pheromone[i][j]);
            }
        }
    }

    private static class Choice {
        private int    wordIndex;
        private double probability;

        public Choice(int wordIndex, double probability) {
            this.wordIndex = wordIndex;
            this.probability = probability;
        }

        public int getWordIndex() {
            return wordIndex;
        }

        public double getProbability() {

            return probability;
        }
    }
}
