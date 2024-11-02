package com.sithumofficial.melomind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.sithumofficial.melomind.Fragment.CategoryFragment;
import com.sithumofficial.melomind.Fragment.DownloadFragment;
import com.sithumofficial.melomind.Fragment.HomeFragment;
import com.sithumofficial.melomind.Fragment.ProfileFragment;
import com.sithumofficial.melomind.Models.PlayerBarFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainHome extends AppCompatActivity {

    private Fragment fragment1;
    private Fragment fragment2;
    private Fragment fragment3;
    private Fragment fragment4;

    private MediaPlayer mediaPlayer;
    private ImageButton playPauseButton;
    private SeekBar seekBar;
    private Handler handler;
    private ConstraintLayout card_layout;
    private boolean isPlaying = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navItemSelectedListener);

        mediaPlayer = new MediaPlayer();
        handler = new Handler();

        card_layout = findViewById(R.id.card_layout);
        playPauseButton = findViewById(R.id.btnplay);
        seekBar = findViewById(R.id.timeline);

        // Initialize SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("audiostatus", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        isPlaying = sharedPreferences.getBoolean("isPlaying", false);

        fragment1 = new HomeFragment();
        fragment2 = new CategoryFragment();
        fragment3 = new DownloadFragment();
        fragment4 = new ProfileFragment();

        setFragment(fragment1);

        // Set up play/pause button click listener
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying) {
                    // Start playing the audio
                    mediaPlayer.start();
                    editor.putBoolean("isPlaying", true);
                    playPauseButton.setImageResource(R.drawable.round_pause_24); // Change the button image to pause
                } else {
                    // Pause the audio
                    mediaPlayer.pause();
                    editor.putBoolean("isPlaying", false);
                    playPauseButton.setImageResource(R.drawable.round_play_arrow_24); // Change the button image to play
                    updateSeekBar();
                }
            }
        });

        // Set up seekbar change listener
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // No action needed
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // No action needed
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    int itemId = item.getItemId();
                    if (itemId == R.id.home) {
                        setFragment(fragment1);
                        return true;
                    } else if (itemId == R.id.category) {
                        setFragment(fragment2);
                        return true;
                    } else if (itemId == R.id.download) {
                        setFragment(fragment3);
                        return true;
                    } else if (itemId == R.id.profile) {
                        setFragment(fragment4);
                        return true;
                    }
                    return false;
                }
            };


    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    // Update seekbar progress periodically
    private void updateSeekBar() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    updateSeekBar();
                }
            }
        }, 1000); // Update every second
    }


}