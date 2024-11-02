package com.sithumofficial.melomind.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.sithumofficial.melomind.R;
// Import the correct PlayFragment class

public class GenreFragment extends Fragment {

    public GenreFragment() {
        // Required empty public constructor
    }

    LinearLayout btnall,btnpersonaldevelopment,btnovercomingchallenges,btnleadershipandsuccess,btnentrepreneurship,btncreativityandinnovation,btnhealthandwellness,btnacademicandeducational,btnsocialimpact,btnselfconfidence;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_genre, container, false);

        btnall = rootView.findViewById(R.id.btnall); // Replace R.id.btnall with the actual ID of your button
        btnpersonaldevelopment = rootView.findViewById(R.id.btnpersonaldevelopment);
        btnovercomingchallenges = rootView.findViewById(R.id.btnovercomingchallenges);
        btnleadershipandsuccess = rootView.findViewById(R.id.btnleadershipandsuccess);
        btnentrepreneurship = rootView.findViewById(R.id.btnentrepreneurship);
        btncreativityandinnovation = rootView.findViewById(R.id.btncreativityandinnovation);
        btnhealthandwellness = rootView.findViewById(R.id.btnhealthandwellness);
        btnacademicandeducational = rootView.findViewById(R.id.btnacademicandeducational);
        btnsocialimpact = rootView.findViewById(R.id.btnsocialimpactandactivism);
        btnselfconfidence = rootView.findViewById(R.id.btnselfconfidence);


        btnall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all();
            }
        });
        btnpersonaldevelopment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personaldevelopment();
            }
        });
        btnovercomingchallenges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overcomingchallenges();
            }
        });
        btnleadershipandsuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leadershipandsuccess();
            }
        });
        btnentrepreneurship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entrepreneurship();
            }
        });
        btncreativityandinnovation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creativityandinnovation();
            }
        });
        btnhealthandwellness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                healthandwellness();
            }
        });
        btnacademicandeducational.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                academicandeducational();
            }
        });
        btnsocialimpact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socialimpact();
            }
        });
        btnselfconfidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selfconfidence();
            }
        });


        return rootView;
    }

    public void all(){
        ItemListFragment itemListFragment = new ItemListFragment();
        Bundle bundle = new Bundle();
        String variableName = "category";
        String variableValue = "All";
        bundle.putString(variableName, variableValue);
        itemListFragment.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, itemListFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void personaldevelopment(){
        ItemListFragment itemListFragment = new ItemListFragment();
        Bundle bundle = new Bundle();
        String variableName = "category";
        String variableValue = "Personal Development";
        bundle.putString(variableName, variableValue);
        itemListFragment.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, itemListFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void overcomingchallenges(){
        ItemListFragment itemListFragment = new ItemListFragment();
        Bundle bundle = new Bundle();
        String variableName = "category";
        String variableValue = "Overcoming Challenges";
        bundle.putString(variableName, variableValue);
        itemListFragment.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, itemListFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void leadershipandsuccess(){
        ItemListFragment itemListFragment = new ItemListFragment();
        Bundle bundle = new Bundle();
        String variableName = "category";
        String variableValue = "Leadership and Success";
        bundle.putString(variableName, variableValue);
        itemListFragment.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, itemListFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void entrepreneurship(){
        ItemListFragment itemListFragment = new ItemListFragment();
        Bundle bundle = new Bundle();
        String variableName = "category";
        String variableValue = "Entrepreneurship";
        bundle.putString(variableName, variableValue);
        itemListFragment.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, itemListFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void creativityandinnovation(){
        ItemListFragment itemListFragment = new ItemListFragment();
        Bundle bundle = new Bundle();
        String variableName = "category";
        String variableValue = "Creativity and Innovation";
        bundle.putString(variableName, variableValue);
        itemListFragment.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, itemListFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void healthandwellness(){
        ItemListFragment itemListFragment = new ItemListFragment();
        Bundle bundle = new Bundle();
        String variableName = "category";
        String variableValue = "Health and Wellness";
        bundle.putString(variableName, variableValue);
        itemListFragment.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, itemListFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void academicandeducational(){
        ItemListFragment itemListFragment = new ItemListFragment();
        Bundle bundle = new Bundle();
        String variableName = "category";
        String variableValue = "Academic and Educational";
        bundle.putString(variableName, variableValue);
        itemListFragment.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, itemListFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void socialimpact(){
        ItemListFragment itemListFragment = new ItemListFragment();
        Bundle bundle = new Bundle();
        String variableName = "category";
        String variableValue = "Social Impact and Activism";
        bundle.putString(variableName, variableValue);
        itemListFragment.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, itemListFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void selfconfidence(){
        ItemListFragment itemListFragment = new ItemListFragment();
        Bundle bundle = new Bundle();
        String variableName = "category";
        String variableValue = "Self-Confidence";
        bundle.putString(variableName, variableValue);
        itemListFragment.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, itemListFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
