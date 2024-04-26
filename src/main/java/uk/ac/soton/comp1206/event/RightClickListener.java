package uk.ac.soton.comp1206.event;

import uk.ac.soton.comp1206.component.GameBlock;

/**
 * Interface for listening to right-click events on GameBlocks.
 * This interface defines a single method that will be called when a GameBlock is right-clicked.
 */

public interface RightClickListener {

    /**
     * Called when a GameBlock is right-clicked.
     *
     * @param gameBlock The GameBlock that was right-clicked.
     *                  This parameter provides a reference to a GameBlock that triggered the event.
     */
    void rightClick(GameBlock gameBlock);
}
