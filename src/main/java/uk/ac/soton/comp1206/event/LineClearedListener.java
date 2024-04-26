/**
 * Package for event handling in the game.
 */
package uk.ac.soton.comp1206.event;

import uk.ac.soton.comp1206.component.GameBlockCoordinate;

import java.util.Set;

/**
 * Interface for listening to line cleared events.
 * This interface defines a single method that will be called when a line is cleared in the game.
 */
public interface LineClearedListener {
    /**
     * Called when a line is cleared in the game.
     *
     * @param clearedLines A set of GameBlockCoordinate objects representing the cleared lines.
     */
    void onLineCleared(Set<GameBlockCoordinate> clearedLines);
}