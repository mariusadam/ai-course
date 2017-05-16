package com.ubb.aicourse.search.local;

/**
 * @author Marius Adam
 */
public class PuzlleAlgo extends GenerationalGenAlg<Integer> {
    public PuzlleAlgo() {
        super();
    }

    @Override
    public void setChromosomeLength(int chromosomeLength) {
        super.setChromosomeLength(chromosomeLength);
        mutationFn = GaUtils.moduloRandomResetting(getChromosomeLength());
    }
}
