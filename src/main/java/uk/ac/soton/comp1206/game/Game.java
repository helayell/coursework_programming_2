package uk.ac.soton.comp1206.game;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.component.GameBlock;
import uk.ac.soton.comp1206.component.GameBlockCoordinate;
import uk.ac.soton.comp1206.event.GameLoopListener;
import uk.ac.soton.comp1206.event.LineClearedListener;
import uk.ac.soton.comp1206.event.NextPieceListener;
import uk.ac.soton.comp1206.scene.Multimedia;

import java.util.*;

/**
 * The Game class handles the main logic, state and properties of the TetrECS game. Methods to manipulate the game state
 * and to handle actions made by the player should take place inside this class.
 */
public class Game {

    private NextPieceListener nextPieceListener;
    private  GamePiece followingPiece; // Tracks the following piece
    private LineClearedListener lineClearedListener;
    private Timer gameTimer;
    private GameLoopListener gameLoopListener;
    private NextPieceListener followingPieceListener = null;




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

    private int currentAimX;
    private int currentAimY;

    /**
     * Create a new game with the specified rows and columns. Creates a corresponding grid model.
     *
     * @param cols number of columns
     * @param rows number of rows
     */


    public Game(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        this.currentAimX = cols / 2;
        this.currentAimY = rows / 2;

        //Create a new grid model to represent the game state
        this.grid = new Grid(cols, rows);
        spawnPiece(); // This will set the initial currentPiece
        spawnFollowingPiece(); // This will set the initial followingPiece
    }
    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final IntegerProperty level = new SimpleIntegerProperty(0);
    private final IntegerProperty lives = new SimpleIntegerProperty(3);
    private static final DoubleProperty multiplier = new SimpleDoubleProperty(1.0);


    public void setNextPieceListener(NextPieceListener listener) {
        this.nextPieceListener = listener;
    }

    public void setLineClearedListener(LineClearedListener listener) {
        this.lineClearedListener = listener;
    }


    public void gameOver() {
        if (gameTimer != null) {
            gameTimer.cancel();// Stop the game timer
        }
        logger.info("Game Over. Final score: {}", getScore());

        // Notify any listeners or UI components that the game is over
        Platform.runLater(() -> {
            if (gameLoopListener != null) {
                gameLoopListener.onGameOver();
            }
        });
    }

