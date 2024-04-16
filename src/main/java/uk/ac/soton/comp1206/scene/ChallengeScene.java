package uk.ac.soton.comp1206.scene;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.component.GameBlock;
import uk.ac.soton.comp1206.component.GameBoard;
import uk.ac.soton.comp1206.component.PieceBoard;
import uk.ac.soton.comp1206.game.Game;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;


/**
 * The Single Player challenge scene. Holds the UI for the single player challenge mode in the game.
 */
public class ChallengeScene extends BaseScene {

    private static final Logger logger = LogManager.getLogger(MenuScene.class);
    protected Game game;

    /**
     * Create a new Single Player challenge scene
     * @param gameWindow the Game Window
     */
    public ChallengeScene(GameWindow gameWindow) {
        super(gameWindow);
        logger.info("Creating Challenge Scene");
    }
    PieceBoard pieceBoard = new PieceBoard(3, 3, 150, 150); // Dimensions for a 3x3 grid, adjust size as needed

    /**
     * Build the Challenge window
     */
    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        setupGame();

        root = new GamePane(gameWindow.getWidth(),gameWindow.getHeight());

        var challengePane = new StackPane();
        challengePane.setMaxWidth(gameWindow.getWidth());
        challengePane.setMaxHeight(gameWindow.getHeight());
        challengePane.getStyleClass().add("menu-background");
        root.getChildren().add(challengePane);

        var mainPane = new BorderPane();
        challengePane.getChildren().add(mainPane);

        var board = new GameBoard(game.getGrid(),gameWindow.getWidth()/2,gameWindow.getWidth()/2);
        mainPane.setCenter(board);

        //Handle block on gameboard grid being clicked
        board.setOnBlockClick(this::blockClicked);
        // Adds the UI elements
        setupUIElements(mainPane);

        mainPane.setRight(pieceBoard);
    }

    private void setupUIElements(BorderPane mainPane) {
        VBox statsBox = new VBox(10); //Vbox to hold our stats, spacing

        Label scoreLabel = new Label();
        Label levelLabel = new Label();
        Label livesLabel = new Label();
        Label multiplierLabel = new Label();

        scoreLabel.textProperty().bind(game.scoreProperty().asString("Score: %d"));
        levelLabel.textProperty().bind(game.levelProperty().asString("Level: %d"));
        livesLabel.textProperty().bind(game.levelProperty().asString("Lives: %d"));
        multiplierLabel.textProperty().bind(game.multiplierProperty().asString("Multiplier: %.1f"));

        statsBox.getChildren().addAll(scoreLabel, levelLabel, livesLabel, multiplierLabel);
        mainPane.setTop(statsBox);
    }

    /**
     * Handle when a block is clicked
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
    }

    /**
     * Initialise the scene and start the game
     */
    @Override
    public void initialise() {
        logger.info("Initialising Challenge");
        game.start();
        Multimedia.playBackgroundMusic("resources/music/game.wav"); // Play background music for the game scene
    }

}

