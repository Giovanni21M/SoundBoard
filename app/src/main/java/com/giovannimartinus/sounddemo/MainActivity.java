package com.giovannimartinus.sounddemo;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    // create a MediaPlayer variable
    MediaPlayer mediaPlayer;

    // create an instance of class SoundBoard
    SoundBoard soundBoard = new SoundBoard();


    // class containing all sound media controls
    class SoundBoard {

        // play the media file
        public void playButton() {
            mediaPlayer.start();
        }

        // pause the media file
        public void pauseButton() {
            mediaPlayer.pause();
        }

        // pause the media file and set it back to the beginning
        public void stopButton() {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
        }

    }


    /* onClick methods */
    public void playAudio(View view){
        soundBoard.playButton();
    }

    public void pauseAudio(View view) {
        soundBoard.pauseButton();
    }

    public void stopAudio(View view) {
        soundBoard.stopButton();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // assign a media file to mediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.crickets);
    }
}
