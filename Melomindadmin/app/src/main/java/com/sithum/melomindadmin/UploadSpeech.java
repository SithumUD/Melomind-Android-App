package com.sithum.melomindadmin;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class UploadSpeech extends AppCompatActivity {

    Button btnupload, btnchoosefile;
    CircleImageView coverimage;
    ImageButton btnback;
    ProgressBar progressBar;
    PowerSpinnerView powerSpinnerView;
    EditText txttitle, txtspeakers, txtduration, txtdesc;
    String strtitle, strspeakers, strduration, strdesc, selectedValue;

    TextView filename;
    Uri resulturi;
    Uri audioUri;

    int favoriteCount = 0;
    private static final int FILE_PICKER_REQUEST_CODE = 1;
    private static final int AUDIO_PICKER_REQUEST_CODE = 2;
    private DatabaseReference speechesRef;
    private StorageReference storageRef;

    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "MyNotificationChannel";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_speech);

        btnupload = findViewById(R.id.btnupload);
        btnchoosefile = findViewById(R.id.btnchoose);
        txttitle = findViewById(R.id.txttitle);
        txtspeakers = findViewById(R.id.txtspeakers);
        txtduration = findViewById(R.id.txtduration);
        txtdesc = findViewById(R.id.txtdescription);
        btnback = findViewById(R.id.btnback);
        coverimage = findViewById(R.id.coverimage);
        powerSpinnerView = findViewById(R.id.powerSpinnerView);
        progressBar = findViewById(R.id.progressBar);
        filename = findViewById(R.id.filename);

        // Initialize Firebase components
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        speechesRef = firebaseDatabase.getReference("speeches");
        storageRef = firebaseStorage.getReference("audio_files");

        // ...

        // Handle cover image selection
        coverimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        // Create the notification channel (for Android 8.0 and later)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Notification Channel";
            String description = "Channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        // Handle spinner selection
        powerSpinnerView.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
            @Override
            public void onItemSelected(int oldIndex, @Nullable String oldItem, int newIndex, String newItem) {
                selectedValue = newItem;
            }
        });

        // Handle upload button click
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strtitle = txttitle.getText().toString();
                strspeakers = txtspeakers.getText().toString();
                strduration = txtduration.getText().toString();
                strdesc = txtdesc.getText().toString();

                if (isValidate()) {
                    uploadData();
                }
            }
        });

        // Handle audio file selection
        btnchoosefile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/mpeg");
                startActivityForResult(intent, AUDIO_PICKER_REQUEST_CODE);
            }
        });

        // Handle back button click
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            final Uri imageuri = data.getData();
            resulturi = imageuri;
            coverimage.setImageURI(resulturi);
        }
        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                String selectedFilePath = getFileNameFromUri(uri);
            }
        }
        if (requestCode == AUDIO_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                audioUri = data.getData();
                String selectedFilePath = getFileNameFromUri(audioUri); // Get the audio file name
                filename.setText(selectedFilePath); // Set the filename TextView to the audio file name
                getAudioDuration(audioUri);
            }
        }
    }

    private String getFileNameFromUri(Uri uri) {
        String fileName = null;
        String scheme = uri.getScheme();

        if (scheme != null && scheme.equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        if (displayNameIndex != -1) {
                            fileName = cursor.getString(displayNameIndex);
                        }
                    }
                } finally {
                    cursor.close();
                }
            }
        }

        if (fileName == null) {
            fileName = uri.getLastPathSegment();
        }

        return fileName;
    }

    private void uploadData() {
        // Create a unique key for the new speech entry
        String speechId = speechesRef.push().getKey();

        // Create a map to store the speech data

        Map<String, Object> speechData = new HashMap<>();
        speechData.put("itemId", speechId);
        speechData.put("title", strtitle);
        speechData.put("favoriteCount" , favoriteCount);
        speechData.put("speakers", strspeakers);
        speechData.put("duration", strduration);
        speechData.put("description", strdesc);
        speechData.put("selectedValue", selectedValue); // Add the selected spinner value
        // Get the current date and time as a formatted string
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String timestamp = dateFormat.format(new Date());

        speechData.put("timestamp",timestamp);

        // Upload the cover image if available
        if (resulturi != null) {
            uploadCoverImage(speechId, speechData);
        } else {
            // If no cover image is selected, proceed to upload audio
            uploadAudioFile(speechId, speechData);
        }
    }

    private void uploadCoverImage(final String speechId, final Map<String, Object> speechData) {
        // Create a reference to store the cover image in Firebase Storage
        final StorageReference coverImageRef = storageRef.child("cover_images/" + speechId);

        // Upload the cover image file
        coverImageRef.putFile(resulturi)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get the download URL of the uploaded cover image
                        coverImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                // Save the cover image URL in the speech data
                                speechData.put("cover_image_url", downloadUri.toString());

                                // Proceed to upload the audio file
                                uploadAudioFile(speechId, speechData);
                            }
                        });
                    }
                });
    }

    private void uploadAudioFile(final String speechId, final Map<String, Object> speechData) {
        if (audioUri != null) {
            // Create a reference to store the audio file in Firebase Storage
            final StorageReference audioFileRef = storageRef.child("audio_files/" + speechId);

            // Upload the audio file
            UploadTask uploadTask = audioFileRef.putFile(audioUri);

            // Set up the progress listener
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    // Calculate the progress percentage and update the progressBar
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressBar.setProgress((int) progress);
                    showNotificationWithProgressBar((int) progress);
                }
            });

            // Handle the completion of the upload task
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get the download URL of the uploaded audio file
                    audioFileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUri) {
                            // Save the audio file URL in the speech data
                            speechData.put("audio_url", downloadUri.toString());

                            // Save the speech data to the Realtime Database
                            speechesRef.child(speechId).setValue(speechData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(UploadSpeech.this, "Speech uploaded successfully!", Toast.LENGTH_SHORT).show();
                                                txttitle.setText("");
                                                txtspeakers.setText("");
                                                txtduration.setText("");
                                                txtdesc.setText("");
                                                powerSpinnerView.clearSelectedItem(); // Clear the selected spinner item
                                                coverimage.setImageResource(R.drawable.user_default_pic); // Set a default image
                                                progressBar.setProgress(0); // Reset progress bar
                                                filename.setText(""); // Clear audio file name
                                                resulturi = null; // Reset image URI
                                                audioUri = null;
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(UploadSpeech.this, "Error uploading speech", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                    });
                    showNotification();
                }
            });
        } else {
            // If no audio file is selected, save the speech data without audio
            speechesRef.child(speechId).setValue(speechData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(UploadSpeech.this, "Speech uploaded successfully!", Toast.LENGTH_SHORT).show();
                            // Clear form fields or navigate to another screen as needed
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadSpeech.this, "Error uploading speech", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

// ...

    private void getAudioDuration(Uri audioUri) {
        SimpleExoPlayer player = new SimpleExoPlayer.Builder(this).build();
        MediaItem mediaItem = MediaItem.fromUri(audioUri);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.addListener(new Player.Listener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_READY) {
                    long durationMs = player.getDuration();
                    long durationInSeconds = durationMs / 1000;
                    double durationInMinutes = durationInSeconds / 60.0;
                    String formattedDuration = String.format("%.2f", durationInMinutes);
                    txtduration.setText(formattedDuration);
                    player.release(); // Release the player
                }
            }
        });


        player.setPlayWhenReady(false); // Do not auto-play the audio
        player.prepare();
    }


    private void showNotification() {
        // Create an intent that opens your app when the notification is clicked
        Intent intent = new Intent(this, UploadSpeech.class); // Replace YourMainActivity with your main activity class
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.melomindadmin_logo) // Replace with your notification icon
                .setContentTitle("Upload Success")
                .setContentText("Speech uploaded successfully!")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        // Show the notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void showNotificationWithProgressBar(int progress) {
        // Create an intent that opens your app when the notification is clicked
        Intent intent = new Intent(this, UploadSpeech.class); // Replace with your main activity class
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Build the custom notification layout
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);
        remoteViews.setProgressBar(R.id.notification_progress, 100, progress, false);

        // Create the notification with the custom layout
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.melomindadmin_logo)
                .setContentTitle("Upload Progress")
                .setContentText("Uploading...")
                .setAutoCancel(false)
                .setContentIntent(pendingIntent)
                .setCustomContentView(remoteViews)
                .setCustomBigContentView(remoteViews);

        // Show the notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }



    private boolean isValidate() {
        if (TextUtils.isEmpty(strtitle)) {
            txttitle.setError("Enter a title");
            return false;
        }
        if (TextUtils.isEmpty(strspeakers)) {
            txtspeakers.setError("Enter speakers");
            return false;
        }
        if (TextUtils.isEmpty(strduration)) {
            txtduration.setError("Enter duration");
            return false;
        }
        if (TextUtils.isEmpty(strdesc)) {
            txtdesc.setError("Enter description");
            return false;
        }
        return true;
    }
}
