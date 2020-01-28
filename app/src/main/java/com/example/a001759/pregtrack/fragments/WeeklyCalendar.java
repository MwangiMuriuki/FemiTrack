package com.example.a001759.pregtrack.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a001759.pregtrack.R;
import com.example.a001759.pregtrack.activities.MainActivity;
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

    WeeklyCalendarAdapter adapter;
    List<ModelClassWeeklyCalendar> newList;

    FragmentWeeklyCalendarBinding binding;

    SearchView searchView;

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
                                documentSnapshot.getLong("week_number"),
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.tips_and_articles_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView = new SearchView(Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).getThemedContext());
        EditText searchPlate = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchPlate.setHint("Search");
        searchPlate.setHintTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.action_search){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {

                    List<ModelClassWeeklyCalendar> filteredWeekList = new ArrayList<>();

                    for (ModelClassWeeklyCalendar searchList : newList){

                        String searchWeekNumber = String.valueOf(searchList.getWeek_number());


                        if (searchWeekNumber.toLowerCase().contains(s.toLowerCase())){

                            filteredWeekList.add(searchList);
                        }
                    }

                    adapter.filterList(filteredWeekList);

                    return true;
                }
            });

        }
        return super.onOptionsItemSelected(item);
    }
}
