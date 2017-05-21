//package org.ubb.courses.ai.evolutivealg;
//
//import aima.core.search.local.GeneticAlgorithm;
//import aima.core.search.local.Individual;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author Marius Adam
// */
//public class Main {
//    public static void main(String... args) {
//        char[][] rebus = new char[5][5];
//        rebus[0][1] = 'x';
//        rebus[1][3] = 'x';
//        rebus[2][2] = 'x';
//        rebus[3][3] = 'x';
//        int n = 9;
//        List<String> words = new ArrayList<>();
//        words.add("a");
//        words.add("abc");
//        words.add("abc");
//        words.add("a");
//        words.add("qq");
//        words.add("rr");
//        words.add("zzz");
//        words.add("z");
//        words.add("123456");
//        GeneticAlgorithm<String> geneticAlgorithm = new GeneticAlgorithm<>(n, words, 0.3);
//
//        List<Individual<String>> init = new ArrayList<>();
//        init.add(new Individual<String>(words));
//
//        Individual<String> res = geneticAlgorithm.geneticAlgorithm(init, individual -> {
//            List<String> words1 = individual.getRepresentation();
//            int row = 0;
//            int column = 0;
//            int fitness = 0;
//            int word = 0;
//            while (row < n) {
//                while (rebus[row][column] == 'x' && column < n) {
//                    column++;
//                }
//                int colStart = column;
//                while (rebus[row][column] != 'x' && column < n) {
//                    column++;
//                }
//                int freePositions = column - colStart;
//                if (freePositions == 0) {
//                    // start of row so continue
//                    column++;
//                    continue;
//                }
//                fitness += Math.abs(freePositions - words1.get(word).length());
//                if (column >= n - 1) {
//                    column = 0;
//                    row++;
//                }
//            }
//
//            return fitness;
//        }, 100);
//
//        res.getRepresentation().forEach(System.out::println);
//    }
//}
