package com.sithumofficial.melomind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;

public class PlaybackActivity extends AppCompatActivity {

    ImageButton back;

    SwitchCompat checkglaples,checknormalizevolume,checkautoplay;

    private static final String PREF_NAME = "MyPrefs";
    private static final String PREF_KEY_VOLUME_NORMALIZE = "volumeNormalize";
    private static final String PREF_KEY_GAPLES_STATUS = "gaplesStatus";
    private static final String PREF_KEY_AUTOPLAY_STATUS = "autoplayStatus";

    private SharedPreferences sharedPreferences;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);

        back = findViewById(R.id.btnback);
        checkglaples = findViewById(R.id.checkglaples);
        checknormalizevolume = findViewById(R.id.checknormalizevolume);
        checkautoplay = findViewById(R.id.checkautoplay);

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Restore the switch state from SharedPreferences
        boolean isVolumeNormalizeOn = sharedPreferences.getBoolean(PREF_KEY_VOLUME_NORMALIZE, false);
        boolean isGaplesOn = sharedPreferences.getBoolean(PREF_KEY_GAPLES_STATUS, false);
        boolean isAutoplayOn = sharedPreferences.getBoolean(PREF_KEY_AUTOPLAY_STATUS, false);
        checknormalizevolume.setChecked(isVolumeNormalizeOn);
        checkglaples.setChecked(isGaplesOn);
        checkautoplay.setChecked(isAutoplayOn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        checkglaples.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Save the switch state to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(PREF_KEY_GAPLES_STATUS, isChecked);
                editor.apply();
                // Handle the toggle state change here
                if (isChecked) {
                    // Gaples is enabled
                } else {
                    // Gaples is disabled
                }
            }
        });

        checknormalizevolume.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Save the switch state to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(PREF_KEY_VOLUME_NORMALIZE, isChecked);
                editor.apply();
                // Handle the toggle state change here
                if (isChecked) {
                    // Enable volume normalization
                    audioManager.setMode(AudioManager.MODE_NORMAL);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                } else {
                    // Disable volume normalization
                    audioManager.setMode(AudioManager.MODE_NORMAL);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                }
            }
        });

        checkautoplay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Save the switch state to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(PREF_KEY_AUTOPLAY_STATUS, isChecked);
                editor.apply();
                // Handle the toggle state change here
                if (isChecked) {

                } else {

                }
            }
        });
    }
}