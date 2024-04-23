package uk.ac.soton.comp1206.game;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * The Grid is a model which holds the state of a game board. It is made up of a set of Integer values arranged in a 2D
 * arrow, with rows and columns.
 * <p>
 * Each value inside the Grid is an IntegerProperty can be bound to enable modification and display of the contents of
 * the grid.
 * <p>
 * The Grid contains functions related to modifying the model, for example, placing a piece inside the grid.
 * <p>
 * The Grid should be linked to a GameBoard for it's display.
 */
public class Grid {

    /**
     * The number of columns in this grid
     */
    private final int cols;

    /**
     * The number of rows in this grid
     */
    private final int rows;

    /**
     * The grid is a 2D arrow with rows and columns of SimpleIntegerProperties.
     */
    private final SimpleIntegerProperty[][] grid;

    /**
     * Create a new Grid with the specified number of columns and rows and initialise them
     *
     * @param cols number of columns
     * @param rows number of rows
     */
    public Grid(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;

        //Create the grid itself
        grid = new SimpleIntegerProperty[cols][rows];

        //Add a SimpleIntegerProperty to every block in the grid
        for (var y = 0; y < rows; y++) {
            for (var x = 0; x < cols; x++) {
                grid[x][y] = new SimpleIntegerProperty(0);
            }
        }
    }

    /**
     * Get the Integer property contained inside the grid at a given row and column index. Can be used for binding.
     *
     * @param x column
     * @param y row
     * @return the IntegerProperty at the given x and y in this grid
     */
    public IntegerProperty getGridProperty(int x, int y) {
        return grid[x][y];
    }

    /**
     * Update the value at the given x and y index within the grid
     *
     * @param x     column
     * @param y     row
     * @param value the new value
     */
    public void set(int x, int y, int value) {
        grid[x][y].set(value);
    }

    /**
     * Get the value represented at the given x and y index within the grid
     *
     * @param x column
     * @param y row
     * @return the value
     */
    public int get(int x, int y) {
        try {
            //Get the value held in the property at the x and y index provided
            return grid[x][y].get();
        } catch (ArrayIndexOutOfBoundsException e) {
            //No such index
            return -1;
        }
    }

    /**
     * Get the number of columns in this game
     *
     * @return number of columns
     */
    public int getCols() {
        return cols;
    }

    /**
     * Get the number of rows in this game
     *
     * @return number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Checks if a piece can be placed on the grid at the specified coordinates.
     * @param piece the game piece to be placed
     * @param x the column index of the piece's center
     * @param y the row index of the piece's center
     * @return true if the piece can be placed; false otherwise
     */

    public boolean canPlayPiece(GamePiece piece, int x, int y) {
        int[][] blocks = piece.getBlocks(); // 2D array representing the shape of the piece
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                if (blocks[i][j] == 0) {
                    continue; // Skip empty blocks in the piece
                }
                int gridX = x + i - 1; // Calculate the actual x coordinate on the grid
                int gridY = y + j - 1; // Calculate the actual y coordinate on the grid
                if (gridX < 0 || gridX >= cols || gridY < 0 || gridY >= rows || get(gridX, gridY) != 0) {
                    return false; // Block cannot be placed if out of bounds or if cell is not empty
                }
            }
        }
        return true; // All checks passed, piece can be placed
    }

    /**
     * Places a piece on the grid if possible at the specified coordinates.
     * @param piece the game piece to be placed
     * @param x the column index of the piece's center
     * @param y the row index of the piece's center
     */
    public void playPiece(GamePiece piece, int x, int y) {
        if (!canPlayPiece(piece, x, y)) {
            return; // Cannot play the piece here, might throw an exception or log an error as needed
        }
        int[][] blocks = piece.getBlocks();
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                if (blocks[i][j] == 0) {
                    continue; // Skip empty blocks
                }
                int gridX = x + i - 1; // Adjust for the center of the piece
                int gridY = y + j - 1; // Adjust for the center of the piece
                set(gridX, gridY, piece.getValue()); // Place the piece block on the grid
            }
        }
    }

}