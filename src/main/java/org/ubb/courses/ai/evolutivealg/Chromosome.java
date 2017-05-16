package org.ubb.courses.ai.evolutivealg;

import java.util.List;

/**
 * @author Marius Adam
 */
public class Chromosome {
    private List<String> words;

    public Chromosome(List<String> words) {
        this.words = words;
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }
}
