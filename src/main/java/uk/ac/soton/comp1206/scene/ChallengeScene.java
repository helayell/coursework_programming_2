package uk.ac.soton.comp1206.scene;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.component.GameBlock;
import uk.ac.soton.comp1206.component.GameBoard;
import uk.ac.soton.comp1206.component.PieceBoard;
import uk.ac.soton.comp1206.event.GameLoopListener;
import uk.ac.soton.comp1206.event.NextPieceListener;
import uk.ac.soton.comp1206.game.Game;
import uk.ac.soton.comp1206.game.GamePiece;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;


/**
 * The Single Player challenge scene. Holds the UI for the single player challenge mode in the game.
 */
public class ChallengeScene extends BaseScene implements NextPieceListener {

    private static final Logger logger = LogManager.getLogger(MenuScene.class);
    protected Game game;
    private final PieceBoard pieceBoard;
    private final PieceBoard followingPieceBoard;
    private GameBoard gameBoard;
    private Rectangle timerBar;
    private Timeline timeline;

    /**
     * Create a new Single Player challenge scene
     *
     * @param gameWindow the Game Window
     */
    public ChallengeScene(GameWindow gameWindow) {
        super(gameWindow);
        logger.info("Creating Challenge Scene");
        this.pieceBoard = new PieceBoard(3, 3, 150, 150);
        this.followingPieceBoard = new PieceBoard(3, 3, 150, 150);
        setupGame();

    }

