package uk.ac.soton.comp1206.event;

public interface GameLoopListener {
    void onGameLoopStart();   // Called when the game loop starts or resets
    void onGameLoopEnd();     // Called when the game loop ends (timer runs out)
    void onGameOver();
}
