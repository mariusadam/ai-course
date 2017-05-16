package org.ubb.courses.ai.evolutivealg;

/**
 * @author Marius Adam
 */
public class Crossword {
    private int rows;
    private int columns;
    private char[][] matrix;

    public final static char OCCUPIED = 'x';
    public final static char FREE = ' ';

    public Crossword(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.matrix = new char[rows][columns];
    }

    public void clear() {
        this.matrix = new char[rows][columns];
    }

    public void markOccupied(int row, int column) {
        if ((row < 0 || row >= rows) || (column < 0 || column >= columns)) {
            throw new IllegalArgumentException();
        }

        matrix[row][column] = OCCUPIED;
    }


}
