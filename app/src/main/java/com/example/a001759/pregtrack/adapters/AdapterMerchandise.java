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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.a001759.pregtrack.R;
import com.example.a001759.pregtrack.models.ModelClassMerchandise;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AdapterMerchandise extends RecyclerView.Adapter<AdapterMerchandise.MyViewHolder> {

    List<ModelClassMerchandise> list;
    FirebaseFirestore firebaseFirestore;
    Context context;

    String image, label, price, shop;

    public AdapterMerchandise(List<ModelClassMerchandise> list, FirebaseFirestore firebaseFirestore, Context context) {
        this.list = list;
        this.firebaseFirestore = firebaseFirestore;
        this.context = context;
    }

    public void setList(List<ModelClassMerchandise> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterMerchandise.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.merchandise_single_item_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMerchandise.MyViewHolder holder, int position) {

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
                context.startActivity(browserIntent);
//                Toast.makeText(context, String.valueOf(modelClassMerchandise.getPrice()), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
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
