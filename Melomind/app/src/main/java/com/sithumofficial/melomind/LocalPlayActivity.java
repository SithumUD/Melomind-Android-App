package com.sithumofficial.melomind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class LocalPlayActivity extends AppCompatActivity {

    String item_audio_url,itemtitle,item_cover_image_url,item_speakers,item_selectedValue,item_duration,itemid;
    ImageView smallCoverImage;

    ImageButton btnback;
    TextView speakers, title, category, playingtime;
    AppCompatImageButton btnplay;

    SeekBar timelineSeekBar;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private Runnable updateSeekBar;
    private Handler updateHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_play);

        itemid = getIntent().getStringExtra("speech_id");
        itemtitle = getIntent().getStringExtra("speech_title");
        item_audio_url = getIntent().getStringExtra("speech_filepath");
        item_cover_image_url = getIntent().getStringExtra("speech_coverimage");
        item_speakers = getIntent().getStringExtra("speech_speakers");
        item_selectedValue = getIntent().getStringExtra("speech_category");
        item_duration = getIntent().getStringExtra("speech_duration");

        smallCoverImage = findViewById(R.id.smallCoverImage);
        speakers = findViewById(R.id.speakers);
        title = findViewById(R.id.title); // You should replace "title" with the actual ID of the title TextView
        category = findViewById(R.id.category); // You should replace "category" with the actual ID of the category TextView
        btnplay = findViewById(R.id.btnplay);
        timelineSeekBar = findViewById(R.id.timelineSeekBar);
        playingtime = findViewById(R.id.playingtime);
        btnback = findViewById(R.id.btnback);

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(item_audio_url); // Set the audio file URL
            mediaPlayer.prepare(); // Prepare the MediaPlayer
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception if the audio file cannot be prepared
        }

        title.setText(itemtitle);
        speakers.setText(item_speakers);
        category.setText(item_selectedValue);

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying) {
                    // Start playing the audio
                    mediaPlayer.start();
                    isPlaying = true;
                    btnplay.setImageResource(R.drawable.round_pause_24); // Change the button image to pause
                    updateSeekBar.run(); // Start updating the SeekBar and playing time

                } else {
                    // Pause the audio
                    mediaPlayer.pause();
                    isPlaying = false;
                    btnplay.setImageResource(R.drawable.round_play_arrow_24); // Change the button image to play
                    updateHandler.removeCallbacks(updateSeekBar); // Stop updating SeekBar and playing time
                }
            }
        });

        timelineSeekBar.setMax(mediaPlayer.getDuration());
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    timelineSeekBar.setProgress(currentPosition);
                    playingtime.setText(formatTime(currentPosition));
                    updateHandler.postDelayed(this, 1000); // Update every second
                }
            }
        };

        // Add this code after initializing your SeekBar timelineSeekBar
        timelineSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress); // Seek to the selected position when the user drags the SeekBar
                    playingtime.setText(formatTime(progress)); // Update the playing time TextView
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not needed for this implementation, but you can add actions here if desired.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not needed for this implementation, but you can add actions here if desired.
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Handle audio completion, e.g., show a message or perform an action
            }
        });
    }

    // Helper method to format time in minutes:seconds
    private String formatTime(int milliseconds) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                TimeUnit.MINUTES.toSeconds(minutes);
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release(); // Release the MediaPlayer when the activity is destroyed
        }
        updateHandler.removeCallbacks(updateSeekBar); // Remove callbacks to avoid memory leaks
    }
}