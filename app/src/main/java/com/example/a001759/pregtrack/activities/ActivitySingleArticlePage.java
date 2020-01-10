package com.example.a001759.pregtrack.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.a001759.pregtrack.R;
import com.example.a001759.pregtrack.databinding.ActivitySingleArticlePageBinding;
import com.google.android.material.snackbar.Snackbar;

public class ActivitySingleArticlePage extends AppCompatActivity {

    ActivitySingleArticlePageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_single_article_page);
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.toolbarLayout.setTitle("Back");
        binding.toolbarLayout.setExpandedTitleColor(Color.parseColor("#00FFFFFF")); /*Hide Title when the collapsing toolbar is expanded*/

        String article_image = getIntent().getStringExtra("image");
        String article_title = getIntent().getStringExtra("title");
        String article_info = getIntent().getStringExtra("info");

        Glide.with(ActivitySingleArticlePage.this).load(article_image).into(binding.articleImage);
        binding.include.articleTitleTV.setText(article_title);
        binding.include.articleInfoTV.setText(article_info);

        binding.shareArticle.setOnClickListener(new View.OnClickListener() {
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
