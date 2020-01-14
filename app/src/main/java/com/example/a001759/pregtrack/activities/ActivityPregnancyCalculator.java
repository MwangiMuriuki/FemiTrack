package com.example.a001759.pregtrack.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.a001759.pregtrack.R;
import com.example.a001759.pregtrack.databinding.ActivityPregnancyCalculatorBinding;

import java.util.Objects;

public class ActivityPregnancyCalculator extends AppCompatActivity {

    ActivityPregnancyCalculatorBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pregnancy_calculator);
        binding.pregnancyCalcToolBar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(binding.pregnancyCalcToolBar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Pregnancy Calculator");
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

}
