package com.sithumofficial.melomind.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sithumofficial.melomind.Adapter.DownloadAdapter;
import com.sithumofficial.melomind.Localdatabase.Databasehelper;
import com.sithumofficial.melomind.Models.MainAdapter;
import com.sithumofficial.melomind.R;
import com.sithumofficial.melomind.Search;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class DownloadFragment extends Fragment {

    public DownloadFragment() {
        // Required empty public constructor
    }

    ImageView search;

    private View view;

    RecyclerView itemlist;

    private MainAdapter mainAdapter;
    private DatabaseReference databaseReference,recentlyPlayedRef;

    FirebaseAuth mAuth;

    private ProgressBar progressBar;

    private String itemId;
    FirebaseUser currentUser;
    Databasehelper mydb;
    ArrayList<String> speech_id,speech_title,speech_speakers,speech_category,speech_duration,speech_cover_image,speech_filepath;
    DownloadAdapter downloadAdapter;
    private AdView mAdView;
    TextView nodata;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_download, container, false);

        search = view.findViewById(R.id.btnsearch);
        itemlist = view.findViewById(R.id.item_list);
        progressBar = view.findViewById(R.id.progress_bar);
        nodata = view.findViewById(R.id.txtemptydownload);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Search.class);
                startActivity(intent);
            }
        });

        mydb = new Databasehelper(requireContext());
        speech_id = new ArrayList<>();
        speech_title = new ArrayList<>();
        speech_speakers = new ArrayList<>();
        speech_category = new ArrayList<>();
        speech_duration = new ArrayList<>();
        speech_cover_image = new ArrayList<>();
        speech_filepath = new ArrayList<>();

        storeDataInArray();

        downloadAdapter = new DownloadAdapter(requireContext(),speech_id,speech_title,speech_speakers,speech_category,speech_duration,speech_cover_image,speech_filepath);
        itemlist.setAdapter(downloadAdapter);
        itemlist.setLayoutManager(new LinearLayoutManager(requireContext()));

        progressBar.setVisibility(View.GONE);


        MobileAds.initialize(requireContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        return view;
    }

    public void storeDataInArray(){
        Cursor cursor = mydb.readAllData();
        if (cursor.getCount() == 0){
            nodata.setVisibility(View.VISIBLE);
        }
        else {
            while (cursor.moveToNext()){
                speech_id.add(cursor.getString(0));
                speech_title.add(cursor.getString(1));
                speech_speakers.add(cursor.getString(2));
                speech_category.add(cursor.getString(3));
                speech_duration.add(cursor.getString(4));
                speech_cover_image.add(cursor.getString(5));
                speech_filepath.add(cursor.getString(6));
            }
        }
    }

}