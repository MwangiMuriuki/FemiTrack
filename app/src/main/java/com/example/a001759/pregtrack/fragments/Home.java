package com.example.a001759.pregtrack.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a001759.pregtrack.R;
import com.example.a001759.pregtrack.activities.ActivityArticles;
import com.example.a001759.pregtrack.activities.ActivityBabyDiet;
import com.example.a001759.pregtrack.activities.ActivityBabyHealth;
import com.example.a001759.pregtrack.activities.ActivityMotherHealth;
import com.example.a001759.pregtrack.activities.ActivityMothersDiet;
import com.example.a001759.pregtrack.activities.MainActivity;
import com.example.a001759.pregtrack.activities.PregnancyInfo;
import com.example.a001759.pregtrack.adapters.HomeTipsAdapter;
import com.example.a001759.pregtrack.databinding.FragmentHomeBinding;
import com.example.a001759.pregtrack.models.HomeTipsModelClass;
import com.example.a001759.pregtrack.models.ModelClassUsers;
import com.example.a001759.pregtrack.models.ModelClassWeeklyCalendar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {

    static RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    HomeTipsAdapter adapter;

    List<HomeTipsModelClass> myList;
    final List<ModelClassUsers> myUsers = new ArrayList<>();
    List<ModelClassWeeklyCalendar> myWeek = new ArrayList<>();

    String weeksPregnant;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser;

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

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = binding.homeArticlesRV;

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        adapter = new HomeTipsAdapter(myList, getContext(), firebaseFirestore);

        recyclerView.setAdapter(adapter);

        getWeeksPregnant();

        getHomeArticles();

        /*TO ARTICLES PAGE*/
        binding.layoutReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActivityArticles.class);
                startActivity(intent);
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

    /*GET HOME PAGE ARTICLES*/
    private void getHomeArticles() {

        firebaseFirestore.collection("articles").limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
    }

    /*GET WEEKS PREGNANT*/
    private void getWeeksPregnant() {

        if (firebaseFirestore != null) {

            String userID = firebaseUser.getUid();

            firebaseFirestore.collection("Users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()){

                        DocumentSnapshot documentSnapshot = task.getResult();

                        if (documentSnapshot !=null && documentSnapshot.exists()){

                            ModelClassUsers modelClassUsers = new ModelClassUsers(
                                    documentSnapshot.getString("uName"),
                                    documentSnapshot.getString("email"),
                                    documentSnapshot.getString("display_picture"),
                                    documentSnapshot.getString("userID"),
                                    documentSnapshot.getString("due_date"),
                                    documentSnapshot.getString("weeks_pregnant"));

                            myUsers.add(modelClassUsers);

                            weeksPregnant = documentSnapshot.getString("weeks_pregnant");
                            binding.homeWeekNumber.setText("Week  " + weeksPregnant);

                            getWeekInfo(weeksPregnant);

                        }
                    }
                }
            });

        }else {

            binding.homeWeekNumber.setText("Week ...");
        }
    }

    /*GET WEEK INFO*/
    private void getWeekInfo(String weeksPregnant) {

        if (weeksPregnant !=null){

            /*GET WEEK INFO*/
            firebaseFirestore.collection("week_by_week_info").whereEqualTo("week_number", weeksPregnant).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful()){

                        for (DocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){

                            ModelClassWeeklyCalendar modelClassWeeklyCalendar = new ModelClassWeeklyCalendar(
                                    documentSnapshot.getString("image_url"),
                                    documentSnapshot.getString("week_number"),
                                    documentSnapshot.getString("baby_info"),
                                    documentSnapshot.getString("symptoms"),
                                    documentSnapshot.getString("source"),
                                    documentSnapshot.getString("intro"));

                            myWeek.add(modelClassWeeklyCalendar);

                            Uri uri = Uri.parse(documentSnapshot.getString("image_url"));

                            final String baby_info = documentSnapshot.getString("baby_info");
                            final String image_url = documentSnapshot.getString("image_url");
                            final String week_number = documentSnapshot.getString("week_number");
                            final String symptoms = documentSnapshot.getString("symptoms");
                            final String source = documentSnapshot.getString("source");
                            final String intro = documentSnapshot.getString("intro");

                            binding.homeWeekInfo.setText(Html.fromHtml(intro));

                            Glide.with(getContext()).load(uri).into(binding.homeWeekImage);

                            binding.homeReadMore.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getContext(), PregnancyInfo.class);
                                    intent.putExtra("baby_info", baby_info);
                                    intent.putExtra("image_url", image_url);
                                    intent.putExtra("week_number", week_number);
                                    intent.putExtra("symptoms", symptoms);
                                    intent.putExtra("source", source);
                                    intent.putExtra("intro", intro);
                                    startActivity(intent);
                                }
                            });
                        }
                    }else{

                        Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Home");
    }

}
