package com.ubb.aicourse.tsp;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;

/**
 * @author Marius Adam
 */
public class Utils {
    public final static Random random = new SecureRandom();

    static class MyRandom extends SecureRandom {
        @Override
        public int nextInt(int bound) {
            int val = super.nextInt(bound);
            System.out.println("Generated " + val);
            return val;
        }
    }

    public static void shuffle(int[] items) {
        int poz, aux;
        for (int i = 0; i < items.length; i++) {
            poz = random.nextInt(items.length);
            aux = items[poz];
            items[poz] = items[i];
            items[i] = aux;
        }
    }

    public static City[] readCities(String fileName) {
        Scanner scanner;
        try {
            scanner = new Scanner(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            scanner = new Scanner(Utils.class.getResourceAsStream(fileName));
        }
        ArrayList<City> result = new ArrayList<>();

        while (scanner.hasNext()) {
            result.add(new City(scanner.nextInt() - 1, scanner.nextDouble(), scanner.nextDouble()));
        }
        scanner.close();

        City[] cities = new City[result.size()];
        result.forEach(c -> cities[c.getLabel()] = c);
        return cities;
    }

    public static double euclideanDistance(City c1, City c2) {
        return Math.sqrt(
                Math.pow(c1.getX() - c2.getX(), 2) +
                Math.pow(c1.getY() - c2.getY(), 2)
        );
    }

    public static boolean contains(int[] items, int item) {
        return IntStream.of(items).anyMatch(value -> value == item);
    }
}
