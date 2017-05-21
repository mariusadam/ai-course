package com.ubb.aicourse.tsp;

/**
 * @author Marius Adam
 */
public class City {
    private int label;
    private double x;
    private double y;

    public City(int label, double x, double y) {
        this.label = label;
        this.x = x;
        this.y = y;
    }

    public int getLabel() {
        return label;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        City city = (City) o;

        return getLabel() == city.getLabel();
    }

    @Override
    public int hashCode() {
        return getLabel();
    }
}
