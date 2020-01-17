package com.example.a001759.pregtrack.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.a001759.pregtrack.R;
import com.example.a001759.pregtrack.adapters.WeeklyCalendarAdapter;
import com.example.a001759.pregtrack.databinding.FragmentWeeklyCalendarBinding;
import com.example.a001759.pregtrack.models.ModelClassWeeklyCalendar;
import com.example.a001759.pregtrack.models.weekly_calendar_model_class;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeeklyCalendar extends Fragment {

    static RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    static GridLayoutManager gridLayoutManager;

    List<weekly_calendar_model_class> list;
    WeeklyCalendarAdapter adapter;
    List<ModelClassWeeklyCalendar> newList;

    FragmentWeeklyCalendarBinding binding;

    public WeeklyCalendar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_weekly_calendar, container, false);
        setHasOptionsMenu(true);

        newList = new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = binding.recyclerView;
        gridLayoutManager = new GridLayoutManager(Objects.requireNonNull(getActivity()).getApplicationContext(), 2);

        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new WeeklyCalendarAdapter(newList, getContext(), firebaseFirestore);

        recyclerView.setAdapter(adapter);

        firebaseFirestore.collection("week_by_week_info").orderBy("week_number", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){

                    for (DocumentSnapshot documentSnapshot: task.getResult()) {

                        ModelClassWeeklyCalendar modelClassWeeklyCalendar = new ModelClassWeeklyCalendar(
                                documentSnapshot.getString("image_url"),
                                documentSnapshot.getString("week_number"),
                                documentSnapshot.getString("baby_info"),
                                documentSnapshot.getString("symptoms"),
                                documentSnapshot.getString("source"),
                                documentSnapshot.getString("intro"));

                        newList.add(modelClassWeeklyCalendar);

                    }

                    adapter.notifyDataSetChanged();

                }else {

                    Toast.makeText(getContext(), "Error Getting info", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Weekly Calendar");
    }


}
