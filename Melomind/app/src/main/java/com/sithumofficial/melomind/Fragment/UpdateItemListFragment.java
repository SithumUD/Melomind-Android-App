package com.sithumofficial.melomind.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.sithumofficial.melomind.Models.ItemModel;
import com.sithumofficial.melomind.Models.MainAdapter;
import com.sithumofficial.melomind.PlayActivity;
import com.sithumofficial.melomind.R;
public class UpdateItemListFragment extends Fragment {

    public UpdateItemListFragment() {
        // Required empty public constructor
    }

    RecyclerView itemlist;
    TextView nodata;

    MainAdapter mainAdapter;



    private String itemId;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_update_item_list, container, false);

        itemlist = rootView.findViewById(R.id.item_list);
        itemlist.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar = rootView.findViewById(R.id.progress_bar);
        nodata = rootView.findViewById(R.id.txtemptydownload);

        Query query;
        query = FirebaseDatabase.getInstance().getReference().child("speeches");

        FirebaseRecyclerOptions<ItemModel> options =
                new FirebaseRecyclerOptions.Builder<ItemModel>()
                        .setQuery(query, ItemModel.class)
                        .build();

        mainAdapter = new MainAdapter(options);
        itemlist.setAdapter(mainAdapter);
        progressBar.setVisibility(View.GONE);

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
                intent.putExtra("collection_type", "All");
                startActivity(intent);
            }
        });

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