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
import com.example.a001759.pregtrack.models.HomeTipsModelClass;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class HomeTipsAdapter extends RecyclerView.Adapter<HomeTipsAdapter.MyViewHolder> {

    List<HomeTipsModelClass> list;
    Context context;
    FirebaseFirestore firestore;

    String image, title, info;

    public HomeTipsAdapter(List<HomeTipsModelClass> list, Context context, FirebaseFirestore firestore) {
        this.list = list;
        this.context = context;
        this.firestore = firestore;
    }

    @NonNull
    @Override
    public HomeTipsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_tips_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeTipsAdapter.MyViewHolder holder, int position) {

        final HomeTipsModelClass modelClass = list.get(position);

        final Uri imageUrl = Uri.parse(modelClass.getImage());
        Glide.with(context).load(imageUrl).into(holder.articleImage);

        holder.articleTitle.setText(modelClass.getTitle());
        holder.articleInfo.setText(modelClass.getInfo());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                image = modelClass.getImage();
                title = modelClass.getTitle();
                info = modelClass.getInfo();

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

        ImageView articleImage;
        TextView articleTitle;
        TextView articleInfo;
        LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            articleImage = itemView.findViewById(R.id.articleImage);
            articleTitle = itemView.findViewById(R.id.articleTitle);
            articleInfo = itemView.findViewById(R.id.articleInfo);
            linearLayout = itemView.findViewById(R.id.cardLayout);
        }
    }
}
