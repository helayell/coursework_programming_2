package uk.ac.soton.comp1206.scene;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;

public class SettingsScene extends BaseScene {

    private static final Logger logger = LogManager.getLogger(SettingsScene.class);

    /**
     * Constructs a new Settings scene with the specified GameWindow.
     */
    public SettingsScene(GameWindow gameWindow) {
        super(gameWindow);
        logger.info("Setting up settings scene");

    }

    @Override
    public void initialise() {

    }
    /**
     * Build the Settings layout
     */
    @Override
    public void build() {

        root = new GamePane(gameWindow.getWidth(),gameWindow.getHeight());
        root.getStyleClass().add("gamepane");

        StackPane settingsPane = new StackPane();
        settingsPane.setMaxHeight(gameWindow.getHeight());
        settingsPane.getStyleClass().add("menu-background");
        root.getChildren().add(settingsPane);

        VBox settingsBox = new VBox(20);
        settingsBox.setAlignment(Pos.CENTER);
        settingsPane.getChildren().add(settingsBox);

        // Create a back button to return to the menu
        Button backButton = new Button("Back");
        backButton.getStyleClass().add("menuButton");
        backButton.setOnAction(e -> gameWindow.startMenu());
        settingsBox.getChildren().add(backButton);


        // Create a toggle button for muting music
        ToggleButton muteButton = new ToggleButton();
        Image muteImage = new Image(getClass().getResourceAsStream("/images/volume-silent-line-icon.png"));
        Image unmuteImage = new Image(getClass().getResourceAsStream("/images/volume-full-line-icon.png"));
        muteButton.setMinWidth(gameWindow.getWidth() * 0.5);
        muteButton.setGraphic(new ImageView(unmuteImage)); // Default to unmuted

        muteButton.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                muteButton.setGraphic(new ImageView(muteImage));
                Multimedia.stopBackgroundMusic();// Mute the music
            } else {
                muteButton.setGraphic(new ImageView(unmuteImage));
                Multimedia.unmuteBackgroundMusic(); // Unmute the music
            }
        });


        settingsBox.getChildren().add(muteButton);


    }
}
