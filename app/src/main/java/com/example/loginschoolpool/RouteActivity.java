package com.example.loginschoolpool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.ICUUncheckedIOException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * RouteActivity class : The user need to decide which route he will choose to do :
 *                      1. school --->  home
 *                      1. home   --->  school
 */
public class RouteActivity extends AppCompatActivity implements View.OnClickListener , DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private Button setHomeButton;
    private Button setSchoolButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        setHomeButton = (Button) findViewById(R.id.button_set_home);
        setSchoolButton = (Button) findViewById(R.id.button_set_school);

        setHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(RouteActivity.this,MapsActivity.class);
                startActivity(intent);
            }
        });

        Button buttonDatePicker = (Button)findViewById(R.id.button_date_picker);
        buttonDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        Button buttonTimePicker = (Button)findViewById(R.id.button_time_picker);
        buttonTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"time picker");
            }
        });

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDateString  = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        TextView textView = (TextView)findViewById(R.id.textView_date);
        textView.setText(currentDateString);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView textViewTime = (TextView)findViewById(R.id.textView_time);
        textViewTime.setText( hourOfDay + ":" + minute);
    }
}
