package com.sithumofficial.melomind;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.room.Room;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.Query;
import com.sithumofficial.melomind.ExtraClasses.MediaPlayerService;
import com.sithumofficial.melomind.Localdatabase.Databasehelper;
import com.sithumofficial.melomind.Models.AppDatabase;
import com.sithumofficial.melomind.Models.ItemDao;
import com.sithumofficial.melomind.Models.ItemModel;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.Manifest;

public class PlayActivity extends AppCompatActivity {

    ImageView smallCoverImage, btnfavorit, btndownload;

    ImageButton btnback;
    TextView speakers, title, category, playingtime;
    AppCompatImageButton btnplay, btnforward, btnbackward;

    SeekBar timelineSeekBar;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private Runnable updateSeekBar;
    private Handler updateHandler = new Handler();

    private int favoriteCount = 0;

    private DatabaseReference itemRef;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    private static final int PERMISSION_REQUEST_CODE = 101;

    String item_audio_url, itemid, itemtitle, item_cover_image_url, item_speakers, item_selectedValue, item_duration, collection_type;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private AppDatabase appDatabase;

    private List<ItemModel> itemList = new ArrayList<>();
    private int currentItemPosition = 0;

    private AdView TopAdView;
    private RewardedAd rewardedAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        itemid = getIntent().getStringExtra("item_id");
        itemtitle = getIntent().getStringExtra("item_title");
        item_audio_url = getIntent().getStringExtra("item_audio_url");
        item_cover_image_url = getIntent().getStringExtra("item_cover_image_url");
        item_speakers = getIntent().getStringExtra("item_speakers");
        item_selectedValue = getIntent().getStringExtra("item_selectedValue");
        item_duration = getIntent().getStringExtra("item_duration");
        collection_type = getIntent().getStringExtra("collection_type");

        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "melomind-db").build();
        ItemDao itemDao = appDatabase.itemDao();

        initializeItemList();

        itemRef = FirebaseDatabase.getInstance().getReference().child("speeches").child(itemid);

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(item_audio_url); // Set the audio file URL
            mediaPlayer.prepare(); // Prepare the MediaPlayer
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception if the audio file cannot be prepared
        }

        // Initialize your ImageView and TextViews
        smallCoverImage = findViewById(R.id.smallCoverImage);
        speakers = findViewById(R.id.speakers);
        title = findViewById(R.id.title); // You should replace "title" with the actual ID of the title TextView
        category = findViewById(R.id.category); // You should replace "category" with the actual ID of the category TextView
        btnplay = findViewById(R.id.btnplay);
        btnforward = findViewById(R.id.btnforward);
        btnbackward = findViewById(R.id.btnbackward);
        timelineSeekBar = findViewById(R.id.timelineSeekBar);
        playingtime = findViewById(R.id.playingtime);
        btnback = findViewById(R.id.btnback);
        btnfavorit = findViewById(R.id.btnfavourit);
        btndownload = findViewById(R.id.btndownload);

        title.setText(itemtitle);
        speakers.setText(item_speakers);
        category.setText(item_selectedValue);
        Picasso.get()
                .load(item_cover_image_url) // Replace with the actual URL of the cover image
                .into(smallCoverImage);

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Check if the item is in the user's favorites and set the button image accordingly
        if (currentUser != null) {

            String userId = currentUser.getUid();

            DatabaseReference userFavoritesRef = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(userId)
                    .child("favorites")
                    .child(itemid);

            userFavoritesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Item is in favorites, set the button image to favorites_on
                        btnfavorit.setImageResource(R.drawable.baseline_favorite_24);
                    } else {
                        // Item is not in favorites, set the button image to favorites_off
                        btnfavorit.setImageResource(R.drawable.baseline_favorite_off_24);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle any errors here
                }
            });
        }


        // Save the item as a recently played item in Firebase
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference recentlyPlayedRef = databaseReference.child("Users").child(userId).child("recently_played");

            // Create a new ItemModel object to store the item's data
            ItemModel itemModel = new ItemModel();
            itemModel.setItemId(itemid);
            itemModel.setTitle(itemtitle);
            itemModel.setAudio_url(item_audio_url);
            itemModel.setCover_image_url(item_cover_image_url);
            itemModel.setSpeakers(item_speakers);
            itemModel.setSelectedValue(item_selectedValue);
            itemModel.setDuration(item_duration);

            // Save the current timestamp
            // Get the current date and time as a formatted string
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String timestamp = dateFormat.format(new Date());
            itemModel.setTimestamp(timestamp);

            // Set the value of the "itemid" inside the "recently_played" node
            recentlyPlayedRef.child(itemid).setValue(itemModel);

            // Optionally, you can use a specific value instead of `itemModel` if needed
        } else {
            // User is not authenticated, show a toast message
            Toast.makeText(PlayActivity.this, "Please log in to save as recently played.", Toast.LENGTH_SHORT).show();
        }

        Intent serviceintent = new Intent(PlayActivity.this, MediaPlayerService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceintent);
        }

        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying) {
                    // Start playing the audio
                    mediaPlayer.start();
                    isPlaying = true;
                    btnplay.setImageResource(R.drawable.round_pause_24); // Change the button image to pause
                    updateSeekBar.run(); // Start updating the SeekBar and playing time


                } else {
                    // Pause the audio
                    mediaPlayer.pause();
                    isPlaying = false;
                    btnplay.setImageResource(R.drawable.round_play_arrow_24); // Change the button image to play
                    updateHandler.removeCallbacks(updateSeekBar); // Stop updating SeekBar and playing time
                }
            }
        });

        btnforward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Play the next item
                playNextItem();
            }
        });

        btnbackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Play the previous item
                playPreviousItem();
            }
        });


        timelineSeekBar.setMax(mediaPlayer.getDuration());
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    timelineSeekBar.setProgress(currentPosition);
                    playingtime.setText(formatTime(currentPosition));
                    updateHandler.postDelayed(this, 1000); // Update every second
                }
            }
        };

        // Add this code after initializing your SeekBar timelineSeekBar
        timelineSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress); // Seek to the selected position when the user drags the SeekBar
                    playingtime.setText(formatTime(progress)); // Update the playing time TextView
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not needed for this implementation, but you can add actions here if desired.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not needed for this implementation, but you can add actions here if desired.
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Handle audio completion, e.g., show a message or perform an action
            }
        });

        btnfavorit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null) {
                    // Get the current user's ID
                    String userId = currentUser.getUid();

                    // Create a reference to the user's favorite items
                    DatabaseReference userFavoritesRef = FirebaseDatabase.getInstance().getReference()
                            .child("Users")
                            .child(userId)
                            .child("favorites")
                            .child(itemid);

                    userFavoritesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Item is in favorites, remove it
                                userFavoritesRef.removeValue();
                                decrementFavoriteCount();
                                // You can also update the UI here to show it's not a favorite anymore
                                btnfavorit.setImageResource(R.drawable.baseline_favorite_off_24); // Assuming you have icons for favorite and unfavorite states

                                Toast.makeText(PlayActivity.this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                            } else {
                                // Item is not in favorites, add it
                                // Increment the favorite count
                                incrementFavoriteCount();

                                // You can also update the UI here to show it's a favorite
                                btnfavorit.setImageResource(R.drawable.baseline_favorite_24); // Assuming you have icons for favorite and unfavorite states

                                // Create a new ItemModel object to store the item's data
                                ItemModel itemModel = new ItemModel();
                                itemModel.setItemId(itemid);
                                itemModel.setTitle(itemtitle);
                                itemModel.setAudio_url(item_audio_url);
                                itemModel.setCover_image_url(item_cover_image_url);
                                itemModel.setSpeakers(item_speakers);
                                itemModel.setSelectedValue(item_selectedValue);
                                itemModel.setDuration(item_duration);

                                // Save the current timestamp
                                // Get the current date and time as a formatted string
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                String timestamp = dateFormat.format(new Date());
                                itemModel.setTimestamp(timestamp);

                                // Set the value of the "itemid" inside the "favorites" node
                                userFavoritesRef.setValue(itemModel);

                                Toast.makeText(PlayActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle any errors here
                        }
                    });
                } else {
                    // User is not authenticated, handle this case accordingly (e.g., show a login screen)
                    Toast.makeText(PlayActivity.this, "Please log in to add to favorites", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btndownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // Request permissions if not granted
                        ActivityCompat.requestPermissions(PlayActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                        return; // Exit the method until the user grants permission
                    }
                }

                downloadfeature();

                if (rewardedAd != null) {
                    Activity activityContext = PlayActivity.this;
                    rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

                        }
                    });
                    rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdClicked() {
                            // Called when a click is recorded for an ad.

                        }

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when ad is dismissed.
                            // Set the ad reference to null so you don't show the ad a second time.
                            //downloadfeature();
                            loadred();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            // Called when ad fails to show.
                            downloadfeature();
                        }

                        @Override
                        public void onAdImpression() {
                            // Called when an impression is recorded for an ad.

                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            // Called when ad is shown.

                        }
                    });
                } else {

                }


            }
        });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        TopAdView = findViewById(R.id.topadView);
        AdRequest topadRequest = new AdRequest.Builder().build();
        TopAdView.loadAd(topadRequest);

        loadred();


    }

    public void downloadfeature() {
        // Define a directory where you want to save the audio and cover image
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        // Create a directory if it doesn't exist
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Create a file for the audio
        File audioFile = new File(directory, itemtitle + ".mp3");

        // Create a file for the cover image
        File imageFile = new File(directory, itemtitle + "_cover.jpg");

        // Download and save the audio file
        DownloadManager.Request audioRequest = new DownloadManager.Request(Uri.parse(item_audio_url))
                .setDestinationUri(Uri.fromFile(audioFile))
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setTitle("Downloading Audio");

        DownloadManager audioManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        audioManager.enqueue(audioRequest);

        // Download and save the cover image
        DownloadManager.Request imageRequest = new DownloadManager.Request(Uri.parse(item_cover_image_url))
                .setDestinationUri(Uri.fromFile(imageFile))
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setTitle("Downloading Cover Image");

        DownloadManager imageManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        imageManager.enqueue(imageRequest);

        // Store the downloaded data in your local SQLite database using Databasehelper
        Databasehelper mydb = new Databasehelper(PlayActivity.this);
        mydb.addSpeache(itemtitle, item_speakers, item_selectedValue, item_duration, imageFile.getAbsolutePath(), audioFile.getAbsolutePath());
    }

    int itemPosition;

    private void initializeItemList() {
        String userId = currentUser.getUid();
        DatabaseReference speechesRef = FirebaseDatabase.getInstance().getReference().child("speeches");
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        if ("new".equals(collection_type)) { // Corrected the string comparison
            Query query = speechesRef.orderByChild("timestamp").limitToLast(100); // Adjust the limit as needed
            DatabaseReference newref = query.getRef();
            newref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && itemid != null) {
                        itemPosition = -1; // Initialize to -1 to indicate that the item is not found

                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                            // Convert the DataSnapshot to an ItemModel or whatever class you are using
                            ItemModel item = itemSnapshot.getValue(ItemModel.class);

                            // Check if the current item and itemid are not null and if they match
                            if (item != null && item.getItemId() != null && item.getItemId().equals(itemid)) {
                                itemPosition = itemList.size(); // Set the position when the item is found
                                itemList.add(item); // Add the items to the list after finding the selected item
                                // Exit the loop once the item is found
                            } else {
                                itemList.add(item); // Add the items to the list
                            }
                        }

                        // Now that itemList is populated, you can play the first item
                        playItemAtIndex(itemPosition); // Play the item at the found position
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle errors here
                }
            });
        }
        if ("popular".equals(collection_type)) {
            Query popularquery = speechesRef.orderByChild("favoriteCount").limitToLast(100);
            DatabaseReference popularref = popularquery.getRef();
            popularref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && itemid != null) {
                        itemPosition = -1; // Initialize to -1 to indicate that the item is not found

                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                            // Convert the DataSnapshot to an ItemModel or whatever class you are using
                            ItemModel item = itemSnapshot.getValue(ItemModel.class);

                            // Check if the current item and itemid are not null and if they match
                            if (item != null && item.getItemId() != null && item.getItemId().equals(itemid)) {
                                itemPosition = itemList.size(); // Set the position when the item is found
                                itemList.add(item); // Add the items to the list after finding the selected item
                                // Exit the loop once the item is found
                            } else {
                                itemList.add(item); // Add the items to the list
                            }
                        }

                        // Now that itemList is populated, you can play the first item
                        playItemAtIndex(itemPosition); // Play the item at the found position
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle errors here
                }
            });
        }
        if ("favorite".equals(collection_type)) {
            DatabaseReference favoriteref = usersRef.child("favorites");
            favoriteref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && itemid != null) {
                        itemPosition = -1; // Initialize to -1 to indicate that the item is not found

                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                            // Convert the DataSnapshot to an ItemModel or whatever class you are using
                            ItemModel item = itemSnapshot.getValue(ItemModel.class);

                            // Check if the current item and itemid are not null and if they match
                            if (item != null && item.getItemId() != null && item.getItemId().equals(itemid)) {
                                itemPosition = itemList.size(); // Set the position when the item is found
                                itemList.add(item); // Add the items to the list after finding the selected item
                                // Exit the loop once the item is found
                            } else {
                                itemList.add(item); // Add the items to the list
                            }
                        }

                        // Now that itemList is populated, you can play the first item
                        playItemAtIndex(itemPosition); // Play the item at the found position

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle errors here
                }
            });
        }
        if ("recent".equals(collection_type)) {
            DatabaseReference recentref = usersRef.child("recently_played");
            recentref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && itemid != null) {
                        itemPosition = -1; // Initialize to -1 to indicate that the item is not found

                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                            // Convert the DataSnapshot to an ItemModel or whatever class you are using
                            ItemModel item = itemSnapshot.getValue(ItemModel.class);

                            // Check if the current item and itemid are not null and if they match
                            if (item != null && item.getItemId() != null && item.getItemId().equals(itemid)) {
                                itemPosition = itemList.size(); // Set the position when the item is found
                                itemList.add(item); // Add the items to the list after finding the selected item
                                // Exit the loop once the item is found
                            } else {
                                itemList.add(item); // Add the items to the list
                            }
                        }

                        // Now that itemList is populated, you can play the first item
                        playItemAtIndex(itemPosition); // Play the item at the found position
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle errors here
                }
            });
        }
        if ("All".equals(collection_type)) {
            Toast.makeText(this, "ALL", Toast.LENGTH_SHORT).show();
            speechesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && itemid != null) {
                        itemPosition = -1; // Initialize to -1 to indicate that the item is not found

                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                            // Convert the DataSnapshot to an ItemModel or whatever class you are using
                            ItemModel item = itemSnapshot.getValue(ItemModel.class);

                            // Check if the current item and itemid are not null and if they match
                            if (item != null && item.getItemId() != null && item.getItemId().equals(itemid)) {
                                itemPosition = itemList.size(); // Set the position when the item is found
                                itemList.add(item); // Add the items to the list after finding the selected item
                                // Exit the loop once the item is found
                            } else {
                                itemList.add(item); // Add the items to the list
                            }
                        }

                        // Now that itemList is populated, you can play the first item
                        playItemAtIndex(itemPosition); // Play the item at the found position
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle errors here
                }
            });
        }
        if (item_selectedValue.equals(collection_type)) {
            Toast.makeText(this, "TRUE", Toast.LENGTH_SHORT).show();
            Query categoryquery = speechesRef.orderByChild("selectedValue")
                    .equalTo(collection_type);
            DatabaseReference categoryref = categoryquery.getRef();
            categoryref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && itemid != null) {
                        itemPosition = -1; // Initialize to -1 to indicate that the item is not found

                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                            // Convert the DataSnapshot to an ItemModel or whatever class you are using
                            ItemModel item = itemSnapshot.getValue(ItemModel.class);

                            // Check if the current item and itemid are not null and if they match
                            if (item != null && item.getItemId() != null && item.getItemId().equals(itemid)) {
                                itemPosition = itemList.size(); // Set the position when the item is found
                                itemList.add(item); // Add the items to the list after finding the selected item
                                // Exit the loop once the item is found
                            } else {
                                itemList.add(item); // Add the items to the list
                            }
                        }

                        // Now that itemList is populated, you can play the first item
                        playItemAtIndex(itemPosition); // Play the item at the found position
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle errors here
                }
            });
        }
    }


    private void playItemAtIndex(int index) {
        if (index >= 0 && index <= itemList.size()) {
            ItemModel item = itemList.get(index);

            // Release the current MediaPlayer to avoid conflicts
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }

            // Create a new MediaPlayer instance
            mediaPlayer = new MediaPlayer();

            try {
                // Set the data source to the next item's audio URL
                mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(item.getAudio_url()));

                // Prepare the MediaPlayer asynchronously
                mediaPlayer.prepareAsync();

                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        // Start playback when prepared
                        mp.start();
                    }
                });

                // Update UI with the new item's information
                updateUIWithItem(item);

                // Update the button image to show that it's playing
                btnplay.setImageResource(R.drawable.round_pause_24);
                isPlaying = true;

                // Start updating the SeekBar and playing time
                updateSeekBar.run();

                // Save the next item as a recently played item in Firebase
                // (you can add this logic here as you did before)

            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception if the audio file cannot be prepared
            }
        }
    }


    private void playNextItem() {
        // Check if there is a next item to play
        if (itemPosition < itemList.size() - 1) {
            // Increment the current item position
            itemPosition++;



            // Get the next item to play
            ItemModel nextItem = itemList.get(itemPosition);

            // Stop the current playback if it's playing
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset();
            }

            try {
                // Set the data source to the next item's audio URL
                mediaPlayer.setDataSource(nextItem.getAudio_url());
                mediaPlayer.prepare();
                mediaPlayer.start();

                // Update UI with the new item's information
                updateUIWithItem(nextItem);

                // Update the button image to show that it's playing
                btnplay.setImageResource(R.drawable.round_pause_24);
                isPlaying = true;

                // Start updating the SeekBar and playing time
                updateSeekBar.run();

                // Save the next item as a recently played item in Firebase
                // (you can add this logic here as you did before)

            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception if the audio file cannot be prepared
            }
        } else {
            // No more items to play, show a toast message
            Toast.makeText(PlayActivity.this, "No more items to play", Toast.LENGTH_SHORT).show();
        }
    }

    private void playPreviousItem() {
        // Check if there is a previous item to play
        if (itemPosition > 0) { // Change this condition
            // Decrement the current item position
            itemPosition--;



            // Get the previous item to play
            ItemModel previousItem = itemList.get(itemPosition);

            // Stop the current playback if it's playing
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset();
            }

            try {
                // Set the data source to the previous item's audio URL
                mediaPlayer.setDataSource(previousItem.getAudio_url());
                mediaPlayer.prepare();
                mediaPlayer.start();

                // Update UI with the new item's information
                updateUIWithItem(previousItem);

                // Update the button image to show that it's playing
                btnplay.setImageResource(R.drawable.round_pause_24);
                isPlaying = true;

                // Start updating the SeekBar and playing time
                updateSeekBar.run();

                // Save the previous item as a recently played item in Firebase
                // (you can add this logic here as you did before)

            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception if the audio file cannot be prepared
            }
        } else {
            // No more items to play, show a toast message
            Toast.makeText(PlayActivity.this, "No more items to play", Toast.LENGTH_SHORT).show();
        }
    }



    private void updateUIWithItem(ItemModel itemModel) {
        // Update your UI elements with the new item's information
        title.setText(itemModel.getTitle());
        speakers.setText(itemModel.getSpeakers());
        category.setText(itemModel.getSelectedValue());
        Picasso.get().load(itemModel.getCover_image_url()).into(smallCoverImage);
    }

    private void decrementFavoriteCount() {
        DatabaseReference favoriteCountRef = FirebaseDatabase.getInstance().getReference()
                .child("speeches")
                .child(itemid)
                .child("favoriteCount");

        favoriteCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve the current favoriteCount value
                    int currentFavoriteCount = dataSnapshot.getValue(Integer.class);

                    // Decrement the favoriteCount
                    currentFavoriteCount--;

                    // Update the favoriteCount in the Firebase Realtime Database
                    favoriteCountRef.setValue(currentFavoriteCount);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors here
            }
        });
    }

    private void incrementFavoriteCount() {
        DatabaseReference favoriteCountRef = FirebaseDatabase.getInstance().getReference()
                .child("speeches")
                .child(itemid)
                .child("favoriteCount");

        favoriteCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve the current favoriteCount value
                    int currentFavoriteCount = dataSnapshot.getValue(Integer.class);

                    // Increment the favoriteCount
                    currentFavoriteCount++;

                    // Update the favoriteCount in the Firebase Realtime Database
                    favoriteCountRef.setValue(currentFavoriteCount);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors here
            }
        });
    }



    // Helper method to format time in minutes:seconds
    private String formatTime(int milliseconds) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                TimeUnit.MINUTES.toSeconds(minutes);
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();// Release the MediaPlayer when the activity is destroyed
            mediaPlayer = null;
        }
        updateHandler.removeCallbacks(updateSeekBar); // Remove callbacks to avoid memory leaks
    }

    public void loadred(){
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, getResources().getString(R.string.download_attempt_rewarded_ad_unit_id),
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        rewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                    }
                });
    }


}
