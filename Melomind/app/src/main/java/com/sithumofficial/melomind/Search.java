package com.sithumofficial.melomind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.sithumofficial.melomind.Models.ItemModel;
import com.sithumofficial.melomind.Models.MainAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Search extends AppCompatActivity {

    ImageButton back;
    RecyclerView searchresult;
    EditText txtsearch;
    MainAdapter searchAdapter;
    DatabaseReference databaseReference;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        back = findViewById(R.id.btnback);
        txtsearch = findViewById(R.id.txtsearch);
        searchresult = findViewById(R.id.searchresultview);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("speeches");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        searchresult.setLayoutManager(layoutManager);


        // Add a TextWatcher to detect text changes in the search input
        // Add a TextWatcher to detect text changes in the search input
        txtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Create a query based on user input
                Query query = databaseReference.orderByChild("title")
                        .startAt(s.toString())
                        .endAt(s.toString() + "\uf8ff");

                FirebaseRecyclerOptions<ItemModel> filteredOptions =
                        new FirebaseRecyclerOptions.Builder<ItemModel>()
                                .setQuery(query, ItemModel.class)
                                .build();
                searchAdapter = new MainAdapter(filteredOptions);
                searchAdapter.startListening();
                searchresult.setAdapter(searchAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
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
}