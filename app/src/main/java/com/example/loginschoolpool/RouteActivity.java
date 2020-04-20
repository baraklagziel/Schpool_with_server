package com.example.loginschoolpool;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
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
    private Intent intentToMatchDriversActivity;

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
        intentToMatchDriversActivity = new Intent(getApplicationContext(),MatchDrivers.class);
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

        ImageButton buttonDatePicker = (ImageButton)findViewById(R.id.button_date_picker);
        buttonDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        ImageButton buttonTimePicker = (ImageButton)findViewById(R.id.button_time_picker);
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

                    intentToMatchDriversActivity.putExtra("SET_HOME_LOCATION", setHomeTextView.getText().toString());
                    intentToMatchDriversActivity.putExtra("SET_SCHOOL_LOCATION",setSchoolTextView.getText().toString());
                    //intentToMatchDriversActivity.putExtra("SET_DATE",setDateTextView.getText().toString());
                   // intentToMatchDriversActivity.putExtra("SET_TIME",setTimeTextView.getText().toString());

                    startActivity(intentToMatchDriversActivity);

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
        String datePicker = sharedPref.getString("SET_DATE", "");
        setDateTextView.setText(datePicker);
        String timePicker = sharedPref.getString("SET_TIME", "");
        setTimeTextView.setText(timePicker);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        intentToMatchDriversActivity.putExtra("SET_YEAR", year);
        month++;
        intentToMatchDriversActivity.putExtra("SET_MONTH", month );
        intentToMatchDriversActivity.putExtra("SET_DAY", dayOfMonth);
        month--;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDateString  = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        SharedPreferences.Editor editor = getSharedPreferences("BARAK", MODE_PRIVATE).edit();
        editor.putString("SET_DATE",currentDateString);
        editor.commit();
        TextView textView = (TextView)findViewById(R.id.textView_date);
        textView.setText(currentDateString);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String AM_PM = (hourOfDay < 12) ? "AM" : "PM";
        intentToMatchDriversActivity.putExtra("SET_AM_PM" , AM_PM);
        intentToMatchDriversActivity.putExtra("SET_HOUR", view.getHour());
        intentToMatchDriversActivity.putExtra("SET_MINUTE", view.getMinute());
        SharedPreferences.Editor editor = getSharedPreferences("BARAK", MODE_PRIVATE).edit();
        editor.putString("SET_TIME",String.format("%02d:%02d", hourOfDay,minute));
        editor.commit();
        TextView textViewTime = (TextView)findViewById(R.id.textView_time);
        textViewTime.setText(String.format("%02d:%02d", hourOfDay,minute));
    }
}
