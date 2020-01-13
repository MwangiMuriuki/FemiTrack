package com.example.a001759.pregtrack.activities;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.a001759.pregtrack.adapters.NavigationDrawerAdapter;
import com.example.a001759.pregtrack.databinding.ActivityMainBinding;
import com.example.a001759.pregtrack.fragments.Home;
import com.example.a001759.pregtrack.R;
import com.example.a001759.pregtrack.fragments.Appointments;
import com.example.a001759.pregtrack.fragments.HealthCenters;
import com.example.a001759.pregtrack.fragments.PregnancyCalculator;
import com.example.a001759.pregtrack.fragments.WeeklyCalendar;
import com.example.a001759.pregtrack.models.ModelClassUsers;
import com.example.a001759.pregtrack.models.ModelNavigationDrawer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseUser firebaseUser;

    final List<ModelClassUsers> myList = new ArrayList<>();

    private RecyclerView recyclerView;
    private NavigationDrawerAdapter navDrawerAdapter;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        recyclerView = findViewById(R.id.navItemslist);

        showMainFragment();
        setupNavDrawerMenu(firebaseUser);

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent logout = new Intent(getApplication(), Login.class);
                startActivity(logout);
                finish();
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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

                            myList.add(modelClassUsers);

                            String uname = documentSnapshot.getString("uName");
                            binding.profileUserName.setText(uname);

                        }
                    }
                }
            });

        }else {

            binding.profileUserName.setText("");
        }

//        displaySelectedScreen(R.id.nav_home);
    }

    private void showMainFragment() {
        Fragment fragment = new Home();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                //.addToBackStack(getString(R.string.revenue_fragment))
                .commit();
    }

    private void setupNavDrawerMenu(FirebaseUser firebaseUser) {

        List<ModelNavigationDrawer> navDrawerItems = new ArrayList<>();

        /*Home*/
        ModelNavigationDrawer home = new ModelNavigationDrawer();
        home.setItem_name(getString(R.string.shortcut_home));
        home.setFragment(true);
        home.setFragmentName(new Home());
        home.setImage_resource(R.drawable.ic_home);
        navDrawerItems.add(home);

        /*WEEKLY CALENDAR*/
        ModelNavigationDrawer weekly_calendar = new ModelNavigationDrawer();
        weekly_calendar.setItem_name(getString(R.string.shortcut_weekly_calendar));
        weekly_calendar.setFragment(true);
        weekly_calendar.setFragmentName(new WeeklyCalendar());
        weekly_calendar.setImage_resource(R.drawable.ic_calendar);
        navDrawerItems.add(weekly_calendar);

        /*PREGNANCY CALCULATOR*/
        ModelNavigationDrawer profile = new ModelNavigationDrawer();
        profile.setItem_name(getString(R.string.preg_calc));
        profile.setActivity(true);
        profile.setActivityName(ActivityPregnancyCalculator.class);
        profile.setImage_resource(R.drawable.ic_calculator);
        navDrawerItems.add(profile);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        navDrawerAdapter = new NavigationDrawerAdapter(MainActivity.this, navDrawerItems);
        recyclerView.setAdapter(navDrawerAdapter);

        navDrawerAdapter.setDrawerListener(new NavigationDrawerAdapter.NavigationDrawerListener() {
            @Override
            public void OnNavMenuItemSelected(ModelNavigationDrawer modelNavigationDrawer) {

                if (modelNavigationDrawer!=null){

                    Fragment fragment;

                    /*HOME*/
                    if (modelNavigationDrawer.getItem_name().equals(getString(R.string.shortcut_home))){
                        fragment = modelNavigationDrawer.getFragmentName();
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, fragment);
                        ft.commit();
                    }

                    /*WEEKLY CALENDAR*/
                    if (modelNavigationDrawer.getItem_name().equals(getString(R.string.shortcut_weekly_calendar))){
                        fragment = modelNavigationDrawer.getFragmentName();
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, fragment);
                        ft.commit();
                    }

                    /*PREGNANCY CALCULATOR*/

                    if (modelNavigationDrawer.getItem_name().equals(getString(R.string.preg_calc))){
                        Intent intent = new Intent(MainActivity.this, modelNavigationDrawer.getActivityName());
                        startActivity(intent);
                    }

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);

                }

            }
        });
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);

        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_home:
                fragment = new Home();
                break;
            case R.id.nav_pregnancy_calculator:
                fragment = new PregnancyCalculator();
                break;
            case R.id.nav_weekly_calendar:
                fragment = new WeeklyCalendar();
                break;
            case R.id.nav_appointments:
                fragment = new Appointments();
                break;
            case R.id.nav_health_centers:
                fragment = new HealthCenters();
                break;
            case R.id.nav_logout:
                logout();
                break;

        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void logout() {

        Intent logout = new Intent(getApplication(), Login.class);
        startActivity(logout);
        finish();
    }
}
