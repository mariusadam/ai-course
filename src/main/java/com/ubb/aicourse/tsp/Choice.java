package com.ubb.aicourse.tsp;

/**
 * @author Marius Adam
 */
public class Choice {
    private City   city;
    private double history;
    private double distance;
    private double heuristic;
    private double probability;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public double getHistory() {
        return history;
    }

    public void setHistory(double history) {
        this.history = history;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(double heuristic) {
        this.heuristic = heuristic;
    }

    public double getProbability() {

        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }
}
