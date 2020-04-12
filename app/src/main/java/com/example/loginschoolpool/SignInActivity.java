package com.example.loginschoolpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
    private EditText textInputUserName, textInputEmail, textInputPassword;
    private Button registerBtn;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        textInputUserName = (EditText) findViewById(R.id.editText_full_name);
        textInputEmail = (EditText) findViewById(R.id.editText_email);
        textInputPassword =  (EditText) findViewById(R.id.editText_password);
        registerBtn = (Button)findViewById(R.id.button_register);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance(); //getting the current instance from database

        //check if account is already logged in

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = textInputEmail.getText().toString().trim();
                String password = textInputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    textInputEmail.setError("Email is Required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    textInputPassword.setError("Password is Required");
                    return;
                }

                if (password.length() <= 6) {
                    textInputPassword.setError("Password must be 6 characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //register user to firebase

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignInActivity.this, "User created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), AppActivity.class));
                        } else {
                            Toast.makeText(SignInActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });


    }
}
