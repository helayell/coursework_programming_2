package uk.ac.soton.comp1206.scene;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * The Multimedia class handles playing audio and background music for the game.
 */
public class Multimedia {
    private static MediaPlayer audioPlayer;
    private static MediaPlayer musicPlayer;

    /**
     * Plays an audio file once. This method is typically used for sound effects
     *
     * @param resourcePath The classpath resource path of the audio file.
     */
    public static void playAudio(String resourcePath) {
        try {
            // Loading media from classpath resources
            Media sound = new Media(Multimedia.class.getResource(resourcePath).toExternalForm());
            if (audioPlayer != null) {
                audioPlayer.stop(); // Stop any currently playing audio
            }
            audioPlayer = new MediaPlayer(sound);
            audioPlayer.play();
        } catch (Exception e) {
            System.err.println("Unable to play audio file: " + resourcePath);
            e.printStackTrace();
        }
    }

    /**
     * Plays background music from a given classpath resource, looping indefinitely.
     * This method is designed for continuous music playback throughout the game scenes.
     *
     * @param resourcePath The classpath resource path of the music file.
     */
    public static void playBackgroundMusic(String resourcePath) {
        try {
            // Loading music from classpath resources
            Media music = new Media(Multimedia.class.getResource(resourcePath).toExternalForm());
            if (musicPlayer != null) {
                musicPlayer.stop(); // Stop any currently playing music
            }
            musicPlayer = new MediaPlayer(music);
            musicPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Set music to loop indefinitely
            musicPlayer.play();
        } catch (Exception e) {
            System.err.println("Unable to play music file: " + resourcePath);
            e.printStackTrace();
        }
    }

    /**
     * Stops the currently playing background music.
     */
    public static void stopBackgroundMusic() {
        if (musicPlayer != null) {
            musicPlayer.stop();
        }
    }
}
