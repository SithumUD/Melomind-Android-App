package com.sithumofficial.melomind.ExtraClasses;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.sithumofficial.melomind.R;

public class MediaPlayerService extends Service {
    private MediaPlayer mediaPlayer;
    private static final int NOTIFICATION_ID = 1;
    private Notification notification;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize MediaPlayer and set up media playback
        mediaPlayer = new MediaPlayer();
        // Set up media player listeners and other configurations
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Handle commands from the notification (e.g., play, pause, skip)
        String action = intent.getAction();
        createNotification();
        if (notification != null) {
            startForeground(NOTIFICATION_ID, notification);
        }
        if (action != null) {
            switch (action) {
                case "ACTION_PLAY":
                    // Play or resume playback
                    mediaPlayer.start();
                    createNotification(); // Create or update the notification when playback starts
                    break;
                case "ACTION_PAUSE":
                    // Pause playback
                    mediaPlayer.pause();
                    break;
                // Handle other actions like skip forward/backward, stop, etc.
            }
        }

        // Return START_STICKY to ensure the service keeps running
        return START_STICKY;
    }


    public void createNotification() {
        // Create a notification channel for Android Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("media_player", "Media Player", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        // Create a notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "media_player")
                .setContentTitle("Now Playing")
                .setContentText("Song Title")
                .setSmallIcon(R.drawable.app_logo)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Add actions to the notification (e.g., play/pause, skip forward/backward)
        builder.addAction(R.drawable.round_pause_24, "Pause", pendingIntentForAction("ACTION_PAUSE"));
        builder.addAction(R.drawable.outline_skip_next_24, "Forward", pendingIntentForAction("ACTION_SKIP_FORWARD"));
        builder.addAction(R.drawable.outline_skip_previous_24, "Backward", pendingIntentForAction("ACTION_SKIP_BACKWARD"));

        // Set the notification as ongoing (persistent)
        builder.setOngoing(true);

        // Display the notification
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        NotificationManagerCompat.from(this).notify(1, builder.build());
    }

    private PendingIntent pendingIntentForAction(String action) {
        Intent intent = new Intent(this, MediaPlayerService.class);
        intent.setAction(action);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Release resources like MediaPlayer when the service is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

