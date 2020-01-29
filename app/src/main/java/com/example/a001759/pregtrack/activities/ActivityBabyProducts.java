package com.example.a001759.pregtrack.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a001759.pregtrack.R;
import com.example.a001759.pregtrack.adapters.AdapterBabyMerchandise_Main;
import com.example.a001759.pregtrack.databinding.ActivityBabyProductsBinding;
import com.example.a001759.pregtrack.models.ModelClassMerchandise;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActivityBabyProducts extends AppCompatActivity {

    List<ModelClassMerchandise> babyMerchList;
    AdapterBabyMerchandise_Main adapterBabyMerchandise;
    FirebaseFirestore firebaseFirestore;
    static GridLayoutManager gridLayoutManager;

    SearchView searchView;

    ActivityBabyProductsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_baby_products);
        binding.babyProductsToolBar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(binding.babyProductsToolBar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Baby Products");

        babyMerchList = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();

        gridLayoutManager = new GridLayoutManager(Objects.requireNonNull(getApplicationContext()).getApplicationContext(), 2);
        binding.babyProdRV.setLayoutManager(gridLayoutManager);
        adapterBabyMerchandise = new AdapterBabyMerchandise_Main(babyMerchList, firebaseFirestore, getApplicationContext());
        binding.babyProdRV.setAdapter(adapterBabyMerchandise);

        firebaseFirestore.collection("baby_merch").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    for (DocumentSnapshot documentSnapshot: task.getResult()){

                        ModelClassMerchandise modelClassMerchandise = new ModelClassMerchandise(
                                documentSnapshot.getString("image"),
                                documentSnapshot.getString("label"),
                                documentSnapshot.getString("price"),
                                documentSnapshot.getString("shop"));

                        babyMerchList.add(modelClassMerchandise);
                    }
                    adapterBabyMerchandise.notifyDataSetChanged();

                }else {

                    Toast.makeText(getApplicationContext(), "Error Getting info", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.tips_and_articles_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView = new SearchView(Objects.requireNonNull(((ActivityBabyProducts) Objects.requireNonNull(this)).getSupportActionBar()).getThemedContext());
        EditText searchPlate = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchPlate.setHint("Search Products...");
        searchPlate.setHintTextColor(ContextCompat.getColor(ActivityBabyProducts.this, R.color.colorWhite));
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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
                    List<ModelClassMerchandise> filteredProductList = new ArrayList<>();

                    for (ModelClassMerchandise searchList : babyMerchList){

                        String searchProductLabel = searchList.getLabel();
                        String searchProductPrice = searchList.getPrice();


                        if (searchProductLabel.toLowerCase().contains(newText.toLowerCase()) || searchProductPrice.toLowerCase().contains(newText.toLowerCase())){

                            filteredProductList.add(searchList);
                        }
                    }

                    adapterBabyMerchandise.filterList(filteredProductList);
                    return false;
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

}
