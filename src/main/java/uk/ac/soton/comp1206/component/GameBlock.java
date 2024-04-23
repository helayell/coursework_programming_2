package uk.ac.soton.comp1206.component;

import javafx.animation.AnimationTimer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Visual User Interface component representing a single block in the grid.
 *
 * Extends Canvas and is responsible for drawing itself.
 *
 * Displays an empty square (when the value is 0) or a coloured square depending on value.
 *
 * The GameBlock value should be bound to a corresponding block in the Grid model.
 */
public class GameBlock extends Canvas {

    private static final Logger logger = LogManager.getLogger(GameBlock.class);
    private boolean centerIndicator = false;

    /**
     * The set of colours for different pieces
     */
    public static final Color[] COLOURS = {
            Color.TRANSPARENT,
            Color.DEEPPINK,
            Color.RED,
            Color.ORANGE,
            Color.YELLOW,
            Color.YELLOWGREEN,
            Color.LIME,
            Color.GREEN,
            Color.DARKGREEN,
            Color.DARKTURQUOISE,
            Color.DEEPSKYBLUE,
            Color.AQUA,
            Color.AQUAMARINE,
            Color.BLUE,
            Color.MEDIUMPURPLE,
            Color.PURPLE
    };

    public final GameBoard gameBoard;

    private final double width;
    private final double height;

    /**
     * The column this block exists as in the grid
     */
    private final int x;

    /**
     * The row this block exists as in the grid
     */
    private final int y;

    /**
     * The value of this block (0 = empty, otherwise specifies the colour to render as)
     */
    private final IntegerProperty value = new SimpleIntegerProperty(0);

    /**
     * Create a new single Game Block
     *
     * @param gameBoard the board this block belongs to
     * @param x         the column the block exists in
     * @param y         the row the block exists in
     * @param width     the width of the canvas to render
     * @param height    the height of the canvas to render
     */
    public GameBlock(GameBoard gameBoard, int x, int y, double width, double height) {
        this.gameBoard = gameBoard;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;

        //A canvas needs a fixed width and height
        setWidth(width);
        setHeight(height);
        setupHoverEffects(); // Call this method to setup hover effects

        //Do an initial paint
        paint();

        //When the value property is updated, call the internal updateValue method
        value.addListener(this::updateValue);
    }

    /**
     * When the value of this block is updated,
     *
     * @param observable what was updated
     * @param oldValue   the old value
     * @param newValue   the new value
     */
    private void updateValue(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        paint();
    }

    public void setCenterIndicator(boolean centerIndicator) {
        this.centerIndicator = centerIndicator;
    }

    private void setupHoverEffects() {
        this.setOnMouseEntered(event -> highlight(true));
        this.setOnMouseExited(event -> highlight(false));
    }

    private void highlight(boolean hover) {
        if (hover) {
            getGraphicsContext2D().setStroke(Color.PURPLE); // Set hover color
            getGraphicsContext2D().setLineWidth(4); // Set hover border thickness
        } else {
            getGraphicsContext2D().setStroke(Color.BLACK);
            getGraphicsContext2D().setLineWidth(1); // Reset border thickness
        }
        paint(); // Repaint to update visual state
    }




    /**
     * Handle painting of the block canvas
     */
    public void paint() {
        var gc = getGraphicsContext2D();
        clearBlock(gc); // Clear previous content

        if (value.get() == 0) {
            paintEmpty();
        } else {
            paintColor(COLOURS[value.get()]);
        }


        if (centerIndicator) {
            var gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0.5, Color.GOLD),
                    new Stop(0, Color.PURPLE));
            gc.setFill(gradient);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(0.65);

            double radius = Math.min(width, height) * 0.65; // Adjust size as necessary
            gc.fillOval((width - radius) / 2, (height - radius) / 2, radius, radius);
        }
    }

    private void clearBlock(GraphicsContext gc) {
        gc.clearRect(0, 0, width, height);
    }



    /**
     * Paint this canvas empty
     */
    private void paintEmpty() {
        var gc = getGraphicsContext2D();

        // Clear
        gc.clearRect(0, 0, width, height);

        // Gradient fill for empty blocks to make them visually appealing
        var gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(1, Color.TRANSPARENT),
                new Stop(0, Color.WHITE));
                new Stop(0.5, Color.PURPLE);
        gc.setFill(gradient);
        gc.fillRoundRect(0, 0, width, height, 10, 10);  // Rounded corners

        // Border
        gc.setStroke(Color.BLACK);
        gc.strokeRoundRect(0, 0, width, height, 10, 10);
    }


    /**
     * Paint this canvas with the given colour
     *
     * @param colour the colour to paint
     */
    private void paintColor(Paint colour) {
        var gc = getGraphicsContext2D();

        // Clear
        gc.clearRect(0, 0, width, height);

        // Drop shadow for a slight 3D effect
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0);
        ds.setOffsetX(3.0);
        ds.setColor(Color.color(0, 0, 0, 0.5));
        gc.setEffect(ds);

        // Colour fill with rounded corners
        gc.setFill(colour);
        gc.fillRoundRect(0, 0, width, height, 10, 10);  // Rounded corners

        // Border
        gc.setStroke(Color.BLACK);
        gc.strokeRoundRect(0, 0, width, height, 10, 10);
        gc.setEffect(null);  // Reset the effect so it does not affect other elements
    }


    /**
     * Get the column of this block
     *
     * @return column number
     */
    public int getX() {
        return x;
    }

    /**
     * Get the row of this block
     *
     * @return row number
     */
    public int getY() {
        return y;
    }

    /**
     * Get the current value held by this block, representing it's colour
     *
     * @return value
     */
    public int getValue() {
        return this.value.get();
    }

    /**
     * Bind the value of this block to another property. Used to link the visual block to a corresponding block in the Grid.
     *
     * @param input property to bind the value to
     */
    public void bind(ObservableValue<? extends Number> input) {
        value.bind(input);
    }

    /**
     * Sets the value of the GameBlock to the specified new value.
     *
     * @param newValue the new value to be set for the GameBlock
     */
    public void setValue(int newValue) {
        // Check if the value is not bound to any other component
        if (!value.isBound()) {
            // Update the value of the GameBlock
            this.value.set(newValue);
            // Triggers a repaint of the block with the new value's color
            paint();
        }// else {
            // Logs a warning if the value is bound and cannot be changed
            //logger.warn("Attempted to set a bound value for GameBlock at position (" + x + ", " + y + ")");
    }

    /**
     * Initiates a fade-out effect on this block.
     */
    public void fadeOut() {
        final long startNanoTime = System.nanoTime();
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                double t = (currentNanoTime - startNanoTime) / 1_000_000_000.0; // convert to seconds
                double opacity = 1.0 - t; // Fade out over one second

                if (opacity <= 0) {
                    this.stop();
                    setValue(0); // Ensure the block is set to empty after fade out
                    paintEmpty();
                    return;
                }

                paintFade(opacity);
            }
        }.start();
    }

    private void paintFade(double opacity) {
        var gc = getGraphicsContext2D();
        clearBlock(gc);
        Color fadedColor = new Color(0, 0, 0, opacity); // Assuming black fade for simplicity
        gc.setFill(fadedColor);
        gc.fillRect(0, 0, width, height);
    }
}

