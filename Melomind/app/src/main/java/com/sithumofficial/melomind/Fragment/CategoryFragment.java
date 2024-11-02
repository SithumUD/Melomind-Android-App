package com.sithumofficial.melomind.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.sithumofficial.melomind.R;
import com.sithumofficial.melomind.Search;
import com.sithumofficial.melomind.databinding.FragmentCategoryBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class CategoryFragment extends Fragment {

    private FragmentCategoryBinding binding;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private int defaultButtonTextColor;

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        defaultButtonTextColor = binding.btngenre.getCurrentTextColor();

        // Set click listeners for the buttons
        binding.btnpopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the frame layout with Fragment1
                PopularFragment fragment1 = new PopularFragment();
                replaceFragment(fragment1);

                binding.btnpopular.setTextColor(Color.parseColor("#FB6580"));
                // Change text color of button2 to default color
                binding.btngenre.setTextColor(defaultButtonTextColor);
            }
        });

        binding.btngenre.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                // Replace the frame layout with Fragment2
                UpdateItemListFragment fragment2 = new UpdateItemListFragment();
                replaceFragment(fragment2);

                binding.btngenre.setTextColor(Color.parseColor("#FB6580"));
                // Change text color of button1 to default color
                binding.btnpopular.setTextColor(defaultButtonTextColor);
            }
        });
        PopularFragment fragment1 = new PopularFragment();
        replaceFragment(fragment1);

        binding.btnpopular.setTextColor(Color.parseColor("#FB6580"));
        // Set initial text color of button2 to default color
        binding.btngenre.setTextColor(defaultButtonTextColor);

        binding.btnsearch.setOnClickListener(new View.OnClickListener() {
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
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}