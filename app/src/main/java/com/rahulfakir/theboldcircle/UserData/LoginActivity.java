package com.rahulfakir.theboldcircle.UserData;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.rahulfakir.theboldcircle.DataManipulation;
import com.rahulfakir.theboldcircle.HomeScreenActivity;
import com.rahulfakir.theboldcircle.R;

public class LoginActivity extends AppCompatActivity {
    private TextView tvCreateProfile;
    private EditText etEmail, etPassword;
    private Button btnLogIn;
    private ProgressBar pbLoginStatus;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        mAuth.signOut();
        if (user != null) {
            // User is signed in

            SharedPreferences sharedpreferences = getSharedPreferences("the_bold_circle", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("current_user", user.getEmail());
            editor.apply();
System.out.println("USERNAME HAS BEEN SET");


            System.out.println("USER IS SIGNED IN");
            System.out.println(user.getEmail());
            Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
            startActivity(intent);
            finish();

        } else {
            // User is signed out
            //do nothing

            System.out.println("USER IS SIGNED OUT");

        }

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        pbLoginStatus = (ProgressBar) findViewById(R.id.pbLoginStatus);

        tvCreateProfile = (TextView) findViewById(R.id.tvCreateProfile);
        tvCreateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogIn = (Button) findViewById(R.id.btnLogin);
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DataManipulation validateData = new DataManipulation();
                if (!validateData.validateForEmailAddress(etEmail.getText().toString())) {
                    Toast.makeText(getApplicationContext(), validateData.getStatusMessage(),
                            Toast.LENGTH_LONG).show();
                } else if (!validateData.validateForPassword(etPassword.getText().toString())) {
                    Toast.makeText(getApplicationContext(), validateData.getStatusMessage(),
                            Toast.LENGTH_LONG).show();
                } else {
                    pbLoginStatus.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        pbLoginStatus.setVisibility(View.INVISIBLE);
                                        Toast.makeText(LoginActivity.this, task.getException().toString(),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        SharedPreferences sharedpreferences = getSharedPreferences("the_bold_circle", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString("current_user", etEmail.getText().toString());
                                        editor.apply();
                                        Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }

            }
        });


    }


}
