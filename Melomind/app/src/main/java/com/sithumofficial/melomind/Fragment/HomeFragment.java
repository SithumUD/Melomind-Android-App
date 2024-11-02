package com.sithumofficial.melomind.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sithumofficial.melomind.AllSpecialItemsActivity;
import com.sithumofficial.melomind.Models.ItemModel;
import com.sithumofficial.melomind.Models.MainAdapter;
import com.sithumofficial.melomind.Models.PopularAdapter;
import com.sithumofficial.melomind.PlayActivity;
import com.sithumofficial.melomind.R;
import com.sithumofficial.melomind.Search;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    ImageView search;

    RecyclerView newrecycleview,popularrecycleview;

    DatabaseReference newlyRef,popularRef;

    AppCompatButton txtnewall;

    MainAdapter mainAdapter;

    PopularAdapter popularAdapter;

    private String itemId;
    private AdView mAdView;
    private ProgressBar popularprogressBar, newprogressBar;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        popularprogressBar = rootView.findViewById(R.id.popularprogressbar);
        newprogressBar = rootView.findViewById(R.id.newprogressbar);
        search = rootView.findViewById(R.id.btnsearch);
        txtnewall = rootView.findViewById(R.id.txtnewall);
        newrecycleview = rootView.findViewById(R.id.newlyrecycleview);
        popularrecycleview = rootView.findViewById(R.id.popularrecycleview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        newrecycleview.setLayoutManager(layoutManager);
        LinearLayoutManager secondlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        popularrecycleview.setLayoutManager(secondlayoutManager);


        newlyRef = FirebaseDatabase.getInstance().getReference().child("speeches");

        Query query = newlyRef.orderByChild("timestamp").limitToLast(100); // Adjust the limit as needed

        FirebaseRecyclerOptions<ItemModel> newoptions =
                new FirebaseRecyclerOptions.Builder<ItemModel>()
                        .setQuery(query, ItemModel.class)
                        .build();

        mainAdapter = new MainAdapter(newoptions);
        newrecycleview.setAdapter(mainAdapter);
        ((LinearLayoutManager) layoutManager).setReverseLayout(true);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);

        mainAdapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ItemModel item) {
                // Handle item click here, and navigate to PlayActivity
                Intent intent = new Intent(getContext(), PlayActivity.class);
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

        txtnewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AllSpecialItemsActivity.class);
                intent.putExtra("type","NEW");
                startActivity(intent);
            }
        });




        popularRef = FirebaseDatabase.getInstance().getReference().child("speeches");

        Query popularquery = popularRef.orderByChild("favoriteCount").limitToLast(100); // Adjust the limit as needed

        FirebaseRecyclerOptions<ItemModel> popularoptions =
                new FirebaseRecyclerOptions.Builder<ItemModel>()
                        .setQuery(popularquery, ItemModel.class)
                        .build();

        popularAdapter = new PopularAdapter(popularoptions);
        popularrecycleview.setAdapter(popularAdapter);
        ((LinearLayoutManager) secondlayoutManager).setReverseLayout(true);
        ((LinearLayoutManager) secondlayoutManager).setStackFromEnd(true);

        popularAdapter.setOnItemClickListener(new PopularAdapter.OnItemClickListener() {
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


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Search.class);
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
        mainAdapter.startListening();
        mainAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                newprogressBar.setVisibility(View.GONE);
                txtnewall.setVisibility(View.GONE);
            }
        });
        popularAdapter.startListening();
        popularAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                popularprogressBar.setVisibility(View.GONE);
            }
        });
    }


}