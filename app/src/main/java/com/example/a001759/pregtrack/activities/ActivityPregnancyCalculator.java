package com.example.a001759.pregtrack.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.a001759.pregtrack.R;
import com.example.a001759.pregtrack.databinding.ActivityPregnancyCalculatorBinding;

import net.danlew.android.joda.JodaTimeAndroid;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ActivityPregnancyCalculator extends AppCompatActivity {

    DatePickerDialog datePickerDialog;
    Calendar calendar;
    int day, month, year;

    String selectedDate;
    String currentDate;


    public static final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};


    ActivityPregnancyCalculatorBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pregnancy_calculator);
        binding.pregnancyCalcToolBar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(binding.pregnancyCalcToolBar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Pregnancy Calculator");

        JodaTimeAndroid.init(this);

        binding.radioGroupOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){

                    case R.id.rbSet:
                        Toast.makeText(getApplicationContext(), "Due Date Picker selected", Toast.LENGTH_LONG).show();
                        binding.cardCalculate.setVisibility(View.GONE);
                        binding.cardSet.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rbCalculate:
                        Toast.makeText(getApplicationContext(), "Due Date Calculator Seleted", Toast.LENGTH_LONG).show();
                        binding.cardCalculate.setVisibility(View.VISIBLE);
                        binding.cardSet.setVisibility(View.GONE);
                        break;
                }
            }
        });

        binding.ddSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDueDate();
            }
        });

        binding.ddCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateDueDate();
            }
        });
    }

    private void calculateDueDate() {

        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR); // get current year
        final int mMonth = c.get(Calendar.MONTH); // get current month
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(ActivityPregnancyCalculator.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                String suffix = null;

                if (date == 1 || date == 21 || date == 31){

                    suffix = "st";

                }else if (date == 2 || date == 22){

                    suffix = "nd";
                }else if (date == 3 || date == 23){

                    suffix = "rd";
                }else {

                    suffix = "th";
                }

                binding.selectedDate2.setText( date + suffix + " " + MONTHS[month] + " " + year);
                binding.currentDate.setText(mDay + suffix + " " + MONTHS[mMonth] + " " + mYear);

                selectedDate = date + "-" + (month + 1) + "-" + year;
                currentDate = mDay + "-" + mMonth + "-" + mYear;

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    c.setTime(Objects.requireNonNull(sdf.parse(selectedDate)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                c.add(Calendar.DATE, 282); /*GET DUE DATE*/

                SimpleDateFormat sdf2 = new SimpleDateFormat("EEE, dd MMMM yyyy", Locale.US);

                String finalDueDate = sdf2.format(c.getTime());

                binding.dueDate.setText("Your Estimated due date is: " + finalDueDate);

                Calendar start = Calendar.getInstance();
                try {
                    start.setTime(Objects.requireNonNull(sdf2.parse(selectedDate)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar end = Calendar.getInstance();
                try {
                    end.setTime(Objects.requireNonNull(sdf2.parse(currentDate)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String CurrentDate= "15/1/2020";
                String FinalDate= "02/05/2020";

                Date date1= new Date();
                Date date2 = new Date();

                SimpleDateFormat dates = new SimpleDateFormat("dd/MM/yyyy");


                try {
                    date1 = dates.parse(CurrentDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    date2 = dates.parse(FinalDate);
                } catch (ParseException e) {

                    e.printStackTrace();
                }

                long difference = Math.abs(Objects.requireNonNull(date2).getTime() - Objects.requireNonNull(date1).getTime());
                long differenceWeeks = difference / (7 * 24 * 60 * 60 * 1000);
//                long differenceDays = differenceWeeks / (24 * 60 * 60 * 1000);
                long weeksPregnant = Math.abs(40 - differenceWeeks) ;
                String weeksDifference = Long.toString(weeksPregnant);
//                String daysDifference = Long.toString(differenceDays);

                binding.weeksPregnant.setText("Congratulations, you are " + weeksDifference + " weeks pregnant!");

            }
        }, mYear, mMonth, mDay);

        datePickerDialog.show();

    }

    private void selectDueDate() {

        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR); // get current year
        final int mMonth = c.get(Calendar.MONTH); // get current month
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

       datePickerDialog = new DatePickerDialog(ActivityPregnancyCalculator.this, new DatePickerDialog.OnDateSetListener() {
           @Override
           public void onDateSet(DatePicker datePicker, int year, int month, int date) {
               String suffix = null;

               if (date == 1 || date == 21 || date == 31){

                   suffix = "st";

               }else if (date == 2 || date == 22){

                   suffix = "nd";
               }else if (date == 3 || date == 23){

                   suffix = "rd";
               }else {

                   suffix = "th";
               }

//               binding.selectedDate.setText(date + "/" + (month + 1) + "/" + year);
               binding.selectedDate.setText( date + suffix + " " + MONTHS[month] + " " + year);
               binding.currentDate.setText(mDay + suffix + " " + MONTHS[mMonth] + " " + mYear);
           }
       }, mYear, mMonth, mDay);

       datePickerDialog.show();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

}
