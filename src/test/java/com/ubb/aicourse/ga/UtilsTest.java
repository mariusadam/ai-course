package com.ubb.aicourse.ga;

import com.ubb.aicourse.search.local.GaUtils;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * @author Marius Adam
 */
public class UtilsTest {
    @Test
    public void getInversionSequenceOf() throws Exception {
        assertArrayEquals(
                new int[]{5, 2, 3, 0, 2, 0, 0},
                GaUtils.getInversionSequenceOf(new int[]{3, 5, 1, 6, 2, 0, 4})
        );
    }

    @Test
    public void getPermutationOf() throws Exception {
        assertArrayEquals(
                new int[]{3, 5, 1, 6, 2, 0, 4},
                GaUtils.getPermutationOf(new int[]{5, 2, 3, 0, 2, 0, 0})
        );
    }

}