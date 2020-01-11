package com.example.a001759.pregtrack.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.a001759.pregtrack.R;
import com.example.a001759.pregtrack.activities.ActivitySingleArticlePage;
import com.example.a001759.pregtrack.models.ModelClassOtherArticles;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterOtherArticles extends RecyclerView.Adapter<AdapterOtherArticles.MyViewHolder> {

    List<ModelClassOtherArticles> list;
    FirebaseFirestore firebaseFirestore;
    Context context;

    String image, title, info;

    public AdapterOtherArticles(List<ModelClassOtherArticles> list, FirebaseFirestore firebaseFirestore, Context context) {
        this.list = list;
        this.firebaseFirestore = firebaseFirestore;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterOtherArticles.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.other_articles_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOtherArticles.MyViewHolder holder, int position) {

        final ModelClassOtherArticles modelClassOtherArticles = list.get(position);

        final Uri imageUrl = Uri.parse(modelClassOtherArticles.getImage());
        Glide.with(context).load(imageUrl).into(holder.imageView2);

        holder.textView.setText(modelClassOtherArticles.getTitle());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                image = modelClassOtherArticles.getImage();
                title = modelClassOtherArticles.getTitle();
                info = modelClassOtherArticles.getInfo();

                Intent intent = new Intent(context, ActivitySingleArticlePage.class);
                intent.putExtra("image", image);
                intent.putExtra("title", title);
                intent.putExtra("info", info);

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView textView;
        LinearLayout linearLayout;
        ImageView imageView2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView2 = itemView.findViewById(R.id.otherArticlesImage);
            textView = itemView.findViewById(R.id.otherArticleTitle);
            linearLayout = itemView.findViewById(R.id.linearLayoutArticles);

        }
    }
}
