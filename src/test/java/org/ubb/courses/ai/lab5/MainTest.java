package org.ubb.courses.ai.lab5;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;


/**
 * @author Marius Adam
 */
public class MainTest {
    @Test
    public void fuzzy() throws Exception {
        assertEquals(0.72, Main.fuzzy(37, 15, 30, 30, 55));
        assertEquals(0.68, Main.fuzzy(38, 15, 30, 30, 55));
        assertEquals(0.64, Main.fuzzy(39, 15, 30, 30, 55));
        assertEquals(0.64, Main.fuzzy(39, 15, 30, 30, 55));
        assertEquals(0.8666666666666667, Main.fuzzy(34, 15, 30, 30, 60));
    }
}