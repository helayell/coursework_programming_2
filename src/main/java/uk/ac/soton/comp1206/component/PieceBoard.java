package uk.ac.soton.comp1206.component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A specialized GameBoard for displaying upcoming game pieces in Tetris-like games.
 * It extends the GameBoard class to provide functionality specific to displaying small, 3x3 pieces typically.
 */
public class PieceBoard extends GameBoard {

    private static final Logger logger = LogManager.getLogger(PieceBoard.class);

    /**
     * Constructs a new PieceBoard with specified dimensions.
     *
     * @param cols  the number of columns in the PieceBoard (e.g., 3 for a Tetris piece preview)
     * @param rows  the number of rows in the PieceBoard
     * @param width the width of the PieceBoard in pixels
     * @param height the height of the PieceBoard in pixels
     */
    public PieceBoard(int cols, int rows, double width, double height) {
        super(cols, rows, width, height);
    }

    /**
     * Displays a game piece on the board. The piece is centered within the grid.
     * Each element of the piece array represents a block; non-zero values indicate the presence of a block.
     *
     * @param piece The 2D array representing the piece to display, where each element's value corresponds to a color index.
     */

    public void displayPiece(int[][] piece) {
        clearGrid(); // Clear previous pieces

        int centerX = (getCols() - piece[0].length) / 2;
        int startY = (getRows() - piece.length) / 2;

        for (int y = 0; y < piece.length; y++) {
            for (int x = 0; x < piece[y].length; x++) {
                int gridX = centerX + x;
                int gridY = startY + y;
                if (piece[y][x] != 0) {
                    GameBlock block = getBlock(gridX, gridY);
                    if (block != null) {
                        block.setValue(piece[y][x]);
                        block.paint();
                    }
                }
            }
        }

        // Add indicator on the central block
        GameBlock centerBlock = getBlock(getCols() / 2, getRows() / 2);
        if (centerBlock != null) {
            centerBlock.setCenterIndicator(true); // Set a flag to draw a special indicator
            centerBlock.paint();
        }
    }

    /**
     * Clears the grid by resetting all blocks to their default (empty) state.
     */
    private void clearGrid() {
        for (int y = 0; y < getRows(); y++) {
            for (int x = 0; x < getCols(); x++) {
                getBlock(x, y).setValue(0);
            }
        }
    }
}
