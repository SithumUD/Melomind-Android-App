package com.sithumofficial.melomind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MusicQualityActivity extends AppCompatActivity {

    ImageButton back;
    SwitchCompat downloadCellularToggle;
    LinearLayout btnequalizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_quality);

        // Initialize UI components
        back = findViewById(R.id.btnback);
        downloadCellularToggle = findViewById(R.id.btndownloadcelleular); // Initialize the toggle button
        btnequalizer = findViewById(R.id.btnequalizer);

        // Set an OnCheckedChangeListener for the toggle button
        downloadCellularToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle the toggle state change here
                if (isChecked) {
                    // Enable downloading over cellular data
                    allowDownloadOverCellularData();
                } else {
                    // Disable downloading over cellular data
                    disallowDownloadOverCellularData();
                }
            }
        });

        // Restore the toggle button state from app settings
        boolean isCellularDataAllowed = checkCellularDataPermission();
        downloadCellularToggle.setChecked(isCellularDataAllowed);

        // Back button click listener
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Function to check if cellular data usage is allowed
    private boolean checkCellularDataPermission() {
        SharedPreferences preferences = getSharedPreferences("MyAppSettings", MODE_PRIVATE);
        return preferences.getBoolean("cellular_data_allowed", false);
    }

    // Function to allow downloading over cellular data
    private void allowDownloadOverCellularData() {
        // Update your app's settings or set a flag to allow downloading over cellular data
        SharedPreferences preferences = getSharedPreferences("MyAppSettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("cellular_data_allowed", true);
        editor.apply();

        // Provide a confirmation message to the user
        Toast.makeText(MusicQualityActivity.this, "Downloading over cellular data is now allowed", Toast.LENGTH_SHORT).show();
    }

    // Function to disallow downloading over cellular data
    private void disallowDownloadOverCellularData() {
        // Update your app's settings or set a flag to disallow downloading over cellular data
        SharedPreferences preferences = getSharedPreferences("MyAppSettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("cellular_data_allowed", false);
        editor.apply();

        // Provide a confirmation message to the user
        Toast.makeText(MusicQualityActivity.this, "Downloading over cellular data is disabled", Toast.LENGTH_SHORT).show();
    }
}
