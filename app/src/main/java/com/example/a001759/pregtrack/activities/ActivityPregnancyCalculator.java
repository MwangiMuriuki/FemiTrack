package com.example.a001759.pregtrack.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import net.danlew.android.joda.JodaTimeAndroid;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class ActivityPregnancyCalculator extends AppCompatActivity {

    AlertDialog saveDialog;

    DatePickerDialog datePickerDialog;
    Calendar calendar;
    int day, month, year;

    String selectedDate;
    String currentDate;
    String finalDueDate;
    String weeksDifference;
    String daysDifference;
    long weeksPregnant;

    String selectedDueDate, weekDiff, period;
    long week_difference;
    long days_difference;

    public static final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};


    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser;

    ActivityPregnancyCalculatorBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pregnancy_calculator);
        binding.pregnancyCalcToolBar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(binding.pregnancyCalcToolBar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Pregnancy Calculator");

        saveDialog = new SpotsDialog(this, R.style.savingDateAlert);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

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

        binding.saveSelectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDialog.setCancelable(false);
                saveDialog.show();
                saveSelectedData();

            }
        });

        binding.saveCalculatedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveDialog.setCancelable(false);
                saveDialog.show();
                saveCalculated();
            }
        });
    }

    private void calculateDueDate() {

        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR); // get current year
        final int mMonth = c.get(Calendar.MONTH); // get current month
        final int mDay = c.get(Calendar.DAY_OF_MONTH);//get current date

        datePickerDialog = new DatePickerDialog(ActivityPregnancyCalculator.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.US);
                SimpleDateFormat dates = new SimpleDateFormat("dd/MM/yyyy");

                Date date1= new Date();
                Date date2 = new Date();

                selectedDate = date + "-" + (month + 1) + "-" + year;
                currentDate = mDay + "-" + mMonth + "-" + mYear;

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    c.setTime(Objects.requireNonNull(sdf.parse(selectedDate)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String pickedDate = sdf2.format(c.getTime());

                c.add(Calendar.DATE, 282); /*GET DUE DATE*/

                finalDueDate = sdf2.format(c.getTime());/*CONVERT DUE DATE TO STRING*/

                /*CONVERT SELECTED DATE STRING INTO DATE FORMAT*/
                Calendar selected = Calendar.getInstance();
                try {
                    selected.setTime(Objects.requireNonNull(sdf2.parse(selectedDate)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                /*CONVERT CURRENT DATE STRING INTO DATE FORMAT*/
                Calendar currentDD = Calendar.getInstance();
                try {
                    currentDD.setTime(Objects.requireNonNull(sdf2.parse(currentDate)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String testCurrent = dates.format(currentDD.getTime());
                String testFinal = dates.format(c.getTime());

                try {
                    date1 = dates.parse(testCurrent);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    date2 = dates.parse(testFinal);
                } catch (ParseException e) {

                    e.printStackTrace();
                }

                long difference = Math.abs(Objects.requireNonNull(date2).getTime() - Objects.requireNonNull(date1).getTime());
                long differenceWeeks = difference / (7 * 24 * 60 * 60 * 1000);
                long differenceDays =  difference / (24 * 60 * 60 * 1000);
                long daysPregnant = Math.abs(282 - differenceDays);
                long arith = daysPregnant % 7;
                weeksPregnant = Math.abs(40 - differenceWeeks) ;

                weeksDifference = Long.toString(weeksPregnant);
                daysDifference = Long.toString(arith);

//                String pickedDate = date + " " + MONTHS[month] + " " + year;

                binding.selectedDate2.setText(pickedDate);
                binding.dueDate.setText(finalDueDate);
                binding.weeksPregnant.setText("Congratulations, you are on week " + weeksDifference + " and day " + arith + " of your pregnancy!");

                binding.layoutData2.setVisibility(View.VISIBLE);


            }
        }, mYear, mMonth, mDay);

        datePickerDialog.show();

    }

    private void saveCalculated() {

        String userID = firebaseUser.getUid();

        DocumentReference documentReference = firebaseFirestore.collection("Users").document(userID);
        documentReference.update("weeks_pregnant", weeksPregnant , "due_date", finalDueDate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                saveDialog.cancel();

                Toast.makeText(ActivityPregnancyCalculator.this, "Done. See more on what to expect this month...", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void selectDueDate() {

        final Calendar cal = Calendar.getInstance();
        final int mYear = cal.get(Calendar.YEAR); // get current year
        final int mMonth = cal.get(Calendar.MONTH); // get current month
        final int mDay = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(ActivityPregnancyCalculator.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.US);
                SimpleDateFormat dates = new SimpleDateFormat("dd/MM/yyyy");

                Date sd= new Date(); /*selected date*/
                Date cd = new Date(); /*current date*/

                String newSelectedDate = date + "-" + (month + 1) + "-" + year;

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    cal.setTime(Objects.requireNonNull(sdf.parse(newSelectedDate)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String selectedDueDate = sdf2.format(cal.getTime());

                cal.add(Calendar.DATE, -282); /*GET FIRST DAY OF LAST PERIOD*/

                period = sdf2.format(cal.getTime());/*CONVERT FIRST DAY OF LAST PERIOD TO STRING*/
                currentDate = mDay + "-" + mMonth + "-" + mYear;

                Calendar periodDD = Calendar.getInstance();
                try {
                    periodDD.setTime(Objects.requireNonNull(sdf2.parse(period)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar currentDD = Calendar.getInstance();
                try {
                    currentDD.setTime(Objects.requireNonNull(sdf2.parse(currentDate)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String stringCurrentDate = dates.format(currentDD.getTime());
                String stringPeriodDate = dates.format(periodDD.getTime());

                try {
                    sd = dates.parse(stringCurrentDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    cd = dates.parse(stringPeriodDate);
                } catch (ParseException e) {

                    e.printStackTrace();
                }

                long difference2 = Math.abs(Objects.requireNonNull(sd).getTime() - Objects.requireNonNull(cd).getTime());
                week_difference =  difference2 / (7 * 24 * 60 * 60 * 1000);
                days_difference = difference2 / (24 * 60 * 60 * 1000);
                long newDiff = 40 - week_difference;
                weekDiff = Long.toString(week_difference);
                long daysPregnant = Math.abs(282 - days_difference);
                long dPreg = daysPregnant % 7;
                String arith2 = Long.toString(dPreg);

//               String suffix = null;
//
//               if (date == 1 || date == 21 || date == 31){
//
//                   suffix = "st";
//
//               }else if (date == 2 || date == 22){
//
//                   suffix = "nd";
//               }else if (date == 3 || date == 23){
//
//                   suffix = "rd";
//               }else {
//
//                   suffix = "th";
//               }

//               binding.selectedDate.setText(date + "/" + (month + 1) + "/" + year);
                binding.selectedDate.setText(selectedDueDate);
                binding.weeksPregnantSelected.setText("Congratulations, you are on week " +  weekDiff + " and day " + arith2 + " of your pregnancy!");

                binding.layoutData1.setVisibility(View.VISIBLE);
            }
        }, mYear, mMonth, mDay);

        datePickerDialog.show();

    }

    private void saveSelectedData() {

        String userID = firebaseUser.getUid();

        DocumentReference documentReference = firebaseFirestore.collection("Users").document(userID);
        documentReference.update("weeks_pregnant", week_difference , "due_date", selectedDueDate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                saveDialog.cancel();

                Toast.makeText(ActivityPregnancyCalculator.this, "Done. See more on what to expect this month...", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
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
