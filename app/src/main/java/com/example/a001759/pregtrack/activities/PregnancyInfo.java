package com.example.a001759.pregtrack.activities;

import android.content.Intent;
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
        final String source = getIntent().getStringExtra("source");
        String intro = getIntent().getStringExtra("intro");

        binding.toolbarLayout.setTitle("Week " + week_number);
        Glide.with(PregnancyInfo.this).load(image_url).into(binding.fetusImageView);
        binding.include.babyInfoTv.setText(Html.fromHtml(baby_info));
        binding.include.symptomsTv.setText(Html.fromHtml(symptoms));
        binding.include.sourceTv.setText(Html.fromHtml(source));
        binding.include.introTv.setText(Html.fromHtml(intro));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Share with...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,Html.fromHtml(source));
                sharingIntent.setType("text/plain");
                sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));

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
