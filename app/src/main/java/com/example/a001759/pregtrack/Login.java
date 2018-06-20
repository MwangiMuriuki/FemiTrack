package com.example.a001759.pregtrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


public class Login extends Activity {
    Button login;
    ImageButton buttonFacebook, buttonTwitter,buttonGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login);
        buttonFacebook = findViewById(R.id.facebook_sign_in);
        buttonTwitter = findViewById(R.id.twitter_sign_in);
        buttonGoogle = findViewById(R.id.google_sign_in);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
            }
        });

        buttonFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Sign In with Facebook", Toast.LENGTH_LONG).show();
            }
        });

       buttonTwitter.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(getApplicationContext(), "Sign in with Twitter", Toast.LENGTH_LONG).show();
           }
       });

       buttonGoogle.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(getApplicationContext(), "Sign in with Google", Toast.LENGTH_LONG).show();
           }
       });
    }
}
