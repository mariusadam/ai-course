package com.ubb.aicourse.tsp;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Marius Adam
 */
public class Aco {
    private static final Logger logger = Logger.getLogger(Aco.class.getName());

    public Ant search(City[] cities, int maxIt, int antsCount, double decayFactor, double cHeur, double cHist, double antPhero) {
        Ant bestAnt = null;
        double[][] pheromoneMatrix = initializePheromoneMatrix(cities.length, antPhero);
        Ant[] solutions = new Ant[antsCount];
        for (; maxIt > 0; maxIt--) {

            for (int i = 0; i < antsCount; i++) {
                Ant candidate = new Ant();
                candidate.setPermutation(stepwiseConst(cities, pheromoneMatrix, cHeur, cHist));
                candidate.setCost(cost(candidate.getPermutation(), cities));
                if (bestAnt == null || bestAnt.getCost() > candidate.getCost()) {
                    bestAnt = candidate;
                }
                solutions[i] = candidate;
            }
            System.out.println("Iteration " + " " + maxIt + " best=" + bestAnt.getCost());

            decayPheromone(pheromoneMatrix, decayFactor);
            updatePheromone(pheromoneMatrix, solutions, antPhero);
        }

        return bestAnt;
    }

    private void updatePheromone(double[][] pheromoneMatrix, Ant[] solutions, double antPhero) {
        for (Ant a : solutions) {
            int[] labels = a.getPermutation();
            for (int i = 0; i < labels.length; i++) {
                int nextCityLabel = (i == labels.length - 1) ? labels[0] : labels[i + 1];
                pheromoneMatrix[i][nextCityLabel] += antPhero / a.getCost();
                pheromoneMatrix[nextCityLabel][i] += antPhero / a.getCost();
            }
        }
    }

    private void decayPheromone(double[][] pheromoneMatrix, double decayFactor) {
        for (int i = 0; i < pheromoneMatrix.length; i++) {
            for (int j = 0; j < pheromoneMatrix[i].length; j++) {
                pheromoneMatrix[i][j] *= (1.0 - decayFactor);
            }
        }
    }

    private int[] stepwiseConst(City[] cities, double[][] pheromoneMatrix, double cHeur, double cHist) {
        int count = 0;
        int[] permutation = new int[cities.length];
        boolean[] permutationFlag = new boolean[cities.length];
        int lastCityLabel = Utils.random.nextInt(cities.length);
        permutationFlag[lastCityLabel] = true;
        permutation[count++] = lastCityLabel;
        for (; count < cities.length; count++) {
            List<Choice> choices = computeChoices(
                    cities,
                    lastCityLabel,
                    permutationFlag,
                    pheromoneMatrix,
                    cHeur,
                    cHist
            );
            lastCityLabel = selectCity(choices).getLabel();
            permutation[count] = lastCityLabel;
            permutationFlag[lastCityLabel] = true;
        }

        return permutation;
    }

    private City selectCity(List<Choice> choices) {
        double sum = 0;
        for (Choice c : choices) {
            sum += c.getProbability();
        }
        if (sum == 0) {
            return choices.get(Utils.random.nextInt(choices.size())).getCity();
        }

        double v = Utils.random.nextDouble();
        for (Choice c : choices) {
            v -= c.getProbability() / sum;
            if (v <= 0) {
                return c.getCity();
            }
        }

        return choices.get(choices.size() - 1).getCity();
    }

    private List<Choice> computeChoices(City[] cities, int lastCityLabel, boolean[] visited, double[][] pheromone, double cHeur, double cHist) {
        List<Choice> choices = new ArrayList<>();
        for (int i = 0; i < cities.length; i++) {
            City currentCity = cities[i];
            if (currentCity.getLabel() != i) {
                throw new IllegalStateException("You know");
            }
            if (visited[currentCity.getLabel()]) {
                continue;
            }

            Choice choice = new Choice();
            choice.setCity(currentCity);
            choice.setHistory(Math.pow(pheromone[lastCityLabel][currentCity.getLabel()], cHist));
            choice.setDistance(Utils.euclideanDistance(cities[lastCityLabel], currentCity));
            choice.setHeuristic(Math.pow(1.0 / choice.getDistance(), cHeur));
            choice.setProbability(choice.getHistory() * choice.getHeuristic());
            choices.add(choice);
        }
        return choices;
    }

    private int[] createHeuristicSolution(int problemSize) {
        int[] sol = new int[problemSize];
        for (int i = 0; i < problemSize; i++) {
            sol[i] = i;
        }

        Utils.shuffle(sol);
        Utils.shuffle(sol);

        return sol;
    }

    private double cost(int[] permutation, City[] cities) {
        double distance = 0;
        int nextCity;
        for (int i = 0; i < permutation.length; i++) {
            nextCity = (i + 1) % permutation.length;
            distance += Utils.euclideanDistance(cities[permutation[i]], cities[permutation[nextCity]]);
        }

        return distance;
    }

    private double[][] initializePheromoneMatrix(int citiesCount, double naiveScore) {
        double[][] matrix = new double[citiesCount][citiesCount];
        for (int i = 0; i < citiesCount; i++) {
            for (int j = 0; j < citiesCount; j++) {
                matrix[i][j] = naiveScore;
            }
        }

        return matrix;
    }
}
