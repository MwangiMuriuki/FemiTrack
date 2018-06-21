package com.example.a001759.pregtrack;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class PregnancyCalculator extends Fragment {
    RadioGroup radioGroup;
    RadioButton setDD, calculateDD;
    LinearLayout setDateLayout, calculateDateLayout;
    CardView cardView1, cardView2;



    public PregnancyCalculator() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pregnancy_calculator, container, false);

        radioGroup = view.findViewById(R.id.radioGroupOptions);
        setDD = view.findViewById(R.id.radioButton1);
        calculateDD = view.findViewById(R.id.radioButton2);
        setDateLayout = view.findViewById(R.id.layoutSetDate);
        calculateDateLayout = view.findViewById(R.id.layoutCalculateDate);
        cardView1 = view.findViewById(R.id.card1);
        cardView2 = view.findViewById(R.id.card2);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){

                    case R.id.radioButton1:
                        Toast.makeText(getContext(), "Due Date Picker selected", Toast.LENGTH_LONG).show();
                        cardView2.setVisibility(View.GONE);
                        cardView1.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radioButton2:
                        Toast.makeText(getContext(), "Due Date Calculator Seleted", Toast.LENGTH_LONG).show();
                        cardView2.setVisibility(View.VISIBLE);
                        cardView1.setVisibility(View.GONE);
                        break;
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Pregnancy Calculator");
    }

}
