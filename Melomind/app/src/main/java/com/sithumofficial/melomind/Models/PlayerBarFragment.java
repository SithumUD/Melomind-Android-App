package com.sithumofficial.melomind.Models;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

import com.sithumofficial.melomind.R;

public class PlayerBarFragment extends Fragment {
    private ImageView coverImage;
    private TextView songTitle,speakers;
    private AppCompatImageButton playPauseButton,favouritbutton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mini_player, container, false);

        // Initialize UI components
        coverImage = view.findViewById(R.id.item_cover_image);
        songTitle = view.findViewById(R.id.item_name);
        speakers = view.findViewById(R.id.speakers);
        favouritbutton = view.findViewById(R.id.btnfavourit);
        playPauseButton = view.findViewById(R.id.btnplay);

        // Set click listeners for playback controls
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle play/pause button click
                // Implement playback control logic here
            }
        });

        // Additional click listeners for next and previous buttons can be added here

        return view;
    }

    // Update the song details in the player bar
    public void updateSongDetails(String title, String artistAlbum) {
        songTitle.setText(title);
        speakers.setText(artistAlbum);
    }
}

