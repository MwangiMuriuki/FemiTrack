package com.example.a001759.pregtrack.fragments;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.example.a001759.pregtrack.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class PregnancyCalculator extends Fragment{
    RadioGroup radioGroup;
    RadioButton setDD, calculateDD;
    Button dd_calculator, dd_selector;
    LinearLayout setDateLayout, calculateDateLayout;
    CardView cardView1, cardView2;
    TextView selected, months;

    DatePicker datePicker;
    DatePickerDialog datePickerDialog;
    Calendar calendar;
    int day, month, year;

    public static final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";

    public PregnancyCalculator() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pregnancy_calculator, container, false);

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        radioGroup = view.findViewById(R.id.radioGroupOptions);
        setDD = view.findViewById(R.id.radioButton1);
        calculateDD = view.findViewById(R.id.radioButton2);
        setDateLayout = view.findViewById(R.id.layoutSetDate);
        calculateDateLayout = view.findViewById(R.id.layoutCalculateDate);
        cardView1 = view.findViewById(R.id.card1);
        cardView2 = view.findViewById(R.id.card2);
        dd_calculator = view.findViewById(R.id.dd_calculator);
        dd_selector = view.findViewById(R.id.dd_selector);
        selected = view.findViewById(R.id.selected_date);
        months = view.findViewById(R.id.months_pregnant_1);

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

        dd_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDueDate();
            }
        });

        dd_calculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateDueDate();
            }
        });

        return view;
    }

    private void selectDueDate()  {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int sYear, int sMonth, int sDay) {

                Toast.makeText(getContext(), sDay + "/" + MONTHS[sMonth] + "/" + sYear, Toast.LENGTH_LONG ).show();

                selected.setText("Your Approximate Due Date is: " + sDay + "th " + MONTHS[sMonth] + " " + sYear);

                /*ARITHMETIC TO ADD DATE*/

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String date = String.valueOf(sYear + sMonth + sDay);
                try {
                    c.setTime(sdf.parse(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                c.add(Calendar.MONTH, 9);
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
                String output = sdf1.format(c.getTime());

                months.setText(output);

            }
        },mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    private void calculateDueDate() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Pregnancy Calculator");
    }

}
