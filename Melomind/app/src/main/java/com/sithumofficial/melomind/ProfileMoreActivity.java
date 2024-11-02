package com.sithumofficial.melomind;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class ProfileMoreActivity extends AppCompatActivity {

    ImageButton back;

    LinearLayout btnplayback,btnmusicquality,btnaboutus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_more);

        btnplayback = findViewById(R.id.btnplayback);
        btnmusicquality = findViewById(R.id.btnMusicquality);
        btnaboutus = findViewById(R.id.btnaboutus);
        back = findViewById(R.id.btnback);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnplayback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileMoreActivity.this, PlaybackActivity.class);
                startActivity(intent);
            }
        });

        btnmusicquality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileMoreActivity.this, MusicQualityActivity.class);
                startActivity(intent);
            }
        });

        btnaboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileMoreActivity.this, AboutUsActivity.class);
                startActivity(intent);
            }
        });
    }
}