package uk.ac.soton.comp1206.component;

public class PieceBoard extends GameBoard {

    public PieceBoard(int cols, int rows, double width, double height) {
        super(cols, rows, width, height);
    }

    /**
     * Display a piece on the PieceBoard. The piece is represented as a 2D array where
     * each element is the value (color) of the block at that position.
     * @param piece The piece to display
     */
    public void displayPiece(int[][] piece) {
        // Ensure the grid is clear before displaying a new piece
        clearGrid();

        // Calculate the top-left position to start displaying the piece
        // to ensure it's centered if the piece is smaller than the PieceBoard
        int startX = (getCols() - piece[0].length) / 2;
        int startY = (getRows() - piece.length) / 2;

        // Loop through the piece array and update the corresponding GameBlocks
        for (int y = 0; y < piece.length; y++) {
            for (int x = 0; x < piece[y].length; x++) {
                // Only update the block if the piece's value at this position is not 0
                if (piece[y][x] != 0) {
                    GameBlock block = getBlock(x + startX, y + startY);
                    block.setValue(piece[y][x]); // This calls the new setValue method in GameBlock
                }
            }
        }
    }

    /**
     * Clear the grid by setting all blocks to 0 (empty).
     */
    private void clearGrid() {
        for (int y = 0; y < getRows(); y++) {
            for (int x = 0; x < getCols(); x++) {
                getBlock(x, y).setValue(0);
            }
        }
    }
}
