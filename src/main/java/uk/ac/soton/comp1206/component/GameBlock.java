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

    /**
     * Set the center indicator flag
     *
     * @param centerIndicator whether to display a center indicator
     */
    public void setCenterIndicator(boolean centerIndicator) {
        this.centerIndicator = centerIndicator;
    }

    private void setupHoverEffects() {
        this.setOnMouseEntered(event -> highlight(true));
        this.setOnMouseExited(event -> highlight(false));
    }

    /**
     * Highlight the block on hover
     *
     * @param hover whether to highlight the block
     */
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
     *
     * This method is responsible for rendering the block's visual representation
     * based on its current value and center indicator status.
     */
    public void paint() {
        // Get the graphics context for the canvas
        var gc = getGraphicsContext2D();

        // Clear the previous content on the canvas
        clearBlock(gc);

        // Check if the block's value is 0, indicating an empty block
        if (value.get() == 0) {
            // Paint the block as empty
            paintEmpty();
        } else {
            // Paint the block with the corresponding color based on its value
            paintColor(COLOURS[value.get()]);
        }
        // Check if the center indicator flag is set to true
        if (centerIndicator) {
            // Create a gradient fill for the center indicator
            var gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0.5, Color.GOLD),
                    new Stop(0, Color.PURPLE));

            // Set the fill and stroke colors for the center indicator
            gc.setFill(gradient);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(0.65);

            // Calculate the radius of the center indicator based on the block's size
            double radius = Math.min(width, height) * 0.65; // Adjust size as necessary

            // Draw the center indicator as an oval shape
            gc.fillOval((width - radius) / 2, (height - radius) / 2, radius, radius);
        }
    }

    /**
     * Clear the block canvas by filling it with a transparent color
     *
     * This method is used to reset the block's visual representation before
     * rendering a new value or state.
     *
     * @param gc the GraphicsContext object for the block canvas
     */
    private void clearBlock(GraphicsContext gc) {
        // Clear the entire block canvas by drawing a transparent rectangle
        // over the entire area, effectively erasing any previous content
        gc.clearRect(0, 0, width, height);
    }

    /**
     * Paint this canvas empty, rendering a visually appealing empty block
     *
     * This method is used to render an empty block with a gradient fill and
     * a black border, making it visually distinct from filled blocks.
     */
    private void paintEmpty() {
        // Get the GraphicsContext object for the canvas
        var gc = getGraphicsContext2D();

        /* Clear the entire canvas by drawing a transparent rectangle
         over the entire area, effectively erasing any previous content
         */
        gc.clearRect(0, 0, width, height);

        /* Create a gradient fill for the empty block, transitioning from
         transparent to white to purple.
         */
        var gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(1, Color.TRANSPARENT),
                new Stop(0, Color.WHITE));
        new Stop(0.5, Color.PURPLE);

        // Set the fill color to the gradient
        gc.setFill(gradient);

        /* Draw a rounded rectangle to fill the entire canvas, using the
         gradient fill and rounded corners
        */
        gc.fillRoundRect(0, 0, width, height, 10, 10);

        // Set the stroke color to black
        gc.setStroke(Color.BLACK);

        /* Draw a border around the empty block using a rounded rectangle
         with a black stroke color
         */
        gc.strokeRoundRect(0, 0, width, height, 10, 10);
    }

    /**
     * Paint this canvas with the given colour, rendering a filled block
     * with a 3D effect and a black border.
     *
     * @param colour the colour to paint, which will be used to fill the block
     */
    private void paintColor(Paint colour) {
        var gc = getGraphicsContext2D();

        // Clear
        gc.clearRect(0, 0, width, height);

        // Create a drop shadow effect to give the block a slight 3D appearance
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0); // Set the vertical offset of the shadow
        ds.setOffsetX(3.0); // Set the horizontal offset of the shadow
        ds.setColor(Color.color(0, 0, 0, 0.5)); // Set the colour of the shadow
        gc.setEffect(ds); // Apply the drop shadow effect to the canvas

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
        // Get the current time in nanoseconds, which will be used as a reference point for the animation
        final long startNanoTime = System.nanoTime();

        // Create a new AnimationTimer to handle the fade-out animation
        new AnimationTimer() {
            // This method will be called repeatedly by the AnimationTimer to update the animation
            public void handle(long currentNanoTime) {
                // Calculate the elapsed time in seconds since the animation started
                double t = (currentNanoTime - startNanoTime) / 1_000_000_000.0;
                // Calculate the opacity of the block based on the elapsed time,
                // so that it fades out over a period of one second
                double opacity = 1.0 - t;

                // If the opacity has reached 0 or less, stop the animation and set the block to empty
                if (opacity <= 0) {
                    this.stop();
                    // Ensure the block is set to empty after fade out
                    setValue(0);
                    // Update the visual representation of the block to reflect its empty state
                    paintEmpty();
                    // Exit the method early, as the animation is complete
                    return;
                }

                // If the opacity is still greater than 0, update the visual representation of the block
                // with the current opacity value, creating the fade-out effect
                paintFade(opacity);
            }
        }.start(); // Start the animation timer
    }

    /**
     * Paints the block with a faded color based on the given opacity value.
     *
     * @param opacity The current opacity value of the block (between 0.0 and 1.0).
     */
    private void paintFade(double opacity) {
        var gc = getGraphicsContext2D();
        clearBlock(gc);
        Color fadedColor = Color.GREEN; // Assuming black fade for simplicity
        gc.setFill(fadedColor);
        gc.fillRect(10, 10, width, height);
    }
}