package com.giovannimartinus.sounddemo;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {


    // create an object
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    public static final String TAG = MainActivity.class.getName();

    // create an instance of class
    SoundBoard soundBoard = new SoundBoard();


    // class containing all sound media controls
    class SoundBoard {

        private boolean muted = false;

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

        // mute/unmute the media file and change muteButton's src
        public void muteButton(int x) {

            try {
                ImageButton volumeButton = (ImageButton) findViewById(R.id.volumeDisplayButton);
                ImageButton muteIcon = (ImageButton) findViewById(R.id.muteButton);


                // if muted set volume back to it's position
                if (muted == true) {
                    mediaPlayer.setVolume(1,1);
                    muted = false;

                    // set the ImageResource back to their original src
                    if (x == 0) {
                        muteIcon.setImageResource(R.drawable.highvolume);
                        volumeButton.setImageResource(R.drawable.highvolume);
                    } else if (x == 1) {
                        muteIcon.setImageResource(R.drawable.novolume);
                        volumeButton.setImageResource(R.drawable.novolume);
                    } else if (x == 2) {
                        muteIcon.setImageResource(R.drawable.lowvolume);
                        volumeButton.setImageResource(R.drawable.lowvolume);
                    } else if (x == 3) {
                        muteIcon.setImageResource(R.drawable.mediumvolume);
                        volumeButton.setImageResource(R.drawable.mediumvolume);
                    }
                // else if it's not muted then mute the volume and change src
                } else if (muted == false) {
                    mediaPlayer.setVolume(0,0);
                    muted = true;

                    muteIcon.setImageResource(R.drawable.novolume);
                    volumeButton.setImageResource(R.drawable.novolume);
                }
            } catch (Exception e){
                Log.e(TAG, "Volume should be muted or unmuted.", e);
            }

        }

        // toggle on/off the audio button
        public void audioButton(View view) {
            LinearLayout volumeLayout = (LinearLayout) findViewById(R.id.volumeLayout);

            // if the visibility is visible than set as invisible - vice versa
            if (volumeLayout.getVisibility() == View.INVISIBLE) {
                volumeLayout.setVisibility(View.VISIBLE);
            } else if (volumeLayout.getVisibility() == View.VISIBLE) {
                volumeLayout.setVisibility(View.INVISIBLE);
            }

        }

        // change the audio button's icon based on volume level
        public void audioButtonView(int x) {

            ImageButton volumeButton = (ImageButton) findViewById(R.id.volumeDisplayButton);
            ImageButton muteButton = (ImageButton) findViewById(R.id.muteButton);


            // create integers for if statement
            int volume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int third = volume / 3;

            if (muted == false) {
                // if the x is between volume - 1/3 or itself
                if (x == volume || x >= (volume - third)) {
                    volumeButton.setImageResource(R.drawable.highvolume);
                    muteButton.setImageResource(R.drawable.highvolume);
                    muteButton.setTag(0);
                    // else if the x is at 0
                } else if (x == 0) {
                    volumeButton.setImageResource(R.drawable.novolume);
                    muteButton.setImageResource(R.drawable.novolume);
                    muteButton.setTag(1);
                    // else if x is between 1 and 1/3 of volume
                } else if (x >= 1 && x < third) {
                    volumeButton.setImageResource(R.drawable.lowvolume);
                    muteButton.setImageResource(R.drawable.lowvolume);
                    muteButton.setTag(2);
                    // else if x is between 1/3 of volume and volume - 1/3
                } else if (x < (volume - third) && x >= third) {
                    volumeButton.setImageResource(R.drawable.mediumvolume);
                    muteButton.setImageResource(R.drawable.mediumvolume);
                    muteButton.setTag(3);
                }
            } // alternative clause not required


        }

        // display scrubberSeekBar progress when clicked
        public void scrubberTextView(int x) {

            TextView scrubberTextView = (TextView) findViewById(R.id.scrubberTextView);
            SeekBar scrubberSeekBar = (SeekBar) findViewById(R.id.scrubberSeekBar);

            // w/ TimeUnit API convert milliseconds into hour:minute:second time format
            String hourMinSec = String.format("%02d:%02d:0%2d", TimeUnit.MILLISECONDS.toHours(x),
                    TimeUnit.MILLISECONDS.toMinutes(x) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(x)),
                    TimeUnit.MILLISECONDS.toSeconds(x) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(x))
            );

            // display selected time of scrubber
            scrubberTextView.setText(hourMinSec);
            // get the distance from the sides
            int distance = (scrubberSeekBar.getWidth() - scrubberSeekBar.getThumbOffset()) / 2;
            // get the forward progress movement
            int forward = scrubberSeekBar.getMax() - (scrubberSeekBar.getMax() - x);
            // setX to move along with progress
            scrubberTextView.setX(distance + (distance + forward) / (scrubberSeekBar.getThumbOffset() / 2));

        }

        // allow volume control with SeekBar widget
        public void volumeControl() {

            try {

                // create objects of STREAM_MUSIC
                int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

                // assign view to object
                SeekBar volumeSeekBar = (SeekBar) findViewById(R.id.volumeSeekBar);

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

        // allow positioning on timeline
        public void scrubberControl() {

            try {

                // assign view to object
                final SeekBar scrubberSeekBar = (SeekBar) findViewById(R.id.scrubberSeekBar);

                final TextView scrubberTextView = (TextView) findViewById(R.id.scrubberTextView);

                // set the max value of the SeekBar to the length of the media file
                scrubberSeekBar.setMax(mediaPlayer.getDuration());

                // update SeekBar with location of media file
                new Timer().scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        // run code in here immediately and every second
                        scrubberSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                    }
                // number of seconds before run for first time,
                // number of milliseconds between successive calls
                }, 0, 100);

                scrubberSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    // while currently clicked
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        // move to current time
                        mediaPlayer.seekTo(progress);

                        // pass progress to scrubberTextView
                        soundBoard.scrubberTextView(progress);

                        // display to log the SeekBar value
                        //Log.i("Scrubber Value", Integer.toString(progress));

                    }

                    // at the start of being clicked
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                        scrubberTextView.setVisibility(View.VISIBLE);
                        mediaPlayer.pause();

                    }

                    // at the end of being clicked
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                        scrubberTextView.setVisibility(View.INVISIBLE);
                        mediaPlayer.start();

                    }
                });


            } catch (Exception e) {
                Log.e(TAG, "SeekBar should position timeline.", e);
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

    public void muteAudio(View view) {
        ImageButton muteIcon = (ImageButton) findViewById(R.id.muteButton);
        int muteTag = Integer.parseInt(muteIcon.getTag().toString());
        soundBoard.muteButton(muteTag);
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
        soundBoard.scrubberControl();
    }
}
