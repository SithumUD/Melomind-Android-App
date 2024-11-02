package com.sithumofficial.melomind;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

public class ExtraDocActivity extends AppCompatActivity {

    ImageButton back;

    TextView lbltitle;

    WebView docview;

    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_doc);

        back = findViewById(R.id.btnback);
        lbltitle = findViewById(R.id.lbltitle);
        docview = findViewById(R.id.docview);

        docview.setWebViewClient(new WebViewClient());
        docview.loadUrl("https://sites.google.com/view/melomind-termsandcondition/home");

        title = getIntent().getStringExtra("title");
        lbltitle.setText(title);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}