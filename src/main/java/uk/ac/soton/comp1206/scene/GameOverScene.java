package uk.ac.soton.comp1206.scene;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import uk.ac.soton.comp1206.game.Game;
import uk.ac.soton.comp1206.ui.GameWindow;

public class GameOverScene extends BaseScene {

    public GameOverScene(GameWindow gameWindow) {
        super(gameWindow);
        this.game = game;
    }
    protected Game game;

    @Override
    public void build() {

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);

        Label gameOverLabel = new Label("Game Over!  Your Score: " + game.getScore());
        Button playAgainButton = new Button("Play Again");
        Button menuButton = new Button("Back to Menu");

        playAgainButton.setOnAction(this::playAgain);
        menuButton.setOnAction(this:: backToMenu);

        layout.getChildren().addAll(gameOverLabel, playAgainButton, menuButton);
        root.getChildren().add(layout);

    }
    private void backToMenu(ActionEvent event) {
        gameWindow.startMenu();
    }

    private void playAgain(ActionEvent event) {
        gameWindow.loadScene(new ChallengeScene(gameWindow));
    }

    @Override
    public void initialise() {

    }


}
