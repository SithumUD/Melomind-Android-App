package com.sithumofficial.melomind.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sithumofficial.melomind.Models.ItemModel;
import com.sithumofficial.melomind.Models.MainAdapter;
import com.sithumofficial.melomind.PlayActivity;
import com.sithumofficial.melomind.R;
import com.sithumofficial.melomind.Search;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ItemListFragment extends Fragment {

    public ItemListFragment() {
        // Required empty public constructor
    }

    TextView title, nodata;
    ImageButton back;
    ImageView search;

    RecyclerView itemlist;

    MainAdapter mainAdapter;



    private String itemId;
    private AdView mAdView;
    private ProgressBar progressBar;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_item_list, container, false);

        String passcategory = getArguments().getString("category");


        search = rootView.findViewById(R.id.btnsearch);
        back = rootView.findViewById(R.id.btnback);
        title = rootView.findViewById(R.id.title);
        progressBar = rootView.findViewById(R.id.progress_bar);
        itemlist = rootView.findViewById(R.id.item_list);
        nodata = rootView.findViewById(R.id.txtemptydownload);
        itemlist.setLayoutManager(new LinearLayoutManager(getContext()));
        title.setText(passcategory);

        Query query;
        if ("All".equals(passcategory)) {
            // If category is "All," fetch all data from "speeches"
            query = FirebaseDatabase.getInstance().getReference().child("speeches");
        } else {
            // If category is not "All," fetch data with the specified category
            query = FirebaseDatabase.getInstance().getReference().child("speeches")
                    .orderByChild("selectedValue")
                    .equalTo(passcategory);
        }


        FirebaseRecyclerOptions<ItemModel> options =
                new FirebaseRecyclerOptions.Builder<ItemModel>()
                        .setQuery(query, ItemModel.class)
                        .build();

        mainAdapter = new MainAdapter(options);
        itemlist.setAdapter(mainAdapter);
        progressBar.setVisibility(View.GONE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                // Pop the current Fragment from the back stack
                fragmentManager.popBackStack();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Search.class);
                startActivity(intent);
            }
        });




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
                intent.putExtra("collection_type", passcategory);
                startActivity(intent);
            }
        });

        MobileAds.initialize(requireContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        return rootView;
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
}
