package uk.ac.soton.comp1206.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.component.GameBlock;
import uk.ac.soton.comp1206.component.GameBlockCoordinate;

import java.util.HashSet;
import java.util.Random;

/**
 * The Game class handles the main logic, state and properties of the TetrECS game. Methods to manipulate the game state
 * and to handle actions made by the player should take place inside this class.
 */
public class Game {

    private static final Logger logger = LogManager.getLogger(Game.class);

    /**
     * Number of rows
     */
    protected final int rows;

    /**
     * Number of columns
     */
    protected final int cols;

    /**
     * The grid model linked to the game
     */
    protected final Grid grid;

    /**
     * Create a new game with the specified rows and columns. Creates a corresponding grid model.
     * @param cols number of columns
     * @param rows number of rows
     */
    public Game(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;

        //Create a new grid model to represent the game state
        this.grid = new Grid(cols,rows);
        spawnPiece();
    }

    /**
     * Start the game
     */
    public void start() {
        logger.info("Starting game");
        initialiseGame();
    }

    /**
     * Initialise a new game and set up anything that needs to be done at the start
     */
    public void initialiseGame() {
        logger.info("Initialising game");
    }

    /**
     * Handle what should happen when a particular block is clicked
     * @param gameBlock the block that was clicked
     */
    public void blockClicked(GameBlock gameBlock) {
        //Get the position of this block
        int x = gameBlock.getX();
        int y = gameBlock.getY();

        logger.info("Block clicked at: {} {}. Attempting to place piece.", x , y);

        if(grid.canPlayPiece(currentPiece, x , y)) {
            grid.playPiece(currentPiece, x , y); //PLace piece on grid
            afterPiece(); // Handles line clearance
            nextPiece(); // Spawns next piece
        }else{
            logger.info("Cannot place piece");
        }
        //Get the new value for this block
       /* int previousValue = grid.get(x,y);
        int newValue = previousValue + 1;
        if (newValue  > GamePiece.PIECES) {
            newValue = 0;
        }

        //Update the grid with the new value
        grid.set(x,y,newValue); */
    }
    private GamePiece currentPiece; // Tracks current piece
    private final Random random = new Random();

    private void spawnPiece() {
        // Spawn a piece using a random index between 0 and the total number of pieces - 1
        currentPiece = GamePiece.createPiece(random.nextInt(GamePiece.PIECES));
        logger.info("Spawning new piece: {}", currentPiece);
    }
    private void nextPiece() {
        spawnPiece(); // should replace the current piece with a new one
        logger.info("New piece spawned!");
    }
    public void afterPiece() {
        logger.info("Checking for full lines after placing piece.");

        HashSet<GameBlockCoordinate> clearedBlocks = new HashSet<>();
        boolean lineCleared = false;

        // Clear full horizontal lines
        for (int y = 0; y < grid.getRows(); y++) {
            boolean isFullLine = true;
            for (int x = 0; x < grid.getCols(); x++) {
                if (grid.get(x, y) == 0) {
                    isFullLine = false;
                    break;
                }
            }

            if (isFullLine) {
                for (int x = 0; x < grid.getCols(); x++) {
                    grid.set(x, y, 0); // Clear the line
                    clearedBlocks.add(new GameBlockCoordinate(x, y));
                }
                lineCleared = true;
            }
        }

        // Clear full vertical lines
        for (int x = 0; x < grid.getCols(); x++) {
            boolean isFullLine = true;
            for (int y = 0; y < grid.getRows(); y++) {
                if (grid.get(x, y) == 0) {
                    isFullLine = false;
                    break;
                }
            }
            if (isFullLine) {
                for (int y = 0; y < grid.getRows(); y++) {
                    grid.set(x, y, 0); // Clear the line
                    clearedBlocks.add(new GameBlockCoordinate(x, y));
                }
                lineCleared = true;
            }
        }
        if (lineCleared) {
            logger.info("Lines cleared, blocks affected: {}", clearedBlocks);
        } else {
            logger.info("No lines cleared.");
        }
    }


    /**
     * Get the grid model inside this game representing the game state of the board
     * @return game grid model
     */
    public Grid getGrid() {
        return grid;
    }

    /**
     * Get the number of columns in this game
     * @return number of columns
     */
    public int getCols() {
        return cols;
    }

    /**
     * Get the number of rows in this game
     * @return number of rows
     */
    public int getRows() {
        return rows;
    }


}
