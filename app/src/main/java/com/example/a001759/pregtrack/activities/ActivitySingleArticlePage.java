package com.example.a001759.pregtrack.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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

        final String article_image = getIntent().getStringExtra("image");
        final String article_title = getIntent().getStringExtra("title");
        final String article_info = getIntent().getStringExtra("info");

        Glide.with(ActivitySingleArticlePage.this).load(article_image).into(binding.articleImage);
        binding.include.articleTitleTV.setText(article_title);
        binding.include.articleInfoTV.setText(Html.fromHtml(article_info));
        binding.include.articleInfoTV.setMovementMethod(LinkMovementMethod.getInstance());

        binding.shareArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Share with...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Uri imgUri = Uri.parse(MediaStore.Images.Media.EXTERNAL_CONTENT_URI + "/" + article_image);

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("*/*");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, article_title);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(article_info));
                sharingIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
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
