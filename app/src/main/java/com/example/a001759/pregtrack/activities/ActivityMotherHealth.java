package com.example.a001759.pregtrack.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.a001759.pregtrack.R;
import com.example.a001759.pregtrack.adapters.AdapterOtherArticles;
import com.example.a001759.pregtrack.databinding.ActivityMotherHealthBinding;
import com.example.a001759.pregtrack.models.ModelClassOtherArticles;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActivityMotherHealth extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    AdapterOtherArticles adapterOtherArticles;
    List<ModelClassOtherArticles> list;

    ActivityMotherHealthBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mother_health);
        binding.mothersHealthToolBar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(binding.mothersHealthToolBar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Mother`s Health");

        list = new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        adapterOtherArticles = new AdapterOtherArticles(list, firebaseFirestore, ActivityMotherHealth.this);
        binding.recyclerView.setAdapter(adapterOtherArticles);

        firebaseFirestore.collection("mothers_health_articles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot documentSnapshot : task.getResult()) {

                        ModelClassOtherArticles modelClassOtherArticles = new ModelClassOtherArticles(
                                documentSnapshot.getString("image"),
                                documentSnapshot.getString("title"),
                                documentSnapshot.getString("info"));

                        list.add(modelClassOtherArticles);
                    }

                    adapterOtherArticles.notifyDataSetChanged();

                } else {

                    Toast.makeText(getApplicationContext(), "Error Getting info", Toast.LENGTH_SHORT).show();

                }
            }

        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
