package com.example.a001759.pregtrack.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.a001759.pregtrack.R;
import com.example.a001759.pregtrack.adapters.AdapterOtherArticles;
import com.example.a001759.pregtrack.adapters.HomeTipsAdapter;
import com.example.a001759.pregtrack.databinding.ActivityArticlesBinding;
import com.example.a001759.pregtrack.models.HomeTipsModelClass;
import com.example.a001759.pregtrack.models.ModelClassOtherArticles;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActivityArticles extends AppCompatActivity {

    static RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    AdapterOtherArticles adapter;

    List<ModelClassOtherArticles> myList;

    ActivityArticlesBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_articles);
        binding.articlesPageToolBar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(binding.articlesPageToolBar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Tips and Articles");

        recyclerView = binding.recyclerView;
        firebaseFirestore = FirebaseFirestore.getInstance();
        myList = new ArrayList<>();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new AdapterOtherArticles(myList, firebaseFirestore, ActivityArticles.this);
        binding.recyclerView.setAdapter(adapter);

        firebaseFirestore.collection("articles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot documentSnapshot : task.getResult()) {

                        ModelClassOtherArticles modelClassOtherArticles = new ModelClassOtherArticles(
                                documentSnapshot.getString("image"),
                                documentSnapshot.getString("title"),
                                documentSnapshot.getString("info"));

                        myList.add(modelClassOtherArticles);
                    }

                    adapter.notifyDataSetChanged();

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
