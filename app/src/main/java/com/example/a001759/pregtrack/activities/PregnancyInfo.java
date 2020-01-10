package com.example.a001759.pregtrack.activities;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.a001759.pregtrack.databinding.ActivityPregnancyInfoBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.text.Html;
import android.view.MenuItem;
import android.view.View;

import com.example.a001759.pregtrack.R;

public class PregnancyInfo extends AppCompatActivity {

    ActivityPregnancyInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pregnancy_info);
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationIcon(R.drawable.ic_back);

        String baby_info = getIntent().getStringExtra("baby_info");
        String image_url = getIntent().getStringExtra("image_url");
        String week_number = getIntent().getStringExtra("week_number");
        String symptoms = getIntent().getStringExtra("symptoms");
        String source = getIntent().getStringExtra("source");

        binding.toolbarLayout.setTitle("Week " + week_number);
        Glide.with(PregnancyInfo.this).load(image_url).into(binding.fetusImageView);
        binding.include.babyInfoTv.setText(Html.fromHtml(baby_info));
        binding.include.symptomsTv.setText(Html.fromHtml(symptoms));
        binding.include.sourceTv.setText(Html.fromHtml(source));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Share with...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
