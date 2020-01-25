package com.example.a001759.pregtrack.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.a001759.pregtrack.R;
import com.example.a001759.pregtrack.models.ModelClassMomMerch;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AdapterMothersMerchandise extends RecyclerView.Adapter<AdapterMothersMerchandise.MyViewHolder> {


    List<ModelClassMomMerch> list;
    FirebaseFirestore firebaseFirestore;
    Context context;

    String image, label, price, shop;

    public AdapterMothersMerchandise(List<ModelClassMomMerch> list, FirebaseFirestore firebaseFirestore, Context context) {
        this.list = list;
        this.firebaseFirestore = firebaseFirestore;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterMothersMerchandise.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.merchandise_single_item_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMothersMerchandise.MyViewHolder holder, int position) {

        ModelClassMomMerch modelClassMerchandise = list.get(position);

        final Uri imageUrl = Uri.parse(modelClassMerchandise.getImageUrl());
        Glide.with(context).load(imageUrl).into(holder.imageView);

        holder.tvLabel.setText(modelClassMerchandise.getLabel());
        holder.tvPrice.setText(modelClassMerchandise.getPrice());
        shop = modelClassMerchandise.getShopUrl();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvLabel;
        TextView tvPrice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.itemImage);
            tvLabel = itemView.findViewById(R.id.itemName);
            tvPrice = itemView.findViewById(R.id.itemPrice);
        }
    }
}
