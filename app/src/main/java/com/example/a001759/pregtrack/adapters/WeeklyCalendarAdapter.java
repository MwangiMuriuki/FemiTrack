package com.example.a001759.pregtrack.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.a001759.pregtrack.R;
import com.example.a001759.pregtrack.activities.PregnancyInfo;
import com.example.a001759.pregtrack.models.ModelClassWeeklyCalendar;
import com.example.a001759.pregtrack.models.weekly_calendar_model_class;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class WeeklyCalendarAdapter extends RecyclerView.Adapter<WeeklyCalendarAdapter.MyViewHolder> {

    Context context;
    FirebaseFirestore firestore;
    List<ModelClassWeeklyCalendar> newList;

    String baby_info, image_url, week_number, symptoms, source, intro;

    public WeeklyCalendarAdapter(Context context, FirebaseFirestore firestore, List<ModelClassWeeklyCalendar> newList) {

    }

    public WeeklyCalendarAdapter(List<ModelClassWeeklyCalendar> newList, Context context, FirebaseFirestore firebaseFirestore) {
        this.context = context;
        this.firestore = firestore;
        this.newList = newList;
    }

    @NonNull
    @Override
    public WeeklyCalendarAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.weekly_calendar_main_layout, parent, false);

        return new MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeeklyCalendarAdapter.MyViewHolder holder, int position) {

        final ModelClassWeeklyCalendar modelClassWeeklyCalendar = newList.get(position);

        holder.weekImg = Uri.parse(modelClassWeeklyCalendar.getWeek_picture());
        holder.week_number.setText("Week " + modelClassWeeklyCalendar.getWeek_number());
        Glide.with(context).load(holder.weekImg).into(holder.week_image);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                baby_info = modelClassWeeklyCalendar.getBaby_info();
                image_url = modelClassWeeklyCalendar.getWeek_picture();
                week_number = String.valueOf(modelClassWeeklyCalendar.getWeek_number());
                symptoms = modelClassWeeklyCalendar.getSymptoms();
                source = modelClassWeeklyCalendar.getSource();
                intro = modelClassWeeklyCalendar.getIntro();

                Intent intent = new Intent(context, PregnancyInfo.class);
                intent.putExtra("baby_info", baby_info);
                intent.putExtra("image_url", image_url);
                intent.putExtra("week_number", week_number);
                intent.putExtra("symptoms", symptoms);
                intent.putExtra("source", source);
                intent.putExtra("intro", intro);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return newList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView week_number;
        Uri weekImg;
        String weekImg_String;
        CircleImageView week_image;
        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            week_number = itemView.findViewById(R.id.weekNumber);
            week_image = itemView.findViewById(R.id.weekImage);
            linearLayout = itemView.findViewById(R.id.layout);
        }
    }
}
