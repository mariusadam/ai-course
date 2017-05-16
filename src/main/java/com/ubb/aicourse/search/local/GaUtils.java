package com.ubb.aicourse.search.local;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Marius Adam
 */
public class GaUtils {

    final public static Random random = new Random();

    /**
     * For a permutation i0, i1, . . . , iN-1 of the set {0, 1, . . . , N-1}
     * we let aj denote the number of integers in the permutation which
     * precede j but are greater than j. So, aj is a measure of how much
     * out of order j is. The sequence of numbers a0, a1, . . . , aN-1 is
     * called the inversion sequence of the permutation i0, i1, . . . , iN-1.
     * <p>
     * {@see http://user.ceng.metu.edu.tr/~ucoluk/research/publications/tspnew.pdf}
     *
     * @param permutation array holding the zero based permutation
     * @return array holding the inversion sequence
     */
    public static int[] getInversionSequenceOf(int[] permutation) {
        int[] inv = new int[permutation.length];
        int m;
        for (int i = 0; i < permutation.length; i++) {
            inv[i] = 0;
            m = 0;
            while (m < permutation.length && permutation[m] != i) {
                if (permutation[m] > i) {
                    inv[i]++;
                }
                m++;
            }
        }
        return inv;
    }

    /**
     * @param invSq array holding the inversion sequence
     * @return array holding the permutation
     */
    public static int[] getPermutationOf(int[] invSq) {
        int[] pos = new int[invSq.length];
        for (int i = invSq.length - 1; i >= 0; i--) {
            for (int m = i + 1; m < invSq.length; m++) {
                if (pos[m] >= invSq[i] + 1) {
                    pos[m]++;
                }
            }
            pos[i] = invSq[i] + 1;
        }

        int[] perm = new int[invSq.length];
        for (int i = 0; i < pos.length; i++) {
            perm[pos[i] - 1] = i;
        }
        return perm;
    }

    public static <T> StopFunction<T> simpleStopFn() {
        return (currentPopulation, bestSoFar, metrics) -> {
            // continues until the time has not elapsed
            // or the maximum number of iterations is not reached
            return (metrics.getMaxExecutionTime() <= 0L || metrics.getElapsedTime() <= metrics.getMaxExecutionTime())
                    && (metrics.getMaxIterations() <= 0L || (metrics.getCurrentIteration() >= metrics.getMaxIterations()));
        };
    }

    public static <T> Function<List<Chromosome<T>>, Chromosome<T>> rouletteWheelSelection() {
        return chromosomes -> {
            double fitnessSum = 0;
            for(Chromosome<T> c : chromosomes) {
                fitnessSum += c.getFitness();
            }

            double value = random.nextDouble() * fitnessSum;
            for (Chromosome<T> chromosome : chromosomes) {
                value -= chromosome.getFitness();
                if (value <= 0) {
                    return chromosome;
                }
            }
            return chromosomes.get(chromosomes.size() - 1);
        };
    }

    public static Function<Chromosome<Integer>, Chromosome<Integer>> moduloRandomResetting(int individualLength) {
        return chromosome -> {
            int mutateOffset = random.nextInt(individualLength);
            int alphaValue = random.nextInt(individualLength);

            List<Integer> mutatedRepresentation = new ArrayList<>(chromosome.getRepresentation());

            mutatedRepresentation.set(
                    mutateOffset,
                    alphaValue % (individualLength - mutateOffset)
            );

            return new Chromosome<>(mutatedRepresentation);
        };
    }

    public static <T> BiFunction<Chromosome<T>, Chromosome<T>, Chromosome<T>> singleCutCrossOver(int individualLength) {
        return (x, y) -> {
            int c = random.nextInt(individualLength);
            // return APPEND(SUBSTRING(x, 1, c), SUBSTRING(y, c+1, n))
            List<T> childRepresentation = new ArrayList<T>();
            childRepresentation.addAll(x.getRepresentation().subList(0, c));
            childRepresentation.addAll(y.getRepresentation().subList(c, individualLength));

            return new Chromosome<T>(childRepresentation);
        };
    }

    /**
     *
     * @param puzzle the encoded puzzle, with ' ' on a free space and 'x' on a blocked space
     * @return the puzzle fittness function
     */
    public static Function<Chromosome<Integer>, Double> puzlleFitness(List<Character> puzzle) {
        return c -> {
            if (c.isEvaluated()) {
                return c.getFitness();
            }

            return 0D;
        };
    }
}
