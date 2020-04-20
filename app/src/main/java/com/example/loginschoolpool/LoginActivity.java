package com.example.loginschoolpool;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.regex.Pattern;

/**
 * MainActivity class - its Like Login class
 *                      The user credentials are typically some form of "username" and a matching "password"
 *
 *                      NOTE: There is validation of Email & Password,
 *                            but the password & Email its hide for convenience
 *
 */

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
//import com.google.firebase.quickstart.auth.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "EmailPassword";
    private EditText mEmailField;
    private EditText mPasswordField;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    @VisibleForTesting
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
    public void onStop() {
        super.onStop();
        hideProgressBar();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setProgressBar(R.id.progressBar);

        // Views
        // mStatusTextView = findViewById(R.id.status);
        //  mDetailTextView = findViewById(R.id.detail);
        mEmailField = findViewById(R.id.edit_text_username);
        mPasswordField = findViewById(R.id.edit_text_password);

        // Buttons
        findViewById(R.id.button_login_now).setOnClickListener(this);
        // findViewById(R.id.emailCreateAccountButton).setOnClickListener(this);
        // findViewById(R.id.signOutButton).setOnClickListener(this);
        // findViewById(R.id.verifyEmailButton).setOnClickListener(this);
        // findViewById(R.id.reloadButton).setOnClickListener(this);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressBar();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressBar();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressBar();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            startActivity(new Intent(LoginActivity.this,AppActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            // [START_EXCLUDE]
                            //checkForMultiFactorFailure(task.getException());
                            // [END_EXCLUDE]
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            //   mStatusTextView.setText(R.string.auth_failed);
                        }
                        hideProgressBar();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

//    private void sendEmailVerification() {
//        // Disable button
//        findViewById(R.id.verifyEmailButton).setEnabled(false);
//
//        // Send verification email
//        // [START send_email_verification]
//        final FirebaseUser user = mAuth.getCurrentUser();
//        user.sendEmailVerification()
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        // [START_EXCLUDE]
//                        // Re-enable button
//                        findViewById(R.id.verifyEmailButton).setEnabled(true);
//
//                        if (task.isSuccessful()) {
//                            Toast.makeText(EmailPasswordActivity.this,
//                                    "Verification email sent to " + user.getEmail(),
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
//                            Log.e(TAG, "sendEmailVerification", task.getException());
//                            Toast.makeText(EmailPasswordActivity.this,
//                                    "Failed to send verification email.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                        // [END_EXCLUDE]
//                    }
//                });
//        // [END send_email_verification]
//    }

    private void reload() {
        mAuth.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    updateUI(mAuth.getCurrentUser());
                    Toast.makeText(LoginActivity.this,
                            "Reload successful!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "reload", task.getException());
                    Toast.makeText(LoginActivity.this,
                            "Failed to reload user.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressBar();
        if (user != null) {
//            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
//                    user.getEmail(), user.isEmailVerified()));
//            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            //findViewById(R.id.emailPasswordButtons).setVisibility(View.GONE);
            //findViewById(R.id.emailPasswordFields).setVisibility(View.GONE);
            findViewById(R.id.button_login_now).setVisibility(View.VISIBLE);

//            if (user.isEmailVerified()) {
//                findViewById(R.id.verifyEmailButton).setVisibility(View.GONE);
//            } else {
//                findViewById(R.id.verifyEmailButton).setVisibility(View.VISIBLE);
//            }
//        } else {
//            mStatusTextView.setText(R.string.signed_out);
//            mDetailTextView.setText(null);
//
//            findViewById(R.id.emailPasswordButtons).setVisibility(View.VISIBLE);
//            findViewById(R.id.emailPasswordFields).setVisibility(View.VISIBLE);
//            findViewById(R.id.signedInButtons).setVisibility(View.GONE);
//        }
        }

//    private void checkForMultiFactorFailure(Exception e) {
//        // Multi-factor authentication with SMS is currently only available for
//        // Google Cloud Identity Platform projects. For more information:
//        // https://cloud.google.com/identity-platform/docs/android/mfa
//        if (e instanceof FirebaseAuthMultiFactorException) {
//            Log.w(TAG, "multiFactorFailure", e);
//            Intent intent = new Intent();
//            MultiFactorResolver resolver = ((FirebaseAuthMultiFactorException) e).getResolver();
//            intent.putExtra("EXTRA_MFA_RESOLVER", resolver);
//            setResult(MultiFactorActivity.RESULT_NEEDS_MFA_SIGN_IN, intent);
//            finish();
//        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        Intent intent = null;
        if (i == R.id.button_login_now) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
            return;
        }
        else if(i == R.id.textView_create_account){
            intent = new Intent(this,SignUpActivity.class);
        }

        if(null != intent) startActivity(intent);
    }
}


