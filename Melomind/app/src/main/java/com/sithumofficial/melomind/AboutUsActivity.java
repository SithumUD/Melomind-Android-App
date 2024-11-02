package com.sithumofficial.melomind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.firebase.auth.FirebaseAuth;

public class AboutUsActivity extends AppCompatActivity {

    ImageButton back;

    LinearLayout btnlogout,btnterms,btnprivacypolicy,btnversion, btnrating;

    FirebaseAuth auth;

    Dialog mydialog;
    private InterstitialAd mInterstitialAd;
    private ReviewManager reviewManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        back = findViewById(R.id.btnback);
        btnlogout = findViewById(R.id.btnlogout);
        btnversion = findViewById(R.id.btnversion);
        btnterms = findViewById(R.id.btnterms);
        btnprivacypolicy = findViewById(R.id.btnprivacypolicy);
        btnrating = findViewById(R.id.btnrating);

        auth = FirebaseAuth.getInstance();

        mydialog = new Dialog(this);

        reviewManager = ReviewManagerFactory.create(this);





        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create a confirmation dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(AboutUsActivity.this);
                builder.setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        generateAds();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User canceled logout, do nothing
                    }
                });

                // Show the confirmation dialog
                builder.show();
            }
        });



        btnterms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the URL for your Terms & Conditions page
                String url = "https://sithumudayanga.blogspot.com/2023/11/terms-conditions-for-melomind.html"; // Replace with your actual URL

                // Create an Intent to open the web browser
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        btnprivacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the URL for your Terms & Conditions page
                String url = "https://sithumudayanga.blogspot.com/2023/11/privacy-policy-for-melomind.html"; // Replace with your actual URL

                // Create an Intent to open the web browser
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
        btnversion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout bt_close;
                mydialog.setContentView(R.layout.about_version_card);
                bt_close =mydialog.findViewById(R.id.bt_close);
                bt_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mydialog.dismiss();
                    }
                });
                mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mydialog.show();
            }
        });

        btnrating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestAppReview();
            }
        });


    }

    private void requestAppReview() {
        Task<ReviewInfo> reviewInfoTask = reviewManager.requestReviewFlow();
        reviewInfoTask.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
            @Override
            public void onComplete(Task<ReviewInfo> task) {
                if (task.isSuccessful()) {
                    // We got the ReviewInfo, launch the review flow
                    ReviewInfo reviewInfo = task.getResult();
                    Task<Void> flow = reviewManager.launchReviewFlow(AboutUsActivity.this, reviewInfo);
                    flow.addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            // Review flow completed, you can log or handle this as needed
                        }
                    });
                } else {
                    // There was an error getting the ReviewInfo.
                    // You can log or handle this as needed.
                    Toast.makeText(AboutUsActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    public void generateAds(){

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, getResources().getString(R.string.logout_interstitial_ad_unit_id), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        interstitialAd.show(AboutUsActivity.this);
                        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                super.onAdFailedToShowFullScreenContent(adError);
                                auth.signOut();
                                finishAffinity(); // Finish the AboutUsActivity to prevent it from being on the back stack
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();
                                auth.signOut();
                                finishAffinity(); // Finish the AboutUsActivity to prevent it from being on the back stack
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                                auth.signOut();
                                finishAffinity(); // Finish the AboutUsActivity to prevent it from being on the back stack
                            }

                            @Override
                            public void onAdImpression() {
                                super.onAdImpression();

                            }

                            @Override
                            public void onAdClicked() {
                                super.onAdClicked();

                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        auth.signOut();
                        finishAffinity(); // Finish the AboutUsActivity to prevent it from being on the back stack
                    }
                });

    }


}