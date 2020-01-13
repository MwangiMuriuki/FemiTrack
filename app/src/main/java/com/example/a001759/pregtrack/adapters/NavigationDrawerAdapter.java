package com.example.a001759.pregtrack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a001759.pregtrack.R;
import com.example.a001759.pregtrack.models.ModelNavigationDrawer;

import java.util.List;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {

    Context context;
    List<ModelNavigationDrawer> itemList;
    NavigationDrawerListener listener;

    public interface NavigationDrawerListener{

        void OnNavMenuItemSelected(ModelNavigationDrawer modelNavigationDrawer);
    }


    public NavigationDrawerAdapter(Context context, List<ModelNavigationDrawer> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public void setDrawerListener(NavigationDrawerListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public NavigationDrawerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.navigation_drawer_single_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NavigationDrawerAdapter.MyViewHolder holder, int position) {

        final ModelNavigationDrawer modelNavigationDrawer = itemList.get(position);

        holder.itemLabel.setText(modelNavigationDrawer.getItem_name());
        holder.itemIcon.setImageResource(modelNavigationDrawer.getImage_resource());
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.OnNavMenuItemSelected(modelNavigationDrawer);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itemLabel;
        ImageView itemIcon;
        LinearLayout itemLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemLabel = itemView.findViewById(R.id.navItemLabel);
            itemIcon = itemView.findViewById(R.id.navItemIcon);
            itemLayout = itemView.findViewById(R.id.navLinearLayout);
        }
    }
}