//    private static final String TAG = "MainActivity";
//
//    private static int SPLASH_TIME_OUT = 4000;
//    private static final Pattern PASSWORD_PATTERN =
//            Pattern.compile("^" +
//                    //"(?=.*[0-9])" +         //at least 1 digit
//                    //"(?=.*[a-z])" +         //at least 1 lower case letter
//                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
//                    "(?=.*[a-zA-Z])" +      //any letter
//                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
//                    "(?=\\S+$)" +           //no white spaces
//                    ".{4,}" +               //at least 4 characters
//                    "$");
//
//    private EditText textInputUserName;
//    private EditText textInputPassword;
//    private Button buttonLogin;
//    private FirebaseAuth firebaseAuth;
//    private FirebaseAuth.AuthStateListener authStateListener;
//    private TextView buttonCreateAccount;
//    private FirebaseUser user;
//    private ProgressDialog progressDialog;
//    private boolean emailIsExists = false;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//
//        textInputUserName = (EditText) findViewById(R.id.edit_text_username);
//        textInputPassword = (EditText) findViewById(R.id.edit_text_password);
//        buttonLogin = (Button) findViewById(R.id.button_login_now);
//        buttonCreateAccount = (TextView) findViewById(R.id.textView_create_account);
//        errorView = findViewById(R.id.signInErrorView);
//
//        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this,SignInActivity.class));
//            }
//        });
//        firebaseAuth = FirebaseAuth.getInstance();
//
//
//        buttonLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            confirmInput(v);
//            final String email = textInputUserName.getText().toString();
//            final String password = textInputPassword.getText().toString();
//
//            firebaseAuth.signInWithEmailAndPassword(email, password)
//                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                // Sign in success, update UI with the signed-in user's information
//                                Log.d(TAG, "signInWithEmail:success");
//
//                                FirebaseUser user = firebaseAuth.getCurrentUser();
//
//                                if (user != null) {
//                                    if (user.isEmailVerified()) {
//                                        System.out.println("Email Verified : " + user.isEmailVerified());
//                                        Intent HomeActivity = new Intent(LoginActivity.this, AppActivity.class);
//                                        setResult(RESULT_OK, null);
//                                        startActivity(HomeActivity);
//                                        //LoginActivity.this.finish();
//                                    } else {
//
//                                        //sendVerifyMailAgainButton.setVisibility(View.VISIBLE);
//                                        //errorView.setText("Please Verify your EmailID and SignIn");
//
//                                    }
//                                }
//
//                            } else {
//                                // If sign in fails, display a message to the user.
//                                Log.w(TAG, "signInWithEmail:failure", task.getException());
//                                Toast.makeText(LoginActivity.this, "Authentication failed.",
//                                        Toast.LENGTH_SHORT).show();
//                                if (task.getException() != null) {
//                                   // errorView.setText(task.getException().getMessage());
//
//                                }
//
//                            }
//
//                        }
//                    });
//
//            }
//            });
//
//    }
//
//
//
//
//
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//    }
//
//    private void toastMessage(String message) {
//        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
//    }
//    private boolean validateUsername() {
////        String usernameInput = textInputUserName.getText().toString().trim();
////
////        if (usernameInput.isEmpty()) {
////            textInputUserName.setError(getResources().getString(R.string.EMPTY_USERNAME));
////            return false;
////        } else if (!Patterns.EMAIL_ADDRESS.matcher(usernameInput).matches()) {
////            textInputUserName.setError(getResources().getString(R.string.USERNAME_INVALID));
////            return false;
////        } else {
////            textInputUserName.setError(null);
////            return true;
////        }
//        return true;
//    }
//
//    private boolean validatePassword() {
//        String passwordInput = textInputPassword.getText().toString().trim();
//
//        if (passwordInput.isEmpty()) {
//            textInputPassword.setError(getResources().getString(R.string.EMPTY_PASSWORD));
//            return false;
//        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
//            textInputPassword.setError(getResources().getString(R.string.PASSWORD_INVALID));
//            return false;
//        } else {
//            textInputPassword.setError(null);
//            return true;
//        }
//    }
//
//    public void confirmInput(View v) {
//        if (!validateUsername() | !validatePassword()) {
//            return;
//        }
//
//        String input = null;
//        input += "Username: " + textInputUserName.getText().toString();
//        input += "\n";
//        input += "Password: " + textInputPassword.getText().toString();
//
//      //  Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
//        return;
//    }
//
//    private void openAppActivity() {
//        Intent intent = new Intent(this,AppActivity.class);
//        startActivity(intent);
//    }
//
//

