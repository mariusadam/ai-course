package org.ubb.courses.ai.lab5;

import static java.lang.Math.max;

/**
 * @author Marius Adam
 */
public class Main {
    public static void main(String ...args) {

    }

    static double fuzzy(int x, int a, int b, int c, int d) {
        double f1 = (x - a) * 1.0 / (b - a);
        double f2 = (d - x) * 1.0 / (d - c);

        double minim = Math.min(Math.min(f1, 1), f2);
        double maxim = max(0, minim);
        return maxim;
    }
}
