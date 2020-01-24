package com.example.a001759.pregtrack.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.a001759.pregtrack.R;
import com.example.a001759.pregtrack.databinding.ActivityResetPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class ActivityResetPassword extends AppCompatActivity {

    ActivityResetPasswordBinding binding;
    String emailAddress;
    boolean isConnected;
    AlertDialog resetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password);

        //CHECK IF DEVICE IS CONNECTED TO THE INTERNET
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = Objects.requireNonNull(cm).getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        resetDialog = new SpotsDialog(this, R.style.resetPasswordAlert);

        binding.cancelReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityResetPassword.this, Login.class);
                startActivity(intent);
                finish();
            }
        });


        binding.resetPwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailAddress = Objects.requireNonNull(binding.emailLayout.getEditText()).getText().toString();

                boolean cancel = false;
                View focusView = null;

                binding.emailField.setError(null);

                if(TextUtils.isEmpty(emailAddress)){

                    binding.emailField.setError("Please enter your Email Address");
                    focusView = binding.emailField;
                    cancel = true;
                }
                if (cancel){

                    focusView.requestFocus();
                }

                else {
                    if (!isConnected){

                        Toast.makeText(getApplicationContext(), "Please connect to the internet and try again.", Toast.LENGTH_LONG).show();

                    }else {
                        resetDialog.setCancelable(false);
                        resetDialog.show();
                        mthdResetPswd(emailAddress);
                    }
                }
            }
        });
    }

    private void mthdResetPswd(String emailAddress) {

            FirebaseAuth auth = FirebaseAuth.getInstance();

            auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {

                        resetDialog.dismiss();

                        Toast.makeText(ActivityResetPassword.this, "Please check your email. We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();

                        binding.emailField.setText("");

                    } else {

                        Toast.makeText(ActivityResetPassword.this, "That email address does not exist. Please try again", Toast.LENGTH_SHORT).show();

                    }
                }
            });
    }
}
