package com.giovannimartinus.sounddemo;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {


    // create an object
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private SeekBar volumeSeekBar;

    public static final String TAG = MainActivity.class.getName();

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

        // toggle on/off the audio button
        public void audioButton(View view) {
            SeekBar volumeSeekBar = (SeekBar) findViewById(R.id.seekBar);

            // if the visibility is visible than set as invisible - vice versa
            if (volumeSeekBar.getVisibility() == View.INVISIBLE) {
                volumeSeekBar.setVisibility(View.VISIBLE);
            } else if (volumeSeekBar.getVisibility() == View.VISIBLE) {
                volumeSeekBar.setVisibility(View.INVISIBLE);
            }

        }

        // change the audio button's icon based on volume level
        public void audioButtonView(int x) {
            ImageButton volumeButton = (ImageButton) findViewById(R.id.imageButton);

            // if the volume is at max
            if (x == audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)) {
                volumeButton.setImageResource(R.drawable.highvolume);
            // else if the volume is at 0
            } else if (x == 0) {
                volumeButton.setImageResource(R.drawable.novolume);
            }
        }

        // allow volume control with SeekBar widget
        public void volumeControl() {

            try {

                // create objects of STREAM_MUSIC
                int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);


                // assign view to object
                volumeSeekBar = (SeekBar) findViewById(R.id.seekBar);


                // set max volume to the maximum volume of the system
                volumeSeekBar.setMax(maxVolume);


                // set current (progressing) volume
                volumeSeekBar.setProgress(currentVolume);

                // listen when/while the SeekBar is clicked
                volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    // while currently clicked
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        // set the current volume on SeekBar
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);

                        // display to log the SeekBar value
                        Log.i("SeekBar Value", Integer.toString(progress));

                        // call class' method and pass progress
                        soundBoard.audioButtonView(progress);
                    }

                    // at the start of being clicked
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    // at the end of being clicked
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }

                });

            // call exception if try doesn't work
            } catch (Exception e) {
                Log.e(TAG, "SeekBar should adjust volume when dragged.", e);
            }

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

    public void toggleAudio(View view) {
        soundBoard.audioButton(view);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);


        // assign a media file to mediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.crickets);

        // pass argument to instance of AudioManager
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // call class' method
        soundBoard.volumeControl();
    }
}
