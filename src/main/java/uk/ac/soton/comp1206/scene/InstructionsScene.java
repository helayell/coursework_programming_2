package uk.ac.soton.comp1206.scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
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
     *
     * @param gameWindow The GameWindow associated with this scene.
     */
    public InstructionsScene(GameWindow gameWindow) {
        super(gameWindow);
    }

    /**
     * Builds the scene layout, including an image view for instructions and a back button.
     */
    @Override
    public void build() {
        // Create the root pane with the game window's width and height
        root = new GamePane(gameWindow.getWidth(), gameWindow.getHeight());
        root.getStyleClass().add("gamepane");

        // Create a stack pane for the instructions
        StackPane instructionsPane = new StackPane();
        instructionsPane.setMaxHeight(gameWindow.getHeight());
        instructionsPane.getStyleClass().add("instructions-background");
        root.getChildren().add(instructionsPane);

        // Create a vertical box for the instructions
        VBox instructionsBox = new VBox(20);
        instructionsBox.setAlignment(Pos.CENTER);
        instructionsPane.getChildren().add(instructionsBox);

        // Grid pane for displaying PieceBoards in a grid layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.BASELINE_CENTER);
        grid.setHgap(7);// Sets horizontal gaps between grid cells to 7 pixels
        grid.setVgap(7);// Sets vertical gaps between grid cells to 7 pixels
        grid.setPadding(new Insets(20)); // adds padding of 20 padding values all around
        instructionsBox.getChildren().add(grid);// Adds the grid to the VBox


        /*
         Loop to create and display each game piece in a 5x3 grid
         We create a grid with 5 columns and a number of rows that
         depends on the total number of pieces.
         */
        // Loop to create and display each game piece in a 5x3 grid
        int numCols = 5; // Number of columns
        for (int i = 0; i < GamePiece.PIECES; i++) {
            GamePiece piece = GamePiece.createPiece(i); // Create a new game piece
            PieceBoard pieceBoard = new PieceBoard(3, 3, 50, 50); // Create a new PieceBoard
            pieceBoard.displayPiece(piece); // Display the game piece on the PieceBoards

            // Calculate column and row based on the piece index
            int col = i % numCols;
            int row = i / numCols;
            grid.add(pieceBoard, col, row);
        }


        // Load and display the instructions image
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/images/instructions.png")));
        imageView.setFitWidth(gameWindow.getWidth() * 0.7); // Set width to 70% of window
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
