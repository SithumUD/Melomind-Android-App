package com.sithumofficial.melomind.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sithumofficial.melomind.AboutUsActivity;
import com.sithumofficial.melomind.AllSpecialItemsActivity;
import com.sithumofficial.melomind.Models.FavoriteAdapter;
import com.sithumofficial.melomind.Models.ItemModel;
import com.sithumofficial.melomind.Models.MainAdapter;
import com.sithumofficial.melomind.PlayActivity;
import com.sithumofficial.melomind.R;
import com.sithumofficial.melomind.Search;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    ImageView search, more;
    TextView usernametextview, useremail, txtrecentlyplayed, txtfavorite, txtemptypopular, txtemptyrecent;
    CircleImageView profileimage;
    RecyclerView favouriterecycleview, recentlyrecycleview;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference usersRef, recentlyPlayedRef, favoriteRef;

    MainAdapter mainAdapter;
    FavoriteAdapter favoriteAdapter;

    private String itemId;

    private ProgressBar popularprogressBar, recentprogressBar;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        search = rootView.findViewById(R.id.btnsearch);
        popularprogressBar = rootView.findViewById(R.id.popularprogressbar);
        recentprogressBar = rootView.findViewById(R.id.recentprogressbar);
        txtrecentlyplayed = rootView.findViewById(R.id.txtrecentlyplayed);
        txtfavorite = rootView.findViewById(R.id.txtfavorite);
        more = rootView.findViewById(R.id.btnmore);
        txtemptypopular = rootView.findViewById(R.id.txtemptyfavorite);
        txtemptyrecent = rootView.findViewById(R.id.txtemptyrecent);
        usernametextview = rootView.findViewById(R.id.username);
        useremail = rootView.findViewById(R.id.useremail);
        profileimage = rootView.findViewById(R.id.profile_image);
        favouriterecycleview = rootView.findViewById(R.id.favouriterecycleview);
        recentlyrecycleview = rootView.findViewById(R.id.recentlyrecycleview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recentlyrecycleview.setLayoutManager(layoutManager);
        LinearLayoutManager secondlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        favouriterecycleview.setLayoutManager(secondlayoutManager);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        if (currentUser != null) {
            String userId = currentUser.getUid();
            usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Retrieve user data
                        String username = dataSnapshot.child("username").getValue(String.class);
                        String email = dataSnapshot.child("mail").getValue(String.class);

                        // Set the retrieved data to your TextViews
                        usernametextview.setText(username);
                        useremail.setText(email);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            recentlyPlayedRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("recently_played");

// Query to retrieve items in descending order of timestamp
            Query query = recentlyPlayedRef.orderByChild("timestamp").limitToLast(100); // You can adjust the limit as needed

            FirebaseRecyclerOptions<ItemModel> recentoptions =
                    new FirebaseRecyclerOptions.Builder<ItemModel>()
                            .setQuery(query, ItemModel.class)
                            .build();

            mainAdapter = new MainAdapter(recentoptions);
            recentlyrecycleview.setAdapter(mainAdapter);
            ((LinearLayoutManager) layoutManager).setReverseLayout(true);
            ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
            recentprogressBar.setVisibility(View.GONE);

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
                    intent.putExtra("collection_type", "recent");
                    startActivity(intent);
                }
            });


            // Initialize favoriteRef
            favoriteRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("favorites");

// Create FirebaseRecyclerOptions for favorite items
            FirebaseRecyclerOptions<ItemModel> favoriteOptions =
                    new FirebaseRecyclerOptions.Builder<ItemModel>()
                            .setQuery(favoriteRef, ItemModel.class)
                            .build();

// Create a new instance of MainAdapter for favorites
            favoriteAdapter = new FavoriteAdapter(favoriteOptions);
            favouriterecycleview.setAdapter(favoriteAdapter);
            popularprogressBar.setVisibility(View.GONE);

            favoriteAdapter.setOnItemClickListener(new FavoriteAdapter.OnItemClickListener() {
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
                    intent.putExtra("collection_type", "favorite");
                    startActivity(intent);
                }
            });

        } else {
            // User is not logged in, display a toast
            Toast.makeText(getContext(), "Please log in to view your profile.", Toast.LENGTH_SHORT).show();
        }

        txtfavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AllSpecialItemsActivity.class);
                intent.putExtra("type","FAVORITE");
                startActivity(intent);
            }
        });

        txtrecentlyplayed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AllSpecialItemsActivity.class);
                intent.putExtra("type","RECENTLY_VIEWED");
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

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AboutUsActivity.class);
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
        if (favoriteAdapter != null) {
            favoriteAdapter.startListening();
        }
    }



}