    private void gameLoop() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    fireGameLoopEnd();
                    // All changes that affect the UI must be placed within this block
                    setLives(getLives() - 1);
                    multiplier.set(1.0);
                    nextPiece();
                    if (getLives() <= 0) {
                        logger.info("Game Over");
                        gameOver();
                    } else {
                        // Reset the timer within the FX thread to prevent IllegalStateException
                        nextPiece();
                        resetTimer();
                        fireGameLoopStart();
                    }
                });
            }
        };
        gameTimer.schedule(task, getTimerDelay());
    }

    private void resetTimer() {
        if (gameTimer != null) {
            gameTimer.cancel();  // Cancel the current running tasks
            gameTimer = new Timer("Game Timer");  // Reinitialize the timer
            gameLoop();  // This internally calls `fireGameLoopStart()`
        }
    }




    /**
     * Notify the listener when lines are cleared.
     * @param clearedBlocks the set of blocks that were cleared
     */
    protected void notifyLineCleared(Set<GameBlockCoordinate> clearedBlocks) {
        if (lineClearedListener != null) {
            lineClearedListener.onLineCleared(clearedBlocks);
        }
    }

    public void setOnGameLoopListener(GameLoopListener listener) {
        this.gameLoopListener = listener;
    }

    protected void fireGameLoopStart() {
        if (gameLoopListener != null) {
            gameLoopListener.onGameLoopStart();
        }
    }

    protected void fireGameLoopEnd() {
        if (gameLoopListener != null) {
            gameLoopListener.onGameLoopEnd();
        }
    }




    /**
     *  Method to generate or retrieve the next piece
     */

    public void generateNextPiece() {
        GamePiece nextPiece = followingPiece;
        followingPiece = GamePiece.createPiece((int) (Math.random() * GamePiece.PIECES));

        // Notify the listener with the new piece
        if (nextPieceListener != null) {
            nextPieceListener.nextPiece(nextPiece);
        }
    }

    /**
     * Start the game
     */
    public void start() {
        logger.info("Starting game");
        initialiseGame();
        // Initialize timer
        gameTimer = new Timer("Game Timer");
        gameLoop(); // Start the initial timer task
    }

    /**
     * Initialise a new game and set up anything that needs to be done at the start
     */
    public void initialiseGame() {
        logger.info("Initialising game");
    }

    /**
     * Calculate the delay time for the timer based on the current level of the game.
     * The delay starts at 12000 milliseconds and reduces by 500 milliseconds each level,
     * with a minimum delay of 2500 milliseconds.
     *
     * @return the calculated delay in milliseconds.
     */
    public int getTimerDelay() {
        int delay = 12000 - 500 * getLevel();
        return Math.max(2500, delay);
    }

    /**
     * Stops the game timer and cleans up any resources. It should be called
     * when the game is ending or needs to be paused.
     */
    public void stopGame() {
        if (gameTimer != null) {
            gameTimer.cancel();
            logger.info("Game stopped");
        }
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
            resetTimer(); // Reset the timer with new delay
        } else {
            logger.info("Cannot place piece");
        }
        //Get the new value for this block

        Multimedia.playAudio("/sounds/place.wav");
    }

    private static GamePiece currentPiece; // Tracks current piece
    private static final Random random = new Random();

    public void spawnPiece() {
        // Spawn a piece using a random index between 0 and the total number of pieces - 1
        currentPiece = GamePiece.createPiece(random.nextInt(GamePiece.PIECES));
        logger.info("Spawning new piece: {}", currentPiece);
    }


    private void spawnFollowingPiece() {
        followingPiece = GamePiece.createPiece(random.nextInt(GamePiece.PIECES));
        logger.info("Following new piece: {}", followingPiece);
    }

    public void nextPiece() {
        currentPiece = followingPiece;
        spawnFollowingPiece(); // Spawn a new following piece
        if (followingPieceListener != null) {
            followingPieceListener.nextPiece(currentPiece);
        }
        logger.info("New current piece: {}, New following piece spawned: {}, Multiplier: {}", currentPiece, followingPiece, multiplier.get());
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
            lineClearedListener.onLineCleared(clearedBlocks);
            // Increase multiplier after score is applied
            multiplier.set(multiplier.get() + 1);
            logger.info("Multiplier increased to {}", multiplier.get());
        } else {
            // No lines were cleared, reset the multiplier
            multiplier.set(1.0);
            logger.info("No lines cleared. Multiplier reset.");
        }
    }

    /**
     * Rotates the current piece 90 degrees clockwise.
     */
    public void rotateCurrentPiece() {
        if (currentPiece != null) {
            // Rotate the piece
            currentPiece.rotate();

            Multimedia.playAudio("/sounds/rotate.wav");

            // Optionally notify any listeners or update the game state
            logger.info("Current piece rotated: {}", currentPiece);
        } else {
            logger.warn("No current piece to rotate.");
        }
    }

    public void swapCurrentPiece() {
        if (currentPiece == null || followingPiece == null) {
            logger.warn("Attempted to swap pieces when one or both pieces are null");
            return;
        }

        GamePiece temp = currentPiece;
        currentPiece = followingPiece;
        followingPiece = temp;
        Multimedia.playAudio("/sounds/pling.wav");

        logger.info("Swapped current piece with following piece. Current: {}, Following: {}", currentPiece, followingPiece);

        // Notify listeners about the swap, update both current and following pieces on the UI

    }

    public void dropPieceAtAim() {
        if (grid.canPlayPiece(currentPiece, currentAimX, currentAimY)) {
            grid.playPiece(currentPiece, currentAimX, currentAimY);
            afterPiece(); // Handles line clearance
            nextPiece(); // Spawns next piece
        } else {
            logger.info("Cannot place piece(ENTER)");
            Multimedia.playAudio("/sounds/place.wav");
            // Handle post-drop actions such as generating the next piece
        }

        Multimedia.playAudio("/sounds/place.wav");

    }


    public void moveAim(int dx, int dy) {
        int newX = currentAimX + dx;
        int newY = currentAimY + dy;

        // Check boundaries before updating the current aim position
        if (newX >= 0 && newX < cols) {
            currentAimX = newX;
        }
        if (newY >= 0 && newY < rows) {
            currentAimY = newY;
        }
    }

    public GamePiece getFollowingPiece() {
        return followingPiece;
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