    /**
     * Build the Challenge window, setting up the game, UI elements, and layout.
     */
    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());
        // Initialize the game
        setupGame();
        // Create the root pane for the game window
        root = new GamePane(gameWindow.getWidth(), gameWindow.getHeight());
        // Create a StackPane to hold the challenge content
        var challengePane = new StackPane();
        challengePane.setMaxWidth(gameWindow.getWidth());
        challengePane.setMaxHeight(gameWindow.getHeight());
        challengePane.getStyleClass().add("menu-background");
        root.getChildren().add(challengePane);
        // Create a BorderPane to hold the main game content
        var mainPane = new BorderPane();
        challengePane.getChildren().add(mainPane);
        // Create a GameBoard instance with the game grid and window dimensions
        var board = new GameBoard(game.getGrid(), gameWindow.getWidth() / 2, gameWindow.getWidth() / 2);
        // Set the on-right-click event handler to rotate the next piece
        board.setOnRightClicked(this::rotateNextPiece);
        mainPane.setCenter(board);

        //Handle block on gameboard grid being clicked
        board.setOnBlockClick(this::blockClicked);
        // Adds the UI elements
        setupUIElements(mainPane);
        setupTimerBar(); // Initialize and set up the timer bar
        mainPane.setBottom(timerBar); // Add timer bar to the layout
        // Position both PieceBoards in the scene
        VBox rightPanel = new VBox(10); // Vertical Box with spacing of 10
        rightPanel.getChildren().addAll(pieceBoard, followingPieceBoard);
        mainPane.setRight(rightPanel);
    }

    /**
     * Sets up the timer bar for visualizing the remaining time in the game.
     */
    private void setupTimerBar() {
        // Create a new Rectangle for the timer bar with an initial width of 300 pixels and a height of 20 pixels.
        // The color is set to green initially.
        timerBar = new Rectangle(0, 0, 300, 20);  // Initial width, adjust as needed
        timerBar.setFill(Color.GREEN);  // Initial color, changes based on time left

        /*
         Create a new Timeline with two KeyFrames.
         The first KeyFrame has a Duration of zero and
         sets the width and fill properties of the timerBar.
         The second KeyFrame has a Duration based on the game's
         timer delay (in seconds) and sets the width and fill properties
         of the timerBar to 0 and red, respectively.
         */
        timeline = new Timeline(new KeyFrame(
                Duration.ZERO,
                new KeyValue(timerBar.widthProperty(), 300),
                new KeyValue(timerBar.fillProperty(), Color.GREEN)
        ), new KeyFrame(
                Duration.seconds(game.getTimerDelay() / 1000),
                new KeyValue(timerBar.widthProperty(), 0),
                new KeyValue(timerBar.fillProperty(), Color.RED)
        ));

        // Set the cycle count of the Timeline to Animation.INDEFINITE to continuously animate the timer bar.
        timeline.setCycleCount(Animation.INDEFINITE);
    }




    /**
     * Sets up the UI elements for the game, including the stats box and labels.
     *
     * @param mainPane the main BorderPane to which the UI elements will be added
     */
    private void setupUIElements(BorderPane mainPane) {
        // Creates a VBox to hold the stats labels, with a spacing of 10 pixels
        VBox statsBox = new VBox(10);
        // Create labels to display the game stats
        Label scoreLabel = new Label();
        scoreLabel.getStyleClass().add("score");
        Label levelLabel = new Label();
        levelLabel.getStyleClass().add("level");
        Label livesLabel = new Label();
        livesLabel.getStyleClass().add("lives");
        Label multiplierLabel = new Label();
        multiplierLabel.getStyleClass().add("multiplier");
        // Binds the label texts to the corresponding game properties
        scoreLabel.textProperty().bind(game.scoreProperty().asString("Score: %d"));
        levelLabel.textProperty().bind(game.levelProperty().asString("Level: %d"));
        livesLabel.textProperty().bind(game.livesProperty().asString("Lives: %d"));
        multiplierLabel.textProperty().bind(game.multiplierProperty().asString("Multiplier: %.1f"));
        // Adds the labels to the stats box
        statsBox.getChildren().addAll(scoreLabel, levelLabel, livesLabel, multiplierLabel);
        // Sets the stats box as the top element of the main pane
        mainPane.setTop(statsBox);
        statsBox.getStyleClass().add("scorelist");
    }

    /**
     * Handle when a block is clicked
     *
     * @param gameBlock the Game Block that was clocked
     */
    private void blockClicked(GameBlock gameBlock) {
        game.blockClicked(gameBlock);
    }

    /**
     * Setup the game object and model
     */
    public void setupGame() {
        logger.info("Starting a new challenge");

        //Start new game
        game = new Game(5, 5);
        game.setNextPieceListener(this);
        // Triggers the first piece generation
        game.generateNextPiece();
        // Set a LineClearedListener for the game to handle faded-out blocks
        game.setLineClearedListener(clearedBlocks -> {
            if (gameBoard != null) {
                gameBoard.fadeOut(clearedBlocks);
            }
        });
        // Set a GameLoopListener for the game to handle game loop events
        game.setOnGameLoopListener(new GameLoopListener() {
            @Override
            public void onGameLoopStart() {
                Platform.runLater(() -> {
                    if (timeline != null) {  // Ensure the timeline is initialized
                        timerBar.setWidth(300); // Reset the timer bar width
                        timerBar.setFill(Color.GREEN); // Reset the color to green
                        timeline.playFromStart(); // Start the timeline animation
                    }
                });
            }

            @Override
            public void onGameLoopEnd() {
                Platform.runLater(() -> {
                    if (timeline != null) {  // Ensure the timeline is initialized
                        timeline.stop(); // Stop the timeline
                        timerBar.setWidth(300); // Reset the width for a new start
                        timerBar.setFill(Color.GREEN); // Reset the color for clarity
                    }
                });
            }

            @Override
            public void onGameOver() {

            }
        });
    }


    /**
     * Initialise the scene and start the game
     */
    @Override
    public void initialise() {
        game.setNextPieceListener(this::nextPiece);
        logger.info("Initialising Challenge");
        game.start();
        Multimedia.playBackgroundMusic("/music/game.wav"); // Play background music for the game scene

        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.ESCAPE) {
                shutdownChallenge();
            } else if (code == KeyCode.R) {
                game.swapCurrentPiece();
            } else if (code == KeyCode.ENTER) {
                game.dropPieceAtAim();
            } else if (code == KeyCode.UP || code == KeyCode.W) {
                game.moveAim(0, -1); // Move aim up
            } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                game.moveAim(0, 1); // Move aim down
            } else if (code == KeyCode.LEFT || code == KeyCode.A) {
                game.moveAim(-1, 0); // Move aim left
            } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
                game.moveAim(1, 0); // Move aim right
            } else if (code == KeyCode.Q || code == KeyCode.E || code == KeyCode.Z || code == KeyCode.C) {
                game.rotateCurrentPiece();
            }
        });
    }


    /**
     * Method to properly shutdown the challenge and cleanup resources.
     */
    public void shutdownChallenge() {
        // Stop timers, release resources, save state, etc.
        logger.info("Shutting down the challenge");
        gameWindow.startMenu();
    }

    private void rotateNextPiece(GameBlock block) {
        game.rotateCurrentPiece();
    }

    @Override
    public void nextPiece(GamePiece nextPiece) {
        pieceBoard.displayPiece(nextPiece);
        followingPieceBoard.displayPiece(game.getFollowingPiece());
    }
}

