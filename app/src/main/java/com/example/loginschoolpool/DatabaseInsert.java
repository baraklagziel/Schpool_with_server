package com.example.loginschoolpool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class DatabaseInsert extends AppCompatActivity {
    FirebaseDatabaseInsert firebaseDatabaseInsert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_insert);
        firebaseDatabaseInsert = new FirebaseDatabaseInsert();
        firebaseDatabaseInsert.InitInsert();
    }
}
