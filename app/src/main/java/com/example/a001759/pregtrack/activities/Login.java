package com.example.a001759.pregtrack.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.a001759.pregtrack.R;
import com.example.a001759.pregtrack.activities.MainActivity;
import com.example.a001759.pregtrack.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dmax.dialog.SpotsDialog;

import static android.content.ContentValues.TAG;


public class Login extends Activity {
    Button login;
    ImageButton buttonFacebook, buttonTwitter,buttonGoogle;

    String login_email;
    String login_password;

    AlertDialog alertDialog;
    private FirebaseAuth mAuth;

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        //CHECK IF DEVICE IS CONNECTED TO THE INTERNET
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        alertDialog = new SpotsDialog(this, R.style.loginAlert);

        mAuth = FirebaseAuth.getInstance();

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                login_email = binding.emailLayout.getEditText().getText().toString();
                login_password = binding.passwordLayout.getEditText().getText().toString();

                boolean cancel = false;
                View focusView = null;

                binding.loginEmail.setError(null);
                binding.loginPassword.setError(null);

                if (TextUtils.isEmpty(login_email)){

                    binding.loginEmail.setError("Please enter your Email");
                    focusView = binding.loginEmail;
                    cancel = true;
                }
                else if (TextUtils.isEmpty(login_password)){

                    binding.loginPassword.setError("Please enter your Password");
                    focusView = binding.loginPassword;
                    cancel = true;


                }
                else if (!TextUtils.isEmpty(login_email) && !isEmailValid(login_email)){

                    binding.loginEmail.setError("Please enter a valid Email");
                    focusView = binding.loginEmail;
                    cancel = true;

                }else if(!TextUtils.isEmpty(login_password) && !isPasswordValid(login_password)){

                    binding.loginPassword.setError("Please enter a password with 4 or more characters");
                    focusView = binding.loginPassword;
                    cancel = true;
                }
                if (cancel) {

                    focusView.requestFocus();
                }
                if (!isConnected){

                    Toast.makeText(getApplicationContext(), "Please connect to the internet and try again.", Toast.LENGTH_LONG).show();

                }
                else {

                    alertDialog.setCancelable(false);
                    alertDialog.show();
                    signInUser(login_email, login_password);

                }
            }
        });

        binding.skip.setOnClickListener(new View.OnClickListener() {
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

    private void signInUser(String login_email, String login_password) {

        if (login_email.isEmpty()|| login_password.isEmpty()){

            alertDialog.cancel();

        }else {

            mAuth.signInWithEmailAndPassword(login_email, login_password)
                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                toMain(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                alertDialog.cancel();
                                Toast.makeText(Login.this, "Login failed. A user with that Email and Password does not exist. Please try again.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }

    }

    private void toMain(FirebaseUser user) {

        Intent intent = new Intent(getApplication(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isPasswordValid(String login_password) {
        return login_password.length() >= 4;
    }

    private boolean isEmailValid(String login_email) {

        return login_email.contains("@");
    }
}
