package com.example.loginschoolpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthMultiFactorException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.MultiFactorResolver;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText textInputUserName, textInputEmail, textInputPassword;
    private Button registerBtn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        textInputUserName = (EditText) findViewById(R.id.editText_full_name);
        textInputEmail = (EditText) findViewById(R.id.editText_email);
        textInputPassword = (EditText) findViewById(R.id.editText_password);
        registerBtn = (Button) findViewById(R.id.button_register);

        firebaseAuth = FirebaseAuth.getInstance();



    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        Log.d("myTag","1 op");
        if(i == R.id.button_register){
            createAccount(textInputEmail.getText().toString(),textInputPassword.getText().toString());
        }
    }

    private void createAccount(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("myTag","2 op");
                if(task.isSuccessful()){
                    //if succeeded to create account
                    Toast.makeText(SignUpActivity.this, "User Create", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    startActivity(new Intent(SignUpActivity.this,AppActivity.class));
                }
                else{
                    //Failed to create account
                    Toast.makeText(SignUpActivity.this, "User created failed", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}
