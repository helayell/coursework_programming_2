package uk.ac.soton.comp1206.scene;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class Multimedia {
    private MediaPlayer audioPlayer;
    private static MediaPlayer musicPlayer;

    /**
     * Plays an audio file once. This can be used for sound effects.
     *
     * @param filePath The file path of the audio file.
     */
    public void playAudio(String filePath) {
        try {
            Media sound = new Media(new File(filePath).toURI().toString());
            // If there's already a sound playing, stop it before playing the new sound
            if (audioPlayer != null) {
                audioPlayer.stop();
            }
            audioPlayer = new MediaPlayer(sound);
            audioPlayer.play();
        } catch (Exception e) {
            System.err.println("Unable to play audio file: " + filePath);
            e.printStackTrace();
        }
    }

    /**
     * Plays background music from a given file, looping indefinitely.
     *
     * @param filePath The file path of the music file.
     */
    public static void playBackgroundMusic(String filePath) {
        try {
            Media music = new Media(new File(filePath).toURI().toString());
            if (musicPlayer != null) {
                musicPlayer.stop(); // Stop current music before playing new
            }
            musicPlayer = new MediaPlayer(music);
            musicPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop indefinitely
            musicPlayer.play();
        } catch (Exception e) {
            System.err.println("Unable to play music file: " + filePath);
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
