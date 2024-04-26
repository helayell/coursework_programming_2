package uk.ac.soton.comp1206.event;

/**
 * Interface for listening to game loop events.
 * This interface defines two methods that will be called at specific points in the game loop.
 */
public interface GameLoopListener {
    /**
     * Called when the game loop starts or resets.
     * This method will be used when the game loop is initialized or restarted.
     */
    void onGameLoopStart();

    /**
     * Called when the game loop ends (timer runs out).
     * This method will be used when the game loop reaches its end, typically due to a timer expiring.
     */
    void onGameLoopEnd();
}