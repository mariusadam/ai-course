package com.ubb.aicourse.tsp;

/**
 * @author Marius Adam
 */
public class Main {
    public static void main(String[] args) {
        Aco aco = new Aco();
        aco.search(
                Utils.readCities("/tsp/berlin52.in"),
                50,
                50,
                0.4,
                7,
                0.4,
                1
        );
    }
}
