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
import com.example.a001759.pregtrack.models.ModelClassMomMerch;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AdapterMotherMerchandise_Main extends RecyclerView.Adapter<AdapterMotherMerchandise_Main.MyViewHolder> {

    List<ModelClassMomMerch> list;
    FirebaseFirestore firebaseFirestore;
    Context context;

    String image, label, price, shop;

    public AdapterMotherMerchandise_Main(List<ModelClassMomMerch> list, FirebaseFirestore firebaseFirestore, Context context) {
        this.list = list;
        this.firebaseFirestore = firebaseFirestore;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterMotherMerchandise_Main.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.merchandise_layout, parent, false);

        return new AdapterMotherMerchandise_Main.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMotherMerchandise_Main.MyViewHolder holder, int position) {
        final ModelClassMomMerch modelClassMomMerch = list.get(position);

        final Uri imageUrl = Uri.parse(modelClassMomMerch.getImageUrl());
        Glide.with(context).load(imageUrl).into(holder.imageView);

        holder.tvLabel.setText(modelClassMomMerch.getLabel());
        holder.tvPrice.setText(modelClassMomMerch.getPrice());
        shop = modelClassMomMerch.getShopUrl();
        price = modelClassMomMerch.getPrice();

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(modelClassMomMerch.getShopUrl()));
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(browserIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(List<ModelClassMomMerch> filteredProductList) {

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
