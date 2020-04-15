package com.example.loginschoolpool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;


/**
 * RouteActivity class : The user need to decide which route he will choose to do :
 *                      1. school --->  home
 *                      1. home   --->  school
 */
public class RouteActivity extends AppCompatActivity implements View.OnClickListener , DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private TextView setHomeTextView;
    private TextView setSchoolTextView;
    private  TextView setDateTextView;
    private  TextView setTimeTextView;
    private Button finishButton;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        setHomeTextView = (TextView) findViewById(R.id.button_set_home);
        setSchoolTextView = (TextView) findViewById(R.id.button_set_school);
        setDateTextView = (TextView)findViewById(R.id.textView_date);
        setTimeTextView = (TextView)findViewById(R.id.textView_time);
        finishButton = (Button)findViewById(R.id.Button_finish);

        SharedPreferences sharedPref = getSharedPreferences("BARAK", Context.MODE_PRIVATE);

        SetStartAndTargetLocationsIfExist(sharedPref);

        setHomeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(RouteActivity.this, MapsActivity.class);
                intent.putExtra("context", "START_LOCATION");
                startActivity(intent);
            }
        });

        setSchoolTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(RouteActivity.this,MapsActivity.class);
                intent.putExtra("context", "TARGET_LOCATION");
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


        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation()){
                    Intent intent = new Intent(getApplicationContext(),testMatchMate.class);
                    intent.putExtra("SET_HOME_LOCATION", setHomeTextView.getText().toString());
                    intent.putExtra("SET_SCHOOL_LOCATION",setSchoolTextView.getText().toString());
                    intent.putExtra("SET_DATE",setDateTextView.getText().toString());
                    intent.putExtra("SET_TIME",setTimeTextView.getText().toString());

                    startActivity(intent);

                }
                else return;
            }

            private boolean validation() {
                //Check if all tabs is full;
                if(!setTimeTextView.getText().toString().matches("") &&
                   !setTimeTextView.getText().toString().matches("") &&
                   !setHomeTextView.getText().toString().matches("") &&
                   !setSchoolTextView.getText().toString().matches("")){
                    return true;
                }

                else{
                    finishButton.setError("Fill all the Fields");
                    return  false;
                }
            }
        });

    }

    private void SetStartAndTargetLocationsIfExist(SharedPreferences sharedPref) {
        String startLocation = sharedPref.getString("START_LOCATION", "");
        setHomeTextView.setText(startLocation);
        String targetLocation = sharedPref.getString("TARGET_LOCATION", "");
        setSchoolTextView.setText(targetLocation);
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
