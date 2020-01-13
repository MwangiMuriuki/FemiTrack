package com.example.a001759.pregtrack.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.example.a001759.pregtrack.R;
import com.example.a001759.pregtrack.activities.MainActivity;
import com.example.a001759.pregtrack.databinding.ActivityLoginBinding;


public class Login extends Activity {
    Button login;
    ImageButton buttonFacebook, buttonTwitter,buttonGoogle;

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);


        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ActivityRegister.class);
                startActivity(intent);

            }
        });

        binding.forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ActivityResetPassword.class);
                startActivity(intent);
            }
        });

    }
}
