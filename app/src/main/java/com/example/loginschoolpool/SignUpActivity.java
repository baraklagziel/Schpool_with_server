package com.example.loginschoolpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText textInputUserName, textInputEmail, textInputPassword;
    private Button registerBtn;
    private FirebaseAuth firebaseAuth;
    private static final String regex = "^(.+)@(.+)$";
    public ProgressBar mProgressBar;

    public void setProgressBar(int resId) {
        mProgressBar = findViewById(resId);
    }

    public void showProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        textInputUserName = (EditText) findViewById(R.id.editText_full_name);
        textInputEmail = (EditText) findViewById(R.id.editText_email);
        textInputPassword = (EditText) findViewById(R.id.editText_password);
        registerBtn = (Button) findViewById(R.id.button_register);
        setProgressBar(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();
       // hideProgressBar();

    }

    @Override
    public void onClick(View v) {
        hideKeyboard(v);
        int i = v.getId();
        Log.d("myTag","1 op");
        if(i == R.id.button_register){
            if(validation())
                createAccount(textInputEmail.getText().toString(),textInputPassword.getText().toString());
            else return;
        }
    }

    private boolean validation() {
        String email = textInputEmail.getText().toString().trim();
        String password = textInputPassword.getText().toString().trim();

         if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
             textInputEmail.setError(getResources().getString(R.string.USERNAME_INVALID));
             return false;
         }
         if(password.length() < 6 ){
             textInputPassword.setError("Password must at least 6 characters");
             return false;
         }

         return true;
    }

    private void createAccount(String email, String password) {
        showProgressBar();
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

                hideProgressBar();


            }
        });

    }
}
