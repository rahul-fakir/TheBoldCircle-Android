package com.rahulfakir.theboldcircle.UserData;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rahulfakir.theboldcircle.DataManipulation;
import com.rahulfakir.theboldcircle.R;

import java.util.ArrayList;

public class UserProfile extends AppCompatActivity {
    private String username;
    private DataManipulation encoder;
    private TextView tvStoreID, tvStoreName, tvStoreAddress;
    private Spinner spnStores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            System.out.println("VALID");
        //    System.out.println(user.getEmail().toString());
        } else {
            System.out.println("INVALID");
            // No user is signed in
        }

        final SharedPreferences sharedpreferences = getSharedPreferences("the_bold_circle", Context.MODE_PRIVATE);
        username = sharedpreferences.getString("current_user", "");
        System.out.println("is " + username);
//        System.out.println(user.getEmail());
        encoder = new DataManipulation();
        username = encoder.encodeEmailToUsername(username);

        final TextView tvEmail = (TextView) findViewById(R.id.tvEmail);
        final EditText etFirstName = (EditText) findViewById(R.id.etFirstName);
        final EditText etSurname = (EditText) findViewById(R.id.etSurname);
        final EditText etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        final EditText etAddressOne = (EditText) findViewById(R.id.etAddressOne);
        final EditText etAddressTwo = (EditText) findViewById(R.id.etAddressTwo);
        final EditText etCity = (EditText) findViewById(R.id.etCity);
        final EditText etProvince = (EditText) findViewById(R.id.etProvince);
        final EditText etZipCode = (EditText) findViewById(R.id.etZipCode);
        final EditText etCountry = (EditText) findViewById(R.id.etCountry);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        spnStores = (Spinner) findViewById(R.id.spnStores);



        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("userData").child(username).child("userProfile");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getRef().toString());
               System.out.println(dataSnapshot.child("firstName").getValue().toString());


                tvEmail.setText(sharedpreferences.getString("current_user", ""));
                etFirstName.setText(dataSnapshot.child("firstName").getValue().toString());
                etSurname.setText(dataSnapshot.child("surname").getValue().toString());
                etPhoneNumber.setText(dataSnapshot.child("mobileNumber").getValue().toString());
                DataSnapshot purchasingOptions = dataSnapshot.child("purchasingOptions").child("shippingAddress");
                etAddressOne.setText(purchasingOptions.child("addressOne").getValue().toString());
                etAddressTwo.setText(purchasingOptions.child("addressTwo").getValue().toString());
                etCity.setText(purchasingOptions.child("city").getValue().toString());
                etProvince.setText(purchasingOptions.child("province").getValue().toString());
                etZipCode.setText(purchasingOptions.child("zipCode").getValue().toString());
                etCountry.setText(purchasingOptions.child("country").getValue().toString());
                final String defaultStore = dataSnapshot.child("purchasingOptions").child("defaultStore").getValue().toString();

                FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                DatabaseReference storesRef = mDatabase.getReference("storeData/storeNames" );

                final ArrayList<String> storeNames = new ArrayList<String>();
                storesRef.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshotStores : dataSnapshot.getChildren()) {
                                    storeNames.add(postSnapshotStores.getKey());


                                }
                                final ArrayAdapter<String> storesAdapter =new ArrayAdapter<String>(UserProfile.this,android.R.layout.simple_spinner_item, storeNames);
                                storesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spnStores.setAdapter(storesAdapter);

                                FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference defaultStoresRef = mDatabase.getReference("storeData/storeDetails").child(defaultStore).child("name");

                                defaultStoresRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {

                                            spnStores.setSelection(storesAdapter.getPosition(dataSnapshot.getValue().toString()));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
            }



            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        Button btnUpdateProfile = (Button) findViewById(R.id.btnUpdateProfile);
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataManipulation validator = new DataManipulation();
                if (!validator.validateForStringNotEmpty("First Name", etFirstName.getText().toString())){
                    Toast.makeText(getApplicationContext(), validator.getStatusMessage(),
                            Toast.LENGTH_LONG).show();
                } else
                if (!validator.validateForStringNotEmpty("Surname", etSurname.getText().toString())){
                    Toast.makeText(getApplicationContext(), validator.getStatusMessage(),
                            Toast.LENGTH_LONG).show();
                } else
                if (!validator.validateForPhoneNumber(etPhoneNumber.getText().toString())){
                    Toast.makeText(getApplicationContext(), validator.getStatusMessage(),
                            Toast.LENGTH_LONG).show();
                } else
                if (!validator.validateForStringNotEmpty("Address Line 1", etAddressOne.getText().toString())){
                    Toast.makeText(getApplicationContext(), validator.getStatusMessage(),
                            Toast.LENGTH_LONG).show();
                } else
                if (!validator.validateForStringNotEmpty("City", etCity.getText().toString())){
                    Toast.makeText(getApplicationContext(), validator.getStatusMessage(),
                            Toast.LENGTH_LONG).show();
                } else
                if (!validator.validateForStringNotEmpty("Zip Code", etZipCode.getText().toString())){
                    Toast.makeText(getApplicationContext(), validator.getStatusMessage(),
                            Toast.LENGTH_LONG).show();
                } else
                if (!validator.validateForStringNotEmpty("Country", etCountry.getText().toString())){
                    Toast.makeText(getApplicationContext(), validator.getStatusMessage(),
                            Toast.LENGTH_LONG).show();
                } else
                if (!validator.validateForPassword(etPassword.getText().toString())){
                    Toast.makeText(getApplicationContext(), validator.getStatusMessage(),
                            Toast.LENGTH_LONG).show();
                } else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(sharedpreferences.getString("current_user", ""), etPassword.getText().toString());

// Prompt the user to re-provide their sign-in credentials
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    final DatabaseReference userRef = database.getReference("userData").child(username).child("userProfile");
                                    userRef.child("firstName").setValue(etFirstName.getText().toString());
                                    userRef.child("surname").setValue(etSurname.getText().toString());
                                    userRef.child("mobileNumber").setValue(etPhoneNumber.getText().toString());
                                    userRef.child("purchasingOptions").child("shippingAddress").child("addressOne").setValue(etAddressOne.getText().toString());
                                    userRef.child("purchasingOptions").child("shippingAddress").child("addressTwo").setValue(etAddressTwo.getText().toString());
                                    userRef.child("purchasingOptions").child("shippingAddress").child("city").setValue(etCity.getText().toString());
                                    userRef.child("purchasingOptions").child("shippingAddress").child("province").setValue(etProvince.getText().toString());
                                    userRef.child("purchasingOptions").child("shippingAddress").child("zipCode").setValue(etZipCode.getText().toString());
                                    userRef.child("purchasingOptions").child("shippingAddress").child("country").setValue(etCountry.getText().toString());

                                    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                                    DatabaseReference defaultStoresRef = mDatabase.getReference("storeData/storeNames").child(spnStores.getSelectedItem().toString());

                                    defaultStoresRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {

                                               userRef.child("purchasingOptions").child("defaultStore").setValue(dataSnapshot.getValue().toString());
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    Toast.makeText(getApplicationContext(), "Profile updated",
                                            Toast.LENGTH_LONG).show();
                                    etPassword.setText("");
                                }
                            });
                }



            }
        });
    }

}
