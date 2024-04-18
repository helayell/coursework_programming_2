package uk.ac.soton.comp1206.scene;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;

public class InstructionsScene extends BaseScene {

    private static final Logger logger = LogManager.getLogger(InstructionsScene.class);

    public InstructionsScene(GameWindow gameWindow) {
        super(gameWindow);
    }

    @Override
    public void build() {
        root = new GamePane(gameWindow.getWidth(), gameWindow.getHeight());
        root.getStyleClass().add("gamepane");

        StackPane instructionsPane = new StackPane();
        root.getChildren().add(instructionsPane);

        VBox instructionsBox = new VBox(20);
        instructionsBox.setAlignment(Pos.CENTER);
        instructionsPane.getChildren().add(instructionsBox);

        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/images/instructions.png")));
        imageView.setFitWidth(gameWindow.getWidth() * 0.8); // Set width to 80% of window
        imageView.setPreserveRatio(true);
        instructionsBox.getChildren().add(imageView);

        Button backButton = new Button("Back");
        backButton.getStyleClass().add("menuButton");
        backButton.setOnAction(e -> gameWindow.startMenu());
        instructionsBox.getChildren().add(backButton);
    }

    @Override
    public void initialise() {
        logger.info("Initialising Instructions Scene");
    }
}
