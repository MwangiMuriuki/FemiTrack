package com.example.a001759.pregtrack.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ActivityCheckLoginSession extends Activity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser userLoggedIn = mAuth.getCurrentUser();

        if (userLoggedIn == null) {

            Intent Login = new Intent(getApplicationContext(), Login.class);
            startActivity(Login);

        } else {

            Intent MainPage = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(MainPage);
        }

        finish();
    }
}
