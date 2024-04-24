package uk.ac.soton.comp1206.event;

/**
 * A listener interface for music playing events.
 */
public interface MusicPlayingListener {

    /**
     * Called when music playback starts.
     */
    void onMusicStart();

    /**
     * Called when music playback stops.
     */
    void onMusicStop();
}
