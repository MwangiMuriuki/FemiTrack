package com.example.a001759.pregtrack.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.a001759.pregtrack.R;
import com.example.a001759.pregtrack.activities.ActivityBabyDiet;
import com.example.a001759.pregtrack.activities.ActivityBabyHealth;
import com.example.a001759.pregtrack.activities.ActivityMotherHealth;
import com.example.a001759.pregtrack.activities.ActivityMothersDiet;
import com.example.a001759.pregtrack.activities.ActivitySingleArticlePage;
import com.example.a001759.pregtrack.activities.PregnancyInfo;
import com.example.a001759.pregtrack.adapters.HomeTipsAdapter;
import com.example.a001759.pregtrack.databinding.FragmentHomeBinding;
import com.example.a001759.pregtrack.models.HomeTipsModelClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.LineNumberInputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {

    static RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    HomeTipsAdapter adapter;

    List<HomeTipsModelClass> myList;


    FragmentHomeBinding binding;
    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        myList = new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = binding.homeArticlesRV;

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        adapter = new HomeTipsAdapter(myList, getContext(), firebaseFirestore);

        recyclerView.setAdapter(adapter);

        firebaseFirestore.collection("articles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    for (DocumentSnapshot documentSnapshot: task.getResult()){

                        HomeTipsModelClass homeTipsModelClass = new HomeTipsModelClass(
                                documentSnapshot.getString("image"),
                                documentSnapshot.getString("title"),
                                documentSnapshot.getString("info"));

                        myList.add(homeTipsModelClass);
                     }

                    adapter.notifyDataSetChanged();

                }else {

                    Toast.makeText(getContext(), "Error Getting info", Toast.LENGTH_SHORT).show();

                }
            }
        });

        binding.homeReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), ActivitySingleArticlePage.class);
//                startActivity(intent);
            }
        });

        binding.mothersDietLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActivityMothersDiet.class);
                startActivity(intent);
            }
        });

        binding.babysDietLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActivityBabyDiet.class);
                startActivity(intent);
            }
        });

        binding.mothersHealthLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActivityMotherHealth.class);
                startActivity(intent);
            }
        });

        binding.babysHealthLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActivityBabyHealth.class);
                startActivity(intent);
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Home");
    }

}
