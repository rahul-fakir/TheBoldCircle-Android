package com.rahulfakir.theboldcircle.ProductData;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rahulfakir.theboldcircle.HomeScreenActivity;
import com.rahulfakir.theboldcircle.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {
    private List<CheckoutObject> productsToCheckoutList = new ArrayList<>();
    private List<CheckoutObject> servicesToCheckoutList = new ArrayList<>();
    private RecyclerView rvProducts, rvServices;
    private CheckoutListAdapter productsAdapter, servicesAdapter;
    public static ArrayList<String> storeIds = new ArrayList();
    public static ArrayList<String> storeNames = new ArrayList();
    public static int year, month, day;
    static final int DATE_DIALOG_ID = 999;
    private Button btnCompletePayment;
    public static int currentViewHolder = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);


        rvProducts = (RecyclerView) findViewById(R.id.recycler_view);
        rvServices = (RecyclerView) findViewById(R.id.recycler_view_services);

        productsAdapter = new CheckoutListAdapter(productsToCheckoutList, this);
        servicesAdapter = new CheckoutListAdapter(servicesToCheckoutList, this);
        RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvProducts.setLayoutManager(pLayoutManager);
        rvProducts.setItemAnimator(new DefaultItemAnimator());
        rvProducts.setAdapter(productsAdapter);

        RecyclerView.LayoutManager sLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvServices.setLayoutManager(sLayoutManager);
        rvServices.setItemAnimator(new DefaultItemAnimator());
        rvServices.setAdapter(servicesAdapter);
        prepareStores();
        prepareCheckoutProducts();


        setCurrentDateOnView();
        addListenerOnButton();

    }

    private void prepareCheckoutProducts() {
        productsAdapter.notifyDataSetChanged();

        for (Map.Entry<String, Integer> entry : HomeScreenActivity.cart.entrySet()) {
            final String key = entry.getKey();
            String basePath = key.substring(0, key.indexOf("~"));
            System.out.println(basePath);
            Object value = entry.getValue();
            System.out.println(value);

            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference productsRef = mDatabase.getReference(basePath);

            productsRef.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            int type = Integer.valueOf(dataSnapshot.child("type").getValue().toString());
                            String sku = dataSnapshot.getKey();
                            String name = dataSnapshot.child("name").getValue().toString();
                            int depth = Integer.valueOf(dataSnapshot.child("variantDepth").getValue().toString());
                            Double price = Double.valueOf(dataSnapshot.child("basePrice").getValue().toString());
                            String firstVariantLevelName = "";
                            String secondVariantLevelName = "";

                            String[] indices = (key.substring(key.indexOf("~") + 1, key.length())).split("/");

                            if (depth == 1) {
                                firstVariantLevelName = " : " + dataSnapshot.child("variants").child("firstVariantLevel").child(indices[0]).child("value").getValue().toString();
                                price = Double.valueOf(dataSnapshot.child("variants").child("firstVariantLevel").child(indices[0]).child("price").getValue().toString());
                            } else if (depth == 2) {
                                firstVariantLevelName = " : " + dataSnapshot.child("variants").child("firstVariantLevel").child(indices[0]).child("value").getValue().toString();
                                secondVariantLevelName = " - " + dataSnapshot.child("variants").child("firstVariantLevel").child(indices[0]).child("secondVariantLevel").child(indices[1]).child("name").getValue().toString();
                                price = Double.valueOf(dataSnapshot.child("variants").child("firstVariantLevel").child(indices[0]).child("secondVariantLevel").child(indices[1]).child("price").getValue().toString());

                            }

                            name = name + firstVariantLevelName + secondVariantLevelName;

                            CheckoutObject currentObject = new CheckoutObject(name, sku, price, type);
                            if (type == 0) {
                                productsToCheckoutList.add(currentObject);
                                productsAdapter.notifyDataSetChanged();
                            } else {
                                System.out.println(Integer.valueOf(dataSnapshot.child("skillRequired").getValue().toString()) + "");
                                currentObject.setSkillRequired(Integer.valueOf(dataSnapshot.child("skillRequired").getValue().toString()));
                                currentObject.setSessionsRequired(Integer.valueOf(dataSnapshot.child("sessionsRequired").getValue().toString()));
                                servicesToCheckoutList.add(currentObject);
                                servicesAdapter.notifyDataSetChanged();
                            }


                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });
            // ...
        }


    }

    public void prepareStores() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference storesRef = mDatabase.getReference("storeData/storeNames");
        storeNames.clear();
        storeIds.clear();


        storesRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshotStores : dataSnapshot.getChildren()) {
                            storeNames.add(postSnapshotStores.getKey());
                            storeIds.add(postSnapshotStores.getValue().toString());

                        }


                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
    }

    public void setCurrentDateOnView() {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        //initialize

    }

    public void addListenerOnButton() {
        btnCompletePayment = (Button) findViewById(R.id.btnCompletePayment);
        System.out.println("RUNNING");
        btnCompletePayment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("RUNNING");
                showDialog(DATE_DIALOG_ID);
            }

        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener,
                        year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            try {
                servicesAdapter.dateSet(currentViewHolder, year, month, day);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };


}
