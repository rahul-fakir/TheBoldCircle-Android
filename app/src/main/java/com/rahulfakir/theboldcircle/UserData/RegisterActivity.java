package com.rahulfakir.theboldcircle.UserData;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rahulfakir.theboldcircle.DataManipulation;
import com.rahulfakir.theboldcircle.R;

public class RegisterActivity extends AppCompatActivity {
    private EditText etFirstName, etSurname, etEmailAddress, etConfirmedEmailAddress, etMobileNumber,
            etPassword, etConfirmedPassword;
    private Button btnRegister;
    private ProgressBar pbRegistrationStatus;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initializing gui component values
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etSurname = (EditText) findViewById(R.id.etSurname);
        etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
        etEmailAddress = (EditText) findViewById(R.id.etEmail);
        etConfirmedEmailAddress = (EditText) findViewById(R.id.etConfirmEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmedPassword = (EditText) findViewById(R.id.etConfirmPassword);

        //initializing gui components
        btnRegister = (Button) findViewById(R.id.btnCreateProfile);
        pbRegistrationStatus = (ProgressBar) findViewById(R.id.pbRegistrationStatus);

        //setting onclick actions
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DataManipulation validateData = new DataManipulation();
                if (!validateData.validateForStringNotEmpty("First name", etFirstName.getText().toString())) {
                    Toast.makeText(getApplicationContext(), validateData.getStatusMessage(),
                            Toast.LENGTH_LONG).show();
                } else if (!validateData.validateForStringNotEmpty("Surname", etSurname.getText().toString())) {
                    Toast.makeText(getApplicationContext(), validateData.getStatusMessage(),
                            Toast.LENGTH_LONG).show();
                } else if (!validateData.validateForPhoneNumber(etMobileNumber.getText().toString())) {
                    Toast.makeText(getApplicationContext(), validateData.getStatusMessage(),
                            Toast.LENGTH_LONG).show();
                } else if (!validateData.validateForEmailAddress(etEmailAddress.getText().toString())) {
                    Toast.makeText(getApplicationContext(), validateData.getStatusMessage(),
                            Toast.LENGTH_LONG).show();
                } else if (!validateData.validateForPassword(etPassword.getText().toString())) {
                    Toast.makeText(getApplicationContext(), validateData.getStatusMessage(),
                            Toast.LENGTH_LONG).show();
                } else if (!validateData.validateForEquality("Email", etEmailAddress.getText().toString(),
                        etConfirmedEmailAddress.getText().toString())) {
                    Toast.makeText(getApplicationContext(), validateData.getStatusMessage(),
                            Toast.LENGTH_LONG).show();
                } else {
                    if (!validateData.validateForEquality("Password", etPassword.getText().toString(), etConfirmedPassword.getText().toString())) {
                        Toast.makeText(getApplicationContext(), validateData.getStatusMessage(),
                                Toast.LENGTH_LONG).show();
                    } else {
                        pbRegistrationStatus.setVisibility(View.VISIBLE);
                        mAuth = FirebaseAuth.getInstance();
                        mAuth.createUserWithEmailAndPassword(etEmailAddress.getText().toString(), etConfirmedPassword.getText().toString())
                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, task.getException().toString(),
                                                    Toast.LENGTH_SHORT).show();
                                            pbRegistrationStatus.setVisibility(View.INVISIBLE);
                                        } else {
                                            Toast.makeText(RegisterActivity.this, "Your account has been created. Please log in",
                                                    Toast.LENGTH_SHORT).show();
                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference registerRef = database.getReference("userData").child(validateData.encodeEmailToUsername(etEmailAddress.getText().toString())).child("userProfile");
                                            registerRef.child("firstName").setValue(etFirstName.getText().toString().toLowerCase());
                                            registerRef.child("surname").setValue(etSurname.getText().toString().toLowerCase());
                                            registerRef.child("mobileNumber").setValue(etMobileNumber.getText().toString().toLowerCase());
                                            finish();

                                        }
                                    }
                                });
                    }
                }
            }
        });

    }
}
