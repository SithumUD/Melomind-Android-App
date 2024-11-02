package com.sithumofficial.melomind.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.sithumofficial.melomind.Models.ItemModel;
import com.sithumofficial.melomind.Models.MainAdapter;
import com.sithumofficial.melomind.PlayActivity;
import com.sithumofficial.melomind.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class PopularFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private MainAdapter mainAdapter;
    private DatabaseReference databaseReference;

    private ProgressBar progressBar;

    private String itemId;



    public PopularFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("speeches");

// Query to retrieve items in descending order of favoriteCount
        Query query = databaseReference.orderByChild("favoriteCount").limitToLast(100);

// Configure options for FirebaseRecyclerAdapter
        FirebaseRecyclerOptions<ItemModel> options =
                new FirebaseRecyclerOptions.Builder<ItemModel>()
                        .setQuery(query, ItemModel.class)
                        .build();

// Initialize the MainAdapter with the options
        mainAdapter = new MainAdapter(options);

// Set an item click listener for the adapter
        mainAdapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ItemModel item) {
                itemId = item.getItemId();

                // Handle item click here, and navigate to PlayActivity
                Intent intent = new Intent(getContext(), PlayActivity.class);
                intent.putExtra("item_id", itemId);
                intent.putExtra("item_title", item.getTitle());
                intent.putExtra("item_audio_url", item.getAudio_url());
                intent.putExtra("item_cover_image_url", item.getCover_image_url());
                intent.putExtra("item_speakers", item.getSpeakers());
                intent.putExtra("item_selectedValue", item.getSelectedValue());
                intent.putExtra("item_duration", item.getDuration());
                intent.putExtra("collection_type","popular");
                startActivity(intent);
            }
        });

// Reverse the order of items in RecyclerView


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_popular, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mainAdapter);

        progressBar = view.findViewById(R.id.progress_bar);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Start listening for Firebase Realtime Database changes
        mainAdapter.startListening();
        mainAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                hideProgressBar();
            }
        });
    }

    // Add this method to hide the progress bar
    private void hideProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }
}