package com.example.a001759.pregtrack.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseUser firebaseUser;

    final List<ModelClassUsers> myList = new ArrayList<>();

    private RecyclerView recyclerView;
    private NavigationDrawerAdapter navDrawerAdapter;

    public static String weeksPregnant;

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

                if (firebaseUser!=null){
                    Intent logout = new Intent(getApplicationContext(), Login.class);
                    mAuth.signOut();
                    binding.profileUserName.setText(" ");
                    startActivity(logout);
                    finish();
                }else{
                    Intent logout = new Intent(getApplication(), Login.class);
                    startActivity(logout);
                    finish();
                }
            }
        });

        if (firebaseFirestore != null && firebaseUser != null) {

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

                            myList.add(modelClassUsers);

                            String uname = documentSnapshot.getString("uName");
                            weeksPregnant = String.valueOf(documentSnapshot.getLong("weeks_pregnant"));
                            binding.profileUserName.setText(uname);
                            binding.logoutTextView.setText("LOGOUT");

                        }
                    }
                }
            });

        }else {

            binding.profileUserName.setText("FemiTrack");
            binding.logoutTextView.setText("LOGIN/REGISTER");
        }

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

    private void setupNavDrawerMenu(final FirebaseUser firebaseUser) {

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

                        if (firebaseUser!=null){

                            Intent intent = new Intent(MainActivity.this, modelNavigationDrawer.getActivityName());
                            startActivity(intent);
                        }else {

                            final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                            alertDialog.setTitle("ALERT");
                            alertDialog.setIcon(R.drawable.ic_alert);
                            alertDialog.setMessage("Please Login or Register to proceed");
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Register", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent register = new Intent(MainActivity.this , ActivityRegister.class);
                                    startActivity(register);

                                }
                            });

                            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Login", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent login = new Intent(MainActivity.this , Login.class);
                                    startActivity(login);

                                }
                            });

                            alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.dismiss();
                                }
                            });

                            alertDialog.show();
                        }

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
//        getMenuInflater().inflate(R.menu.main, menu);
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

}
