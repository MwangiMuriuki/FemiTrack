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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a001759.pregtrack.R;
import com.example.a001759.pregtrack.activities.ActivityArticles;
import com.example.a001759.pregtrack.activities.ActivityBabyDiet;
import com.example.a001759.pregtrack.activities.ActivityBabyHealth;
import com.example.a001759.pregtrack.activities.ActivityBabyProducts;
import com.example.a001759.pregtrack.activities.ActivityMotherHealth;
import com.example.a001759.pregtrack.activities.ActivityMothersDiet;
import com.example.a001759.pregtrack.activities.ActivityMothersProducts;
import com.example.a001759.pregtrack.activities.PregnancyInfo;
import com.example.a001759.pregtrack.adapters.AdapterBabyMerchandise_Home;
import com.example.a001759.pregtrack.adapters.AdapterMothersMerchandise_Home;
import com.example.a001759.pregtrack.adapters.HomeTipsAdapter;
import com.example.a001759.pregtrack.databinding.FragmentHomeBinding;
import com.example.a001759.pregtrack.models.HomeTipsModelClass;
import com.example.a001759.pregtrack.models.ModelClassMerchandise;
import com.example.a001759.pregtrack.models.ModelClassMomMerch;
import com.example.a001759.pregtrack.models.ModelClassUsers;
import com.example.a001759.pregtrack.models.ModelClassWeeklyCalendar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {

    static RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    HomeTipsAdapter adapter;
    AdapterBabyMerchandise_Home adapterBabyMerchandiseHome;
    AdapterMothersMerchandise_Home adapterMothersMerchandiseHome;

    private static final String TAG = "Home";

    List<HomeTipsModelClass> myList;
    final List<ModelClassUsers> myUsers = new ArrayList<>();
    List<ModelClassWeeklyCalendar> myWeek = new ArrayList<>();
    List<ModelClassMerchandise> merch = new ArrayList<>();
    List<ModelClassMomMerch> merhcMom = new ArrayList<>();

    String weeksPregnant;
    String daysPregnant;

    String weeks_pregnant;
    String days_pregnant;

    long wPreg;
    long dayDiff;

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

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        /*CHECK IF USER IS LOGGED IN AND DISPLAY RELEVEANT INFORMATION*/
        if (firebaseUser != null){
            binding.pBar1.setVisibility(View.VISIBLE);
            getDayOfWeek();

        }else{
            binding.pBar1.setVisibility(View.GONE);
        }

        firebaseFirestore = FirebaseFirestore.getInstance();

        myList = new ArrayList<>();
        binding.homeArticlesRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new HomeTipsAdapter(myList, getContext(), firebaseFirestore);
        binding.homeArticlesRV.setAdapter(adapter);
        getHomeArticles();

        merch = new ArrayList<>();
        binding.homeBabProdRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.homeMomProdRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapterBabyMerchandiseHome = new AdapterBabyMerchandise_Home(merch, firebaseFirestore, getContext());
        adapterMothersMerchandiseHome = new AdapterMothersMerchandise_Home(merhcMom, firebaseFirestore, getContext());
        binding.homeBabProdRV.setAdapter(adapterBabyMerchandiseHome);
        binding.homeMomProdRV.setAdapter(adapterMothersMerchandiseHome);
        getBabyProd();
        getMomProd();


        /*TO ARTICLES PAGE*/
        binding.layoutReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActivityArticles.class);
                startActivity(intent);
            }
        });

        /*TO BABY MERCHANDISE PAGE*/
        binding.babProdReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActivityBabyProducts.class);
                startActivity(intent);
            }
        });

        /*TO MOTHER`S MERCHANDISE PAGE*/
        binding.momProdReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActivityMothersProducts.class);
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

    private void getMomProd() {
        firebaseFirestore.collection("mothers_merch").limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    for (DocumentSnapshot documentSnapshot: task.getResult()){

                        ModelClassMomMerch modelClassMomMerch = new ModelClassMomMerch(
                                documentSnapshot.getString("image"),
                                documentSnapshot.getString("label"),
                                documentSnapshot.getString("price"),
                                documentSnapshot.getString("shop"));

                        merhcMom.add(modelClassMomMerch);
                    }

                    adapterMothersMerchandiseHome.notifyDataSetChanged();

                }else {

                    Toast.makeText(getContext(), "Error Getting info", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void getBabyProd() {
        firebaseFirestore.collection("baby_merch").limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    for (DocumentSnapshot documentSnapshot: task.getResult()){

                        ModelClassMerchandise modelClassMerchandise = new ModelClassMerchandise(
                                documentSnapshot.getString("image"),
                                documentSnapshot.getString("label"),
                                documentSnapshot.getString("price"),
                                documentSnapshot.getString("shop"));

                        merch.add(modelClassMerchandise);
                    }
                    adapterBabyMerchandiseHome.setList(merch);
                    adapterBabyMerchandiseHome.notifyDataSetChanged();

                }else {

                    Toast.makeText(getContext(), "Error Getting info", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    /*GET DAY OF WEEK FOR EVERY SINGLE DAY*/
    private void getDayOfWeek() {
        final Calendar cal = Calendar.getInstance();
        final int mYear = cal.get(Calendar.YEAR); // get current year
        final int mMonth = cal.get(Calendar.MONTH); // get current month
        final int mDay = cal.get(Calendar.DAY_OF_MONTH); // get today`s date

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
                                    documentSnapshot.getLong("weeks_pregnant"),
                                    documentSnapshot.getLong("days_pregnant"));

                            myUsers.add(modelClassUsers);

                            long daysPreg = documentSnapshot.getLong("days_pregnant");

                            daysPregnant = String.valueOf(daysPreg);

                            String due_date = documentSnapshot.getString("due_date");
                            String today = mDay + "-" + mMonth + "-" + mYear;
//                            String today = mDay + "-" + (mMonth + 1) + "-" + mYear;

                            Date dd= new Date(); /*due date*/
                            Date cd = new Date(); /*current date*/

                            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.US);

                            /*CONVERT STRINGS TO CALENDAR FORMAT*/
                            Calendar calendarDD = Calendar.getInstance();
                            try {
                                assert due_date != null;
                                calendarDD.setTime(sdf.parse(due_date));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            Calendar calendarTD = Calendar.getInstance();
                            try {
                                calendarTD.setTime(Objects.requireNonNull(sdf.parse(today)));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            String dueDate = sdf.format(calendarDD.getTime());
                            String currentDate = sdf.format(calendarTD.getTime());

                            try {
                                dd = sdf.parse(dueDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            try {
                                cd = sdf.parse(currentDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            long diff = Math.abs(Objects.requireNonNull(cd).getTime() - Objects.requireNonNull(dd).getTime());
                            wPreg = diff / (7 * 24 * 60 * 60 * 1000);
                            dayDiff =  diff / (24 * 60 * 60 * 1000);
                            long newWeekDiff = 40 - wPreg;
                            long daysPregnant = Math.abs(282 - dayDiff);
                            long dPreg = daysPregnant % 7;
                            days_pregnant = Long.toString(dPreg);

                            updateData(newWeekDiff, dPreg);

                        }
                    }
                }
            });

        }else {

        }

    }

    private void updateData(long newWeekDiff, long dPreg) {
        String userID = firebaseUser.getUid();

        DocumentReference documentReference = firebaseFirestore.collection("Users").document(userID);
        documentReference.update("weeks_pregnant", newWeekDiff ,  "days_pregnant", dPreg).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                getWeeksPregnant();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
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

                    binding.pBar2.setVisibility(View.GONE);
                    binding.homeArticlesRV.setVisibility(View.VISIBLE);

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
                                    documentSnapshot.getLong("weeks_pregnant"),
                                    documentSnapshot.getLong("days_pregnant"));

                            myUsers.add(modelClassUsers);

                            long weeksPreg = documentSnapshot.getLong("weeks_pregnant");
                            long daysPreg = documentSnapshot.getLong("days_pregnant");

                            weeksPregnant = String.valueOf(weeksPreg);
                            daysPregnant = String.valueOf(daysPreg);
                            binding.homeWeekNumber.setText("Week  " + weeksPregnant + ", Day " + daysPregnant);

                            getWeekInfo(weeksPreg);

                        }
                    }
                }
            });

        }else {

            binding.homeWeekNumber.setText("Week ...");
        }
    }

    /*GET WEEK INFO*/
    private void getWeekInfo(long weeksPreg) {

        if (weeksPreg != 0){

            /*GET WEEK INFO*/
            firebaseFirestore.collection("week_by_week_info").whereEqualTo("week_number", weeksPreg).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    Log.d(TAG, "onComplete: " + task.getResult().size());

                    if (task.isSuccessful()){

                        for (DocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){

                            ModelClassWeeklyCalendar modelClassWeeklyCalendar = new ModelClassWeeklyCalendar(
                                    documentSnapshot.getString("image_url"),
                                    documentSnapshot.getLong("week_number"),
                                    documentSnapshot.getString("baby_info"),
                                    documentSnapshot.getString("symptoms"),
                                    documentSnapshot.getString("source"),
                                    documentSnapshot.getString("intro"));

                            myWeek.add(modelClassWeeklyCalendar);

                            Uri uri = Uri.parse(documentSnapshot.getString("image_url"));

                            final String baby_info = documentSnapshot.getString("baby_info");
                            final String image_url = documentSnapshot.getString("image_url");
                            final String week_number = String.valueOf(documentSnapshot.getLong("week_number"));
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

                            binding.pBar1.setVisibility(View.GONE);
                            binding.relativeLayoutTop.setVisibility(View.VISIBLE);
                        }
                    }else{

                        Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_LONG).show();

                        binding.homeWeekInfo.setText("Error fetching data");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.d(TAG, "onFailure: " + e.getMessage());
                }
            });
        }else {

            Toast.makeText(getContext(), "Mmmmh, 0 weeks pregnant...well, you really aren`t pregnant......yet", Toast.LENGTH_LONG).show();
            binding.homeWeekInfo.setText("Mmmmh, 0 weeks pregnant...well, you really aren`t pregnant......yet");

        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Home");
    }

}
