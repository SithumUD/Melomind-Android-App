package com.sithumofficial.melomind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sithumofficial.melomind.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forget_Password extends AppCompatActivity {

    AppCompatButton btnlogin;
    EditText txtemail;
    AppCompatButton btnsend;
    ImageButton btnback;

    String stremail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        btnback = findViewById(R.id.btnback);
        btnlogin = findViewById(R.id.btnlogin);
        btnsend = findViewById(R.id.btnreset);
        txtemail = findViewById(R.id.txtemail);
        stremail = txtemail.getText().toString();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Forget_Password.this, Login.class);
                startActivity(intent);
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stremail = txtemail.getText().toString();

                if (TextUtils.isEmpty(stremail)) {
                    Toast.makeText(Forget_Password.this, "Enter your email", Toast.LENGTH_SHORT).show();
                } else {
                    // Send a password reset email
                    auth.sendPasswordResetEmail(stremail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Forget_Password.this, "Password reset email sent to your email address", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Forget_Password.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });


    }
}
