package com.example.loginschoolpool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class testMatchMate extends AppCompatActivity {

    private TextView setStartLocationTextView;
    private TextView setTargetLocationTextView;
    private TextView setDateTextView;
    private TextView setTimeTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_match_mate);
        setStartLocationTextView = (TextView) findViewById(R.id.textView_start_location);
        setTargetLocationTextView = (TextView) findViewById(R.id.textView_target_location);
        setDateTextView = (TextView) findViewById(R.id.textView_date);
        setTimeTextView = (TextView) findViewById(R.id.textView_time);

        setInformationFromRouteActivaity(setStartLocationTextView, setTargetLocationTextView,setDateTextView,setTimeTextView);
    }

    private void setInformationFromRouteActivaity(TextView setStartLocationTextView, TextView setTargetLocationTextView,
                                                  TextView setDateTextView, TextView setTimeTextView) {
        setStartLocationTextView.setText(getIntent().getStringExtra("SET_HOME_LOCATION"));
        setTargetLocationTextView.setText(getIntent().getStringExtra("SET_SCHOOL_LOCATION"));
        setDateTextView.setText(getIntent().getStringExtra("SET_DATE"));
        setTimeTextView.setText(getIntent().getStringExtra("SET_TIME"));

    }
}
