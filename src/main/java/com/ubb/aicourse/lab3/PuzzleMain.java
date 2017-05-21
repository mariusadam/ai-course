package com.ubb.aicourse.lab3;

import com.ubb.aicourse.lab3.aco.AntColonyOptimisation;
import com.ubb.aicourse.lab3.search.local.*;

import java.util.*;

/**
 * @author Marius Adam
 */
public class PuzzleMain {

    public static void main(String[] args) throws Exception {
        Integer set = 2;
        String dir = "/lab3/" + set + "/";

        Scanner puzzleScanner = new Scanner(PuzzleMain.class.getResourceAsStream(dir + "puzzle.txt"));

        StringBuilder sb = new StringBuilder();
        while (puzzleScanner.hasNext()) {
            sb.append(puzzleScanner.next()).append('x');
        }

        String puzzle = sb.toString();
        List<Integer> blankWordsLen = new ArrayList<>();
        for (String s : puzzle.split("x")) {
            if (!s.isEmpty()) {
                blankWordsLen.add(s.length());
            }
        }

        Scanner wordsScanner = new Scanner(PuzzleMain.class.getResourceAsStream(dir + "words.txt"));

        ArrayList<String> givenWords = new ArrayList<>();
        while (wordsScanner.hasNext()) {
            givenWords.add(wordsScanner.next());
        }
        Collections.shuffle(givenWords, new Random(System.nanoTime()));
        Collections.shuffle(givenWords, new Random(System.nanoTime()));

        if (blankWordsLen.size() != givenWords.size()) {
            throw new Exception("Number of blank spaces must be the same with the number of words");
        }

        System.out.println("Initial words: ");
        givenWords.forEach(System.out::println);

//        runEvolutive(givenWords, blankWordsLen);
        runAco(givenWords, blankWordsLen);

    }

    private static void runAco(List<String> givenWords, List<Integer> blankWordsLen) {
        AntColonyOptimisation aco = new AntColonyOptimisation(
                givenWords.toArray(new String[0]),
                blankWordsLen.toArray(new Integer[0])
        );
        aco.setMaxIt(150);
        aco.setAntsCount(20);
        aco.setDecayFactor(0.5);
        aco.setHeuristicCoeff(0.8);
        aco.setHistoryCoeff(1.0);
        aco.setPheromoneRewardFactor(1);

        Arrays.stream(aco.search()).forEach(System.out::println);
    }

    private static void runEvolutive(List<String> givenWords, List<Integer> blankWordsLen) {
        PuzzleAgo algo = new PuzzleAgo();
        algo.setChromosomeLength(givenWords.size());
        algo.setMutationProbability(0.6);
        algo.setFitnessFn(GaUtils.puzzleFitness(blankWordsLen, givenWords));
        algo.setSelectionFn(GaUtils.tournamentSelection(50));
        algo.setMutationFn(GaUtils.moduloRandomResetting(givenWords.size()));
        algo.setMaxIterations(1000);

        List<Integer> permutation = new ArrayList<>();
        for (int i = 0; i < givenWords.size(); i++) {
            permutation.add(i);
        }
        List<Chromosome<Integer>> initialPopulation = new ArrayList<>();
        Integer initialPopulationSize = 150;

        for (int i = 0; i < initialPopulationSize; i++) {
            List<Integer> representation = new ArrayList<>(permutation);
            Collections.shuffle(representation, new Random(System.nanoTime()));
            initialPopulation.add(new PuzzleChromosome(GaUtils.getInversionSequenceOf(representation)));
        }
        algo.setInitialPopulation(initialPopulation);

        Chromosome<Integer> sol = algo.search();
        List<Integer> solPerm = GaUtils.getPermutationOf(sol.getRepresentation());
        System.out.println("Solution found: ");
        for (int i = 0; i < sol.length(); i++) {
            System.out.println(givenWords.get(solPerm.get(i)));
        }
        System.out.println("Fitness " + sol.getFitness() + " found in iteration " + algo.getMetrics().get(Metrics.Keys.BestChromosomeIteration));
        int a = 1;
    }
}
