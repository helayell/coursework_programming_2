package uk.ac.soton.comp1206.scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.component.PieceBoard;
import uk.ac.soton.comp1206.game.GamePiece;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;

public class InstructionsScene extends BaseScene {

    private static final Logger logger = LogManager.getLogger(InstructionsScene.class);

    /**
     * Constructs a new InstructionsScene with the specified GameWindow.
     */

    public InstructionsScene(GameWindow gameWindow) {
        super(gameWindow);
    }

    /**
     * Builds the scene layout, including an image view for instructions and a back button.
     */
    @Override
    public void build() {
        root = new GamePane(gameWindow.getWidth(), gameWindow.getHeight());
        root.getStyleClass().add("gamepane");

        StackPane instructionsPane = new StackPane();
        root.getChildren().add(instructionsPane);

        VBox instructionsBox = new VBox(20);
        instructionsBox.setAlignment(Pos.CENTER);
        instructionsPane.getChildren().add(instructionsBox);

        // Grid pane for displaying PieceBoards in a grid layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_RIGHT);
        grid.setHgap(10);// Sets horizontal gaps between grid cells to 10 pixels
        grid.setVgap(10);// Sets vertical gaps between grid cells to 10 pixels
        grid.setPadding(new Insets(20)); // adds padding of 20 padding values all around
        instructionsBox.getChildren().add(grid);// Adds the grid to the VBox

        // Loop to create and display each game piece
        for (int i = 0; i < GamePiece.PIECES; i++) {
            /* Create a game piece using the method in GamePiece class
             'i' iterates from 0 to the total number of pieces minus one (15 pieces in total)
             */
            GamePiece piece = GamePiece.createPiece(i);
            /* Initializes a new PieceBoard to display the game pieces
             The PieceBoard is set with dimensions of 3x3 and size 100x100 pixels
             */
            PieceBoard pieceBoard = new PieceBoard(3, 3, 100, 100);
            /* Displays the game piece blocks on the PieceBoard
            The blocks are retrieved from the GamePiece instance
             */
            pieceBoard.displayPiece(piece.getBlocks());
            grid.add(pieceBoard, 15, 5); // Arrange in 5 columns
        }


        // Load and display the instructions image
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/images/instructions.png")));
        imageView.setFitWidth(gameWindow.getWidth() * 0.5); // Set width to 80% of window
        imageView.setPreserveRatio(true);
        instructionsBox.getChildren().add(imageView);

        // Create a back button to return to the menu
        Button backButton = new Button("Back");
        backButton.getStyleClass().add("menuButton");
        backButton.setOnAction(e -> gameWindow.startMenu());
        instructionsBox.getChildren().add(backButton);
    }

    /**
     * Initializes the scene. This method is called after the scene is constructed.
     */
    @Override
    public void initialise() {
        logger.info("Initialising Instructions Scene");

        // Listen for keyboard events
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                gameWindow.startMenu();  // Go back to the main menu directly
            }
        });
    }
}
