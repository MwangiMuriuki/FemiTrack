package com.example.a001759.pregtrack.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.a001759.pregtrack.R;
import com.example.a001759.pregtrack.databinding.ActivityRegisterBinding;
import com.example.a001759.pregtrack.models.ModelClassUsers;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import dmax.dialog.SpotsDialog;


public class ActivityRegister extends AppCompatActivity {

    AlertDialog registerDialog;

    FirebaseFirestore myFirestore = FirebaseFirestore.getInstance();
    StorageReference storageReference;
    private FirebaseAuth mAuth;

    String userName, email, password;
    Uri resultUri;
    UploadTask myUploadTask;

    String user_name;
    String regEmail;
    String rePassword;
    String display_picture;


    ActivityRegisterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        //CHECK IF DEVICE IS CONNECTED TO THE INTERNET
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        registerDialog = new SpotsDialog(this, R.style.registerAlert);

        mAuth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();

        binding.registerProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(ActivityRegister.this,  " Selecting Profile picture ", Toast.LENGTH_LONG).show();

                CropImage.activity()
                        .setAspectRatio(1,1)
                        .start(ActivityRegister.this);

            }
        });

        binding.cancelRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(getApplication(), Login.class);
                startActivity(home);
                finish();
            }
        });

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userName = binding.registerNameLayout.getEditText().getText().toString();
                email = binding.registerEmailLayout.getEditText().getText().toString();
                password = binding.registerPasswordLayout.getEditText().toString();

                if (userName.isEmpty()){

                    Toast.makeText(ActivityRegister.this,  " Please enter your name ", Toast.LENGTH_LONG).show();

                }else if (email.isEmpty()){

                    Toast.makeText(ActivityRegister.this,  " Please enter an Email Address ", Toast.LENGTH_LONG).show();

                }else if (password.isEmpty()){

                    Toast.makeText(ActivityRegister.this,  " Please Enter a Password ", Toast.LENGTH_LONG).show();

                } if (!isConnected){

                    Toast.makeText(getApplicationContext(), "Please check your internet connection and try again.", Toast.LENGTH_LONG).show();

                }else{

                    registerDialog.setCancelable(false);
                    registerDialog.show();
//                    registerNewUser(email, password);
                    mthdregisterUser(email,password);
                }
            }
        });

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                resultUri = result.getUri();

                binding.registerProfilePic.setImageURI(resultUri);

                String downloadUri = resultUri.toString();

                Toast.makeText(ActivityRegister.this, downloadUri, Toast.LENGTH_LONG).show();

            }
        }

    }

    private void mthdregisterUser(final String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            registerDialog.cancel();
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            final String userID = mAuth.getCurrentUser().getUid();
                            user_name = binding.registerNameLayout.getEditText().getText().toString();

                            ModelClassUsers modelClassUsers = new ModelClassUsers(user_name, email, display_picture, userID, null, 0);
                            myFirestore.collection("Users").document(userID).set(modelClassUsers).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){

                                        Toast.makeText(ActivityRegister.this,  "Registration Successful!!!", Toast.LENGTH_LONG).show();

                                    } else{

                                        Toast.makeText(getApplicationContext(), "Account Creation failed. Please try again Later. ", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });

                            updateUI(user); /*DIRECT USER TO THE NEXT PAGE AFTER OPERTAION IS SUCCESFUL*/

                        } else {
                            registerDialog.cancel();

                            Toast.makeText(getApplicationContext(), "Error Creating Account. Please try again Later. ", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    private void registerNewUser(final String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            registerDialog.cancel();
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            final String userID = mAuth.getCurrentUser().getUid();
                            user_name = binding.registerNameLayout.getEditText().getText().toString();

                            if (resultUri!=null) {

                                final StorageReference mStorageRef = storageReference.child("Profile_Images").child(user_name);
                                myUploadTask = mStorageRef.putFile(resultUri);

                                final Task<Uri> urlTask = myUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if (!task.isSuccessful()) {
                                            throw task.getException();
                                        }

                                        /*Continue with the task to get the download URL*/
                                        return mStorageRef.getDownloadUrl();
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {

                                            Uri downloadUri = task.getResult();

                                            assert downloadUri != null;
                                            display_picture = downloadUri.toString();

                                            ModelClassUsers modelClassUsers = new ModelClassUsers(user_name, email, display_picture, userID, null, 0);
                                            myFirestore.collection("Users").document(userID).set(modelClassUsers).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()){

                                                        Toast.makeText(ActivityRegister.this,  "Registration Successful!!!", Toast.LENGTH_LONG).show();

                                                    }
                                                    else{

                                                        Toast.makeText(getApplicationContext(), "Account Creation failed. Please try again Later. ", Toast.LENGTH_LONG).show();

                                                    }
                                                }
                                            });

                                        } else {
                                            Toast.makeText(getApplicationContext(), "Error Creating Account. Please try again Later. ", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            }else{

                                Toast.makeText(getApplicationContext(), "Please select a profile picture. ", Toast.LENGTH_LONG).show();

                            }

                            updateUI(user); /*DIRECT USER TO THE NEXT PAGE AFTER OPERTAION IS SUCCESFUL*/

                        } else {

                           /*If registration in fails, display a message to the user.*/

                            registerDialog.cancel();

                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void updateUI(FirebaseUser user) {

        Intent nextPage = new Intent(getApplicationContext(), ActivityPregnancyCalculator.class);
        startActivity(nextPage);
        finish();
    }
}
