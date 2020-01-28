package com.example.a001759.pregtrack.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
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

    SearchView searchView;

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
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.tips_and_articles_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView = new SearchView(Objects.requireNonNull(((ActivityArticles) Objects.requireNonNull(this)).getSupportActionBar()).getThemedContext());
        EditText searchPlate = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchPlate.setHint("Search...");
        searchPlate.setHintTextColor(ContextCompat.getColor(ActivityArticles.this, R.color.colorWhite));
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        if(item.getItemId() == R.id.action_search){

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    List<ModelClassOtherArticles> filteredArticleList = new ArrayList<>();

                    for (ModelClassOtherArticles searchList : myList){

                        String searchArticleTitle = searchList.getTitle();


                        if (searchArticleTitle.toLowerCase().contains(newText.toLowerCase())){

                            filteredArticleList.add(searchList);
                        }
                    }

                    adapter.filterList(filteredArticleList);
                    return false;
                }
            });


        }
        return super.onOptionsItemSelected(item);
    }
}
