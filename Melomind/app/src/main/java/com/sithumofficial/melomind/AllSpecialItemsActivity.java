package com.sithumofficial.melomind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sithumofficial.melomind.Models.ItemModel;
import com.sithumofficial.melomind.Models.MainAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AllSpecialItemsActivity extends AppCompatActivity {

    TextView title, nodata;
    ImageButton back;
    ImageView search;

    RecyclerView itemlist;

    MainAdapter mainAdapter;
    private AdView mAdView;

    DatabaseReference newlyRef, recentlyPlayedRef, favoriteRef;
    private String itemId;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_special_items);

        String passtype = getIntent().getStringExtra("type");

        search = findViewById(R.id.btnsearch);
        back = findViewById(R.id.btnback);
        title = findViewById(R.id.title);
        itemlist = findViewById(R.id.item_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        itemlist.setLayoutManager(layoutManager);
        title.setText(passtype);
        progressBar = findViewById(R.id.progress_bar);
        nodata = findViewById(R.id.txtemptydownload);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();

        if ("NEW".equals(passtype)){
            newlyRef = FirebaseDatabase.getInstance().getReference().child("speeches");

            Query query = newlyRef.orderByChild("timestamp").limitToLast(100); // Adjust the limit as needed

            FirebaseRecyclerOptions<ItemModel> newoptions =
                    new FirebaseRecyclerOptions.Builder<ItemModel>()
                            .setQuery(query, ItemModel.class)
                            .build();

            mainAdapter = new MainAdapter(newoptions);
            itemlist.setAdapter(mainAdapter);
            ((LinearLayoutManager) layoutManager).setReverseLayout(true);
            ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
            progressBar.setVisibility(View.GONE);

            mainAdapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(ItemModel item) {
                    // Handle item click here, and navigate to PlayActivity
                    Intent intent = new Intent(AllSpecialItemsActivity.this, PlayActivity.class);
                    intent.putExtra("item_id", item.getItemId());
                    intent.putExtra("item_title", item.getTitle());
                    intent.putExtra("item_audio_url", item.getAudio_url());
                    intent.putExtra("item_cover_image_url", item.getCover_image_url());
                    intent.putExtra("item_speakers", item.getSpeakers());
                    intent.putExtra("item_selectedValue", item.getSelectedValue());
                    intent.putExtra("item_duration", item.getDuration());
                    intent.putExtra("collection_type","new");
                    startActivity(intent);
                }
            });
        }
        else if ("FAVORITE".equals(passtype)){
            // Initialize favoriteRef
            favoriteRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("favorites");

// Create FirebaseRecyclerOptions for favorite items
            FirebaseRecyclerOptions<ItemModel> favoriteOptions =
                    new FirebaseRecyclerOptions.Builder<ItemModel>()
                            .setQuery(favoriteRef, ItemModel.class)
                            .build();

// Create a new instance of MainAdapter for favorites
            mainAdapter = new MainAdapter(favoriteOptions);
            itemlist.setAdapter(mainAdapter);
            progressBar.setVisibility(View.GONE);

            mainAdapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(ItemModel item) {
                    // Handle item click here, and navigate to PlayActivity
                    Intent intent = new Intent(AllSpecialItemsActivity.this, PlayActivity.class);
                    intent.putExtra("item_id", item.getItemId());
                    intent.putExtra("item_title", item.getTitle());
                    intent.putExtra("item_audio_url", item.getAudio_url());
                    intent.putExtra("item_cover_image_url", item.getCover_image_url());
                    intent.putExtra("item_speakers", item.getSpeakers());
                    intent.putExtra("item_selectedValue", item.getSelectedValue());
                    intent.putExtra("item_duration", item.getDuration());
                    intent.putExtra("collection_type", "favorite");
                    startActivity(intent);
                }
            });
        }
        else if ("RECENTLY_VIEWED".equals(passtype)){
            recentlyPlayedRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("recently_played");

// Query to retrieve items in descending order of timestamp
            Query query = recentlyPlayedRef.orderByChild("timestamp").limitToLast(100); // You can adjust the limit as needed

            FirebaseRecyclerOptions<ItemModel> recentoptions =
                    new FirebaseRecyclerOptions.Builder<ItemModel>()
                            .setQuery(query, ItemModel.class)
                            .build();

            mainAdapter = new MainAdapter(recentoptions);
            itemlist.setAdapter(mainAdapter);
            ((LinearLayoutManager) layoutManager).setReverseLayout(true);
            ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
            progressBar.setVisibility(View.GONE);

            mainAdapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(ItemModel item) {
                    itemId = item.getItemId();

                    // Handle item click here, and navigate to PlayActivity
                    Intent intent = new Intent(AllSpecialItemsActivity.this, PlayActivity.class);
                    intent.putExtra("item_id", itemId);
                    intent.putExtra("item_title", item.getTitle());
                    intent.putExtra("item_audio_url", item.getAudio_url());
                    intent.putExtra("item_cover_image_url", item.getCover_image_url());
                    intent.putExtra("item_speakers", item.getSpeakers());
                    intent.putExtra("item_selectedValue", item.getSelectedValue());
                    intent.putExtra("item_duration", item.getDuration());
                    intent.putExtra("collection_type", "recent");
                    startActivity(intent);
                }
            });
        }



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllSpecialItemsActivity.this, Search.class);
                startActivity(intent);
            }
        });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mainAdapter != null) {
            mainAdapter.startListening();
        }
        else {
            nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mainAdapter != null) {
            mainAdapter.stopListening();
        }
    }
}