package com.ubb.aicourse.lab3.search.local;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Marius Adam
 */
public class Metrics {
    private Map<Keys, String> metricsMap;

    public Metrics() {
        this.metricsMap = new TreeMap<>();
    }

    public void set(Keys name, int i) {
        metricsMap.put(name, Integer.toString(i));
    }

    public void set(Keys name, double d) {
        metricsMap.put(name, Double.toString(d));
    }

    public void incrementInt(Keys name) {
        set(name, getInt(name) + 1);
    }

    public void set(Keys name, long l) {
        metricsMap.put(name, Long.toString(l));
    }

    public int getInt(Keys name) {
        String value = metricsMap.get(name);
        return value != null ? Integer.parseInt(value) : 0;
    }

    public double getDouble(Keys name) {
        String value = metricsMap.get(name);
        return value != null ? Double.parseDouble(value) : Double.NaN;
    }

    public long getLong(Keys name) {
        String value = metricsMap.get(name);
        return value != null ? Long.parseLong(value) : 0L;
    }

    public String get(Keys name) {
        return metricsMap.get(name);
    }

    public Set<Keys> keySet() {
        return metricsMap.keySet();
    }

    /** Sorts the key-value pairs by key names and formats them as equations. */
    public String toString() {
        return metricsMap.toString();
    }

    public void clear() {
        metricsMap.clear();
    }

    public long getMaxExecutionTime() {
        return getLong(Keys.MaxExecutionTime);
    }

    public long getElapsedTime() {
        return getLong(Keys.ElapsedTime);
    }

    public long getCurrentIteration() {
        return getLong(Keys.CurrentIteration);
    }

    public long getMaxIterations() {
        return getLong(Keys.MaxIterations);
    }

    public enum Keys {
        CurrentIteration,
        ElapsedTime,
        MaxIterations,
        MaxExecutionTime,
        BestChromosomeIteration, Exception, TotalExecutionTime, TotalIterations, BestChromosomeFitness, BestChromosomeTime
    }
}
