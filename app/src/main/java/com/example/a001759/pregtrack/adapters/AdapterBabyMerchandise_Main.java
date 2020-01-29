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
import com.example.a001759.pregtrack.models.ModelClassMerchandise;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AdapterBabyMerchandise_Main extends RecyclerView.Adapter<AdapterBabyMerchandise_Main.MyViewHolder> {

    List<ModelClassMerchandise> list;
    FirebaseFirestore firebaseFirestore;
    Context context;

    String image, label, price, shop;


    public AdapterBabyMerchandise_Main(List<ModelClassMerchandise> list, FirebaseFirestore firebaseFirestore, Context context) {
        this.list = list;
        this.firebaseFirestore = firebaseFirestore;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterBabyMerchandise_Main.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.merchandise_layout, parent, false);

        return new AdapterBabyMerchandise_Main.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBabyMerchandise_Main.MyViewHolder holder, int position) {

        final ModelClassMerchandise modelClassMerchandise = list.get(position);

        final Uri imageUrl = Uri.parse(modelClassMerchandise.getImageUrl());
        Glide.with(context).load(imageUrl).into(holder.imageView);

        holder.tvLabel.setText(modelClassMerchandise.getLabel());
        holder.tvPrice.setText(modelClassMerchandise.getPrice());
        shop = modelClassMerchandise.getShopUrl();
        price = modelClassMerchandise.getPrice();

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(modelClassMerchandise.getShopUrl()));
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(browserIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(List<ModelClassMerchandise> filteredProductList) {

        list = filteredProductList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tvLabel;
        TextView tvPrice;
        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.itemImage);
            tvLabel = itemView.findViewById(R.id.itemName);
            tvPrice = itemView.findViewById(R.id.itemPrice);
            linearLayout = itemView.findViewById(R.id.cardLayoutMerch);
        }
    }
}
