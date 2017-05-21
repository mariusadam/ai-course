package com.ubb.aicourse.lab3.search.local;

/**
 * @author Marius Adam
 */
public class MetricsTableRow {
    private String  algorithm;
    private Integer populationSize;
    private Long    bestIteration;
    private Double  bestTimeSeconds;
    private Double  totalSeconds;
    private Long    iterationsCount;
    private Double  bestFitness;
    private String  others;

    public MetricsTableRow() {
        this("", 0, 0L, 0D, 0D, 0L, 0D, "");
    }

    public MetricsTableRow(String algorithm, Integer populationSize, Long bestIteration, Double bestTime, Double totalTime, Long iterationsCount, Double bestFitness, String others) {
        this.algorithm = algorithm;
        this.populationSize = populationSize;
        this.bestIteration = bestIteration;
        this.bestTimeSeconds = bestTime;
        this.totalSeconds = totalTime;
        this.iterationsCount = iterationsCount;
        this.bestFitness = bestFitness;
        this.others = others;

    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public Integer getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(Integer populationSize) {
        this.populationSize = populationSize;
    }

    public Long getBestIteration() {
        return bestIteration;
    }

    public void setBestIteration(Long bestIteration) {
        this.bestIteration = bestIteration;
    }

    public Double getBestTimeSeconds() {
        return bestTimeSeconds;
    }

    public void setBestTimeSeconds(Long time) {
        this.bestTimeSeconds = time / 1000.0;
    }

    public Double getTotalSeconds() {
        return totalSeconds;
    }

    public void setTotalSeconds(Long time) {
        this.totalSeconds = time / 1000.0;
    }

    public Long getIterationsCount() {
        return iterationsCount;
    }

    public void setIterationsCount(Long iterationsCount) {
        this.iterationsCount = iterationsCount;
    }

    public Double getBestFitness() {
        return bestFitness;
    }

    public void setBestFitness(Double bestFitness) {
        this.bestFitness = bestFitness;
    }
}
