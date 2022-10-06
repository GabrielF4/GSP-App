package com.example.notespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText personNameEditText, emailEditText, passwordEditText, confirmPasswordEditText, mobileNumberEditText, dateOfBirthEditText, homeAdressEditText;
    private String personName, email, mobileNumber, dateOfBirth, homeAdress, password;
    private Button createAccountBtn;
    private ProgressBar progressBar;
    private TextView loginBtnTextView;

    private FirebaseDatabase database;
    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;
    private User user;
    private static final String USERS = "user";
    private String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        /* Hämtar från "skapa konton" fälten */
        personNameEditText = findViewById(R.id.name_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
        mobileNumberEditText = findViewById(R.id.mobile_number_edit_text);
        dateOfBirthEditText = findViewById(R.id.date_of_birth_edit_text);
        homeAdressEditText = findViewById(R.id.adress_edit_text);

        createAccountBtn = findViewById(R.id.create_account_btn);
        progressBar = findViewById(R.id.progress_bar);
        loginBtnTextView = findViewById(R.id.login_text_view_btn);
        database = FirebaseDatabase.getInstance();
        mDataBase = database.getReference(USERS);
        mAuth = FirebaseAuth.getInstance();

        /* Adding listeners to buttons. */
        createAccountBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Insert data into firebase database
                if(emailEditText.getText().toString() != null && passwordEditText.getText().toString() != null) {
                    personName = personNameEditText.getText().toString();
                    email = emailEditText.getText().toString();
                    mobileNumber = mobileNumberEditText.getText().toString();
                    dateOfBirth = dateOfBirthEditText.getText().toString();
                    homeAdress = homeAdressEditText.getText().toString();
                    password = passwordEditText.getText().toString();
                    user = new User(personName, email, homeAdress, mobileNumber, dateOfBirth);
                    registerUser();
                }
            }
        });

        loginBtnTextView.setOnClickListener(v->finish());
    }


    public void registerUser() {
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
                            Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * adding user information to database and redirect to login screen
     * @param currentUser
     */
    public void updateUI(FirebaseUser currentUser) {
        String keyid = mAuth.getCurrentUser().getUid();
        mDataBase.child(keyid).setValue(user); // Adding user info to database
        Intent loginIntent = new Intent(this, MainActivity.class);
        startActivity(loginIntent);
    }

    /*private void createAccount() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        boolean isValidated = validateData(email,password,confirmPassword);
        if(!isValidated) {
            return;
        }
        createAccountInFirebase(email,password);
    }
    */

   /* void createAccountInFirebase(String email, String password){
        changeInProgress(true);
        // FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseAuth.getInstance();
        mDataBase = database.getReference(USER);
        mAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccountActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            User user = new User(personName, emailEditText, mobileNumber, dateOfBirth, homeAdress);
                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                Toast.makeText(CreateAccountActivity.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                                changeInProgress(true);
                                                startActivity(new Intent(CreateAccountActivity.this, MainActivity.class));
                                            } else {
                                                Toast.makeText(CreateAccountActivity.this, "Failed to register user!", Toast.LENGTH_LONG).show();
                                                changeInProgress(false);
                                            }
                                        }
                                    });

                            // Creating account is done
                            Utility.showToast(CreateAccountActivity.this, "Successfully create account, Check email to verify");
                            changeInProgress(false);
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                        }else {
                            //failure
                            Utility.showToast(CreateAccountActivity.this, task.getException().getLocalizedMessage());
                        }
                    }
                }
        );
    } */


   /* void changeInProgress(boolean inProgress){
        if(!inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String password, String confirmPassword) {
        //Validate the data from the user

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email is invalid");
            return false;
        }
        if(password.length()<6) {
            passwordEditText.setError("Password too short");
            return false;
        }
        if(!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("The passwords do not match");
            return false;
        }
        return true;
    } */
}