package uk.ac.soton.comp1206.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.component.GameBlock;
import uk.ac.soton.comp1206.component.GameBlockCoordinate;

import java.util.HashSet;
import java.util.Random;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

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
     *
     * @param cols number of columns
     * @param rows number of rows
     */


    public Game(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;

        //Create a new grid model to represent the game state
        this.grid = new Grid(cols, rows);
        spawnPiece();
    }
    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final IntegerProperty level = new SimpleIntegerProperty(0);
    private final IntegerProperty lives = new SimpleIntegerProperty(3);
    private final DoubleProperty multiplier = new SimpleDoubleProperty(1.0);


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
     *
     * @param gameBlock the block that was clicked
     */
    public void blockClicked(GameBlock gameBlock) {
        //Get the position of this block
        int x = gameBlock.getX();
        int y = gameBlock.getY();

        logger.info("Block clicked at: {} {}. Attempting to place piece.", x, y);

        if (grid.canPlayPiece(currentPiece, x, y)) {
            grid.playPiece(currentPiece, x, y); //PLace piece on grid
            afterPiece(); // Handles line clearance
            nextPiece(); // Spawns next piece
        } else {
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
        logger.info("New piece spawned. Multiplier : {}", multiplier.get());
    }

    public void updateScore(int numberOfLines, int numberOfBlocksCleared) {
        if (numberOfLines > 0) {
            // Calculate the score
            int scoreToAdd = numberOfLines * numberOfBlocksCleared * 10 * (int) multiplier.get();

            // Update the score property with the new score
            score.set(score.get() + scoreToAdd);

            //Calculates the new level based on the updated score
            int newLevel = score.get() / 1000;

            //If the new level is greater than the current level, update the level
            if (newLevel > level.get()) {
                level.set(newLevel);
                logger.info("Level up! New level: {}", newLevel);
            }

            logger.info("Score updated: {} points added for clearing {} lines and {} blocks. New score: {}", scoreToAdd, numberOfLines, numberOfBlocksCleared, score.get());
        }
    }

    public void afterPiece() {
        logger.info("Checking for full lines after placing piece.");

        HashSet<GameBlockCoordinate> clearedBlocks = new HashSet<>();
        int lineClearedCount = 0;

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
                lineClearedCount++;
                for (int x = 0; x < grid.getCols(); x++) {
                    grid.set(x, y, 0); // Clear the line
                    clearedBlocks.add(new GameBlockCoordinate(x, y));
                }
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
                lineClearedCount++;
                for (int y = 0; y < grid.getRows(); y++) {
                    grid.set(x, y, 0); // Clear the line
                    clearedBlocks.add(new GameBlockCoordinate(x, y));
                }
            }
        }
        if (!clearedBlocks.isEmpty()) {
            // Lines were cleared, update the score
            updateScore(lineClearedCount, clearedBlocks.size());
            logger.info("Lines cleared: {}, Blocks cleared: {}, Current Score: {}",
                    lineClearedCount,
                    clearedBlocks.size(),
                    getScore());
            // Increase multiplier after score is applied
            multiplier.set(multiplier.get() + 1);
            logger.info("Multiplier increased to {}", multiplier.get());
        } else {
            // No lines were cleared, reset the multiplier
            multiplier.set(1.0);
            logger.info("No lines cleared. Multiplier reset.");
        }
    }
    public IntegerProperty scoreProperty() {
        return this.score;
    }

    public int getScore() {
        return this.score.get();
    }

    public void setScore(int score) {
        this.score.set(score);
    }

    public IntegerProperty levelProperty() {
        return this.level;
    }

    public int getLevel() {
        return this.level.get();
    }

    public void setLevel(int level) {
        this.level.set(level);
    }

    public IntegerProperty livesProperty() {
        return this.lives;
    }

    public int getLives() {
        return this.lives.get();
    }

    public void setLives(int lives) {
        this.lives.set(lives);
    }

    public DoubleProperty multiplierProperty() {
        return this.multiplier;
    }

    public double getMultiplier() {
        return this.multiplier.get();
    }

    public void setMultiplier(double multiplier) {
        this.multiplier.set(multiplier);
    }


    /**
     * Get the grid model inside this game representing the game state of the board
     *
     * @return game grid model
     */
    public Grid getGrid() {
        return grid;
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


}
