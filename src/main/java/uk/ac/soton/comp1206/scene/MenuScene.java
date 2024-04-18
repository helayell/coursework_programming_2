package uk.ac.soton.comp1206.scene;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;

/**
 * The main menu of the game. Provides a gateway to the rest of the game.
 */
public class MenuScene extends BaseScene {

    private static final Logger logger = LogManager.getLogger(MenuScene.class);

    /**
     * Create a new menu scene
     * @param gameWindow the Game Window this will be displayed in
     */
    public MenuScene(GameWindow gameWindow) {
        super(gameWindow);
        logger.info("Creating Menu Scene");
    }

    /**
     * Build the menu layout
     */
    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        root = new GamePane(gameWindow.getWidth(),gameWindow.getHeight());
        root.getStyleClass().add("gamepane"); // Ensure the game pane has black background

        StackPane menuPane = new StackPane();
        menuPane.setMaxHeight(gameWindow.getHeight());
        menuPane.getStyleClass().add("menu-background");
        root.getChildren().add(menuPane);

        VBox menuBox = new VBox(20);
        menuBox.setAlignment(Pos.CENTER);
        menuPane.getChildren().add(menuBox);

        //Awful title
        Text title = new Text("TetrECS");
        title.getStyleClass().add("bigtitle");
        menuBox.getChildren().add(title);

        //For now, let us just add a button that starts the game. I'm sure you'll do something way better.
        Button playButton = new Button("Play!");
        playButton.getStyleClass().add("menuButton");
        //Bind the button action to the startGame method in the menu
        playButton.setOnAction(this::startGame);
        menuBox.getChildren().add(playButton);

        Button settingsButton = new Button("Settings");
        settingsButton.getStyleClass().add("menuButton");
        settingsButton.setOnAction(this::showSettings);
        menuBox.getChildren().add(settingsButton);

        Button instructionsButton = new Button("Instructions");
        instructionsButton.getStyleClass().add("menuButton");
        instructionsButton.setOnAction(this::showInstructions);
        menuBox.getChildren().add(instructionsButton);

        Button exitButton = new Button("Exit");
        exitButton.getStyleClass().add("menuButton");
        exitButton.setOnAction(e -> System.exit(0));
        menuBox.getChildren().add(exitButton);




        applyFadeInTransition(menuPane);
    }

    /**
     * Shows the settings for the game
     */
    private void showSettings(ActionEvent actionEvent) {

        logger.info("Settings button clicked");

    }

    /**
     * Handles the action of pressing the Instructions button.
     * Transitions to the InstructionsScene.
     * @param event the event triggered when the button is pressed
     */
    private void showInstructions(ActionEvent event) {
        gameWindow.loadScene(new InstructionsScene(gameWindow));
    }

    /**
     * Initialise the menu
     */
    @Override
    public void initialise() {
        Multimedia.stopBackgroundMusic(); // Stop current music
        // Then play the appropriate music for the current scene
        logger.info("Initialising Menu");
        Multimedia.playBackgroundMusic("/music/menu.mp3");

        // Listen for keyboard events
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                System.exit(0); // Leaves the game (but why would you :/ )
            }
        });

    }

    private void applyFadeInTransition(StackPane pane) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), pane);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    /**
     * Handle when the Start Game button is pressed
     * @param event event
     */
    private void startGame(ActionEvent event) {
        gameWindow.startChallenge();
    }

}
