package com.sithumofficial.melomind.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sithumofficial.melomind.R;

public class ListFragment extends Fragment {

    public ListFragment() {
        // Required empty public constructor
    }

    TextView name;
    LinearLayout item;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        // Retrieve the variable from the arguments bundle
        String category = getArguments().getString("category");

        name = rootView.findViewById(R.id.item_name);
        item = rootView.findViewById(R.id.single_item);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        name.setText(category);

        // Use the variable as needed

        return rootView;
    }
}