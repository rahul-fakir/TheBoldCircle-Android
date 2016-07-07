package com.rahulfakir.theboldcircle.ProductData;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rahulfakir.theboldcircle.HomeScreenActivity;
import com.rahulfakir.theboldcircle.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by rahulfakir on 6/25/16.
 */

public class CheckoutListAdapter extends RecyclerView.Adapter<CheckoutListAdapter.ViewHolder> {

    private LinkedHashMap<Double, ArrayList<String>> availableAppointments = new LinkedHashMap<Double, ArrayList<String>>();
    private List<CheckoutObject> checkoutObjectsList;
    private Context context;
    private Double openingTime, closingTime;

    private ArrayList<ViewHolder> holders = new ArrayList();

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, price, sku, selectedAppointmentDate, appointmentStore, appointmentTime, appointmentDate;
        private RelativeLayout rlEditAppointment, rlAppointmentView;
        private Spinner spnStores, spnEmployees, spnTimes;
        private Button btnSelectDate, btnEditAppointment;


        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tvName);
            price = (TextView) view.findViewById(R.id.tvPrice);
            sku = (TextView) view.findViewById(R.id.tvSKU);
            appointmentStore = (TextView) view.findViewById(R.id.tvAppointmentStore);
            appointmentDate = (TextView) view.findViewById(R.id.tvAppointmentDate);
            appointmentTime = (TextView) view.findViewById(R.id.tvAppointmentTime);
            rlEditAppointment = (RelativeLayout) view.findViewById(R.id.rlEditAppointment);
            rlAppointmentView = (RelativeLayout) view.findViewById(R.id.rlAppointmentView);
            spnStores = (Spinner) view.findViewById(R.id.spnStores);
            spnEmployees = (Spinner) view.findViewById(R.id.spnEmployees);
            spnTimes = (Spinner) view.findViewById(R.id.spnTimes);
            btnSelectDate = (Button) view.findViewById(R.id.btnSelectDate);
            selectedAppointmentDate = (TextView) view.findViewById(R.id.tvSelectedAppointmentDate);
            btnEditAppointment = (Button) view.findViewById(R.id.btnEditAppointment);
        }
    }


    public CheckoutListAdapter(List<CheckoutObject> checkoutObjectsList, Context context) {
        this.checkoutObjectsList = checkoutObjectsList;
        this.context = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.checkout_row_item, parent, false);

        ViewHolder currentViewHolder = new ViewHolder(itemView);
        holders.add(currentViewHolder);
        System.out.println(holders.size());
        return currentViewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CheckoutObject checkoutObject = checkoutObjectsList.get(position);
        holder.name.setText(checkoutObject.getName());
        holder.price.setText(String.valueOf(checkoutObject.getPrice()));
        holder.sku.setText(checkoutObject.getSku());

        if (checkoutObject.getType() == 1) {


            holder.btnEditAppointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!checkoutObject.getAppointmentStatus()) {
                        holder.rlEditAppointment.setVisibility(View.GONE);

                        String storeName = holder.spnStores.getSelectedItem().toString();
                        checkoutObject.setAppointmentStoreName(storeName);

                        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference storesRef = mDatabase.getReference("storeData/storeNames/" + storeName);


                        storesRef.addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {


                                        String id = dataSnapshot.getValue().toString();
                                        checkoutObject.setAppointmentStoreId(id);

                                    }


                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }

                                });


                        String dateString = holder.selectedAppointmentDate.getText().toString().substring(4, holder.selectedAppointmentDate.length());
                        checkoutObject.setFullAppointmentDate(dateString);
                        String appointmentDate = "";
                        Date date = null;
                        try {
                            date = new SimpleDateFormat("dd MMM yyyy").parse(dateString);
                            String day = String.valueOf(date.getDate());
                            if (day.length() == 1) {
                                day = "0" + day;
                            }
                            String month = String.valueOf(date.getMonth());
                            if (month.length() == 1) {
                                month = "0" + month;
                            }

                            String year = String.valueOf(1900 + date.getYear());

                            appointmentDate = day + month + year;
                            checkoutObject.setAppointmentDate(appointmentDate);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        String sAppointmentTime = holder.spnTimes.getSelectedItem().toString().substring(0, holder.spnTimes.getSelectedItem().toString().indexOf("-"));

                        double hour = Double.valueOf(sAppointmentTime.substring(0, sAppointmentTime.indexOf(":")));
                        double minutes = Double.valueOf(sAppointmentTime.substring(sAppointmentTime.indexOf(":") + 1, sAppointmentTime.length())) / 60;
                        double appointmentTime = hour + minutes;
                        checkoutObject.setAppointmentTime(appointmentTime);
                        sAppointmentTime = appointmentTime + "-" + (appointmentTime + 0.25 * checkoutObject.sessionsRequired);
                        checkoutObject.setFullAppointmentTime(sAppointmentTime);
                        checkoutObject.setAppointmentStatus(true);
                        System.out.println("Set TO:" + checkoutObject.getAppointmentStatus());


                        holder.appointmentStore.setText(checkoutObjectsList.get(position).getAppointmentStoreName());
                        holder.appointmentDate.setText(dateString);
                        holder.appointmentTime.setText(sAppointmentTime);

                        holder.rlAppointmentView.setVisibility(View.VISIBLE);
                    }
                }
            });



            System.out.println(checkoutObject.getAppointmentStatus());
            System.out.println(checkoutObject.getAppointmentStoreName());
            if (!checkoutObjectsList.get(position).getAppointmentStatus()) {
                System.out.println("IT IS NOOOOOOOOOOOOOOOOOOOOot SETTTTTTTTTTTTTTTTTTTTTT");
                holder.rlEditAppointment.setVisibility(View.VISIBLE);

            ArrayAdapter<String> storesAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, CheckoutActivity.storeNames);
            storesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.spnStores.setAdapter(storesAdapter);
            holder.spnStores.setSelection(0, true);
            holder.btnSelectDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckoutActivity.currentViewHolder = position;
                    ((Activity) CheckoutListAdapter.this.context).showDialog(999);
                }
            });


            holder.spnEmployees.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int spinnerPosition, long id) {
                    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference schedulesRef = null;
                    final int sessionsRequired = checkoutObjectsList.get(position).getSessionsRequired();
                    String appointmentDate = "";
                    try {
                        appointmentDate = formatDate(checkoutObjectsList.get(position).getDay(),
                                checkoutObjectsList.get(position).getMonth(),
                                checkoutObjectsList.get(position).getYear(), "ddMMyyy");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    schedulesRef = mDatabase.getReference("storeData/appointmentData/" + checkoutObjectsList.get(position).getStoreID()
                            + "/" + appointmentDate + "/schedules/employeeSchedules");

                    if (spinnerPosition != 0) {
                        schedulesRef = schedulesRef.child(HomeScreenActivity.employeeDetails.get(holder.spnEmployees.getSelectedItem()));

                        final DatabaseReference finalSchedulesRef = schedulesRef;
                        schedulesRef.addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {


                                        //                    System.out.println("START HERE");
                                        ArrayList<Double> unavailableAppointments = new ArrayList<Double>();
                                        for (DataSnapshot postSnapshotEmployeeAppointments : dataSnapshot.getChildren()) {
                                            // System.out.println(postSnapshotEmployeeAppointments.getKey());
                                            String preConvertedTime = postSnapshotEmployeeAppointments.getKey();
                                            preConvertedTime = preConvertedTime.replace("-", ".");

                                            unavailableAppointments.add(Double.valueOf(preConvertedTime));
                                        }
                                        //                  System.out.println("END HERE");

                                        ArrayList<Double> adjustedUnavailableAppointments = new ArrayList<Double>();
                                        for (int i = 0; i < unavailableAppointments.size(); i++) {
                                            //  System.out.println((unavailableAppointments.get(i) - (0.25 * (sessionsRequired -1) )) + " - " + (unavailableAppointments.get(i) + 0.25));
                                            // System.out.println("Timetable");
                                            for (double j = unavailableAppointments.get(i) - (0.25 * (sessionsRequired - 1)); j < unavailableAppointments.get(i) + 0.25; j = j + 0.25) {
                                                if ((j >= openingTime) && (j <= closingTime - sessionsRequired * 0.25)) {
                                                    adjustedUnavailableAppointments.add(j);
                                                }
                                            }

                                        }


                                        for (double i = closingTime - (0.25 * (sessionsRequired - 1)); i < closingTime; i += 0.25) {
                                            adjustedUnavailableAppointments.add(i);
                                        }

                                        resetTimesMap();

                                        for (double i = openingTime; i < closingTime; i += 0.25) {
                                            if (!adjustedUnavailableAppointments.contains(i)) {
                                                ArrayList<String> currentAppointments = availableAppointments.get(i);
                                                currentAppointments.add(holder.spnEmployees.getSelectedItem().toString());
                                                availableAppointments.put(i, currentAppointments);
                                            }
                                        }

                                        ArrayList<String> availableTimes = new ArrayList<String>();


                                        for (Map.Entry<Double, ArrayList<String>> entry : availableAppointments.entrySet()) {
                                            Double key = entry.getKey();

                                            ArrayList<String> value = entry.getValue();

                                            for (int i = 0; i < value.size(); i++) {
                                                availableTimes.add(convertBaseTenTime(key) + " - " + String.valueOf(value.get(i)));
                                            }

                                        }

                                        ArrayAdapter<String> timesAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, availableTimes);
                                        timesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        holder.spnTimes.setAdapter(timesAdapter);


                                    }


                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }

                                });

                    } else {
                        schedulesRef.addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getChildrenCount() == 0) {
                                            for (Map.Entry<String, String> entry : HomeScreenActivity.employeeDetails.entrySet()) {
                                                System.out.println(entry.getKey());
                                            }


                                            for (Double i = openingTime; i < closingTime - (sessionsRequired - 1) * 0.25; i += 0.25) {
                                                System.out.println(i);
                                                for (Map.Entry<String, String> entry : HomeScreenActivity.employeeDetails.entrySet()) {
                                                    System.out.println(entry.getKey());
                                                    ((ArrayList<String>) (availableAppointments.get(i))).add(entry.getKey());

                                                }

                                            }


                                            ArrayList<String> availableTimes = new ArrayList<String>();
                                            for (Map.Entry<Double, ArrayList<String>> entry : availableAppointments.entrySet()) {
                                                Double key = entry.getKey();
                                                System.out.println(key);
                                                System.out.println(entry.getValue().size());


                                                if (entry.getValue().size() != 0) {
                                                    availableTimes.add(convertBaseTenTime(key) + " - " + "All");
                                                }


                                            }

                                            ArrayAdapter<String> timesAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, availableTimes);
                                            timesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            holder.spnTimes.setAdapter(timesAdapter);

                                            System.out.println();
                                        } else {
                                            resetTimesMap();
                                            for (DataSnapshot postSnapshotEmployees : dataSnapshot.getChildren()) {

                                                // System.out.println("EMP KEY" + postSnapshotEmployees.getKey());
                                                String currentEmployee = "";
                                                for (Map.Entry<String, String> entry : HomeScreenActivity.employeeDetails.entrySet()) {
                                                    String key = entry.getKey();

                                                    String value = entry.getValue();

                                                    if (value.equals(postSnapshotEmployees.getKey())) {
                                                        currentEmployee = key;
                                                        break;
                                                    }

                                                }
                                                ArrayList<Double> unavailableAppointments = new ArrayList<Double>();
                                                for (DataSnapshot postSnapshotEmployeeAppointments : postSnapshotEmployees.getChildren()) {


                                                    String preConvertedTime = postSnapshotEmployeeAppointments.getKey();
                                                    preConvertedTime = preConvertedTime.replace("-", ".");

                                                    unavailableAppointments.add(Double.valueOf(preConvertedTime));
                                                }

                                                ArrayList<Double> adjustedUnavailableAppointments = new ArrayList<Double>();
                                                for (int i = 0; i < unavailableAppointments.size(); i++) {
                                                    //  System.out.println((unavailableAppointments.get(i) - (0.25 * (sessionsRequired -1) )) + " - " + (unavailableAppointments.get(i) + 0.25));
                                                    //   System.out.println("Timetable");
                                                    for (double j = unavailableAppointments.get(i) - (0.25 * (sessionsRequired - 1)); j < unavailableAppointments.get(i) + 0.25; j = j + 0.25) {
                                                        if ((j >= openingTime) && (j <= closingTime - sessionsRequired * 0.25)) {
                                                            adjustedUnavailableAppointments.add(j);
                                                        }
                                                    }

                                                }


                                                for (double i = closingTime - (0.25 * (sessionsRequired - 1)); i < closingTime; i += 0.25) {
                                                    adjustedUnavailableAppointments.add(i);
                                                }


                                                for (double i = openingTime; i < closingTime; i += 0.25) {
                                                    if (!adjustedUnavailableAppointments.contains(i)) {
                                                        ((ArrayList<String>) (availableAppointments.get(i))).add(currentEmployee);
                                                        //   ArrayList<String> currentAppointments = availableAppointments.get(i);
                                                        //  System.out.println("SIZE:" + availableAppointments.get(i).size());
                                                        //  currentAppointments.add(holder.spnEmployees.getSelectedItem().toString());
                                                        // availableAppointments.put(i, currentAppointments);
                                                    }
                                                }

                                                ArrayList<String> availableTimes = new ArrayList<String>();


                                                for (Map.Entry<Double, ArrayList<String>> entry : availableAppointments.entrySet()) {
                                                    Double key = entry.getKey();

                                                    ArrayList<String> value = entry.getValue();
                                                    //   System.out.println(key + " - " + value.size());
                                                    if (value.size() != 0) {
                                                        availableTimes.add(convertBaseTenTime(key) + " - " + currentEmployee);
                                                    }


                                                }

                                                ArrayAdapter<String> timesAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, availableTimes);
                                                timesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                holder.spnTimes.setAdapter(timesAdapter);


                                            }
                                        }

                                    }


                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }

                                });
                    }

                    //  System.out.println(schedulesRef);


                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });
        } else
            {
                System.out.println("IT IS SETTTTTTTTTTTTTTTTTTTTTT");
                holder.rlEditAppointment.setVisibility(View.GONE);
                holder.appointmentStore.setText(checkoutObject.getAppointmentStoreName());
                holder.appointmentDate.setText(checkoutObject.getFullAppointmentDate());
                holder.appointmentTime.setText(checkoutObject.getFullAppointmentTime());

               holder.rlAppointmentView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return checkoutObjectsList.size();
    }

    public void dateSet(final int currentViewHolder, int year, int month, final int day) throws ParseException {

        checkoutObjectsList.get(currentViewHolder).setDay(day);
        checkoutObjectsList.get(currentViewHolder).setMonth(month);
        checkoutObjectsList.get(currentViewHolder).setYear(year);
        String sAppointmentDate = formatDate(day, month, year, "EEE d MMM yyyy");
        holders.get(currentViewHolder).selectedAppointmentDate.setText(sAppointmentDate);
        final String dayOfWeek = formatDate(day, month, year, "EEEE");

        String storeID = CheckoutActivity.storeIds.get(holders.get(currentViewHolder).spnStores.getSelectedItemPosition());
        checkoutObjectsList.get(currentViewHolder).setStoreID(storeID);
        String ref = "storeData/storeSchedules/" + storeID + "/" + dayOfWeek.toLowerCase();

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference schedulesRef = mDatabase.getReference(ref);


     //   System.out.println(ref);
        schedulesRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Boolean isOpen = Boolean.valueOf(dataSnapshot.child("isOpen").getValue().toString());
                        if (!isOpen) {
                            Toast.makeText(context, "This store is not open on " + dayOfWeek,
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Double openingTime = Double.valueOf(dataSnapshot.child("hours").child("open").getValue().toString());
                            Double closingTime = Double.valueOf(dataSnapshot.child("hours").child("close").getValue().toString());
                            prepareStoreAppointments(openingTime, closingTime);
                            int skillRequired = checkoutObjectsList.get(currentViewHolder).getSkillRequired();
                            int sessionsRequired = checkoutObjectsList.get(currentViewHolder).getSessionsRequired();
                            if (!dataSnapshot.child("skillsOffered").child(String.valueOf(skillRequired)).exists()) {
                                Toast.makeText(context, "This store does not offer this service ",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                ArrayList<String> employees = new ArrayList<>();
                                employees.add("Any Employee");
                                HomeScreenActivity.employeeDetails.clear();
                                for (DataSnapshot postSnapshotEmployees : dataSnapshot.child("skillsOffered").child(String.valueOf(skillRequired)).getChildren()) {
                                    employees.add(postSnapshotEmployees.getValue().toString());

                                    if (!HomeScreenActivity.employeeDetails.containsKey(postSnapshotEmployees.getValue().toString())) {
                                        HomeScreenActivity.employeeDetails.put(postSnapshotEmployees.getValue().toString(), postSnapshotEmployees.getKey());
                            //            System.out.println("KEY IS:" + postSnapshotEmployees.getValue().toString());
                                    }

                                }

                                ArrayAdapter<String> storesAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, employees);
                                storesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                holders.get(currentViewHolder).spnEmployees.setAdapter(storesAdapter);
                            }
                        }


                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

    }

    public String formatDate(int day, int month, int year, String formatPattern) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date appointmentDate = formatter.parse(day + "/" + (month + 1) + "/" + year);
        formatter.applyPattern(formatPattern);
        String sDate = formatter.format(appointmentDate);
        return sDate;

    }

    public void prepareStoreAppointments(Double openingTime, Double closingTime) {
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        Double sessionDuration = 0.25;
        for (Double i = openingTime; i < closingTime; i = i + sessionDuration) {
            ArrayList<String> currentSession = new ArrayList<String>();
            availableAppointments.put(i, currentSession);
        }

    }

    public void resetTimesMap() {
        for (Map.Entry<Double, ArrayList<String>> entry : availableAppointments.entrySet()) {
            Double key = entry.getKey();
            ArrayList<String> value = entry.getValue();
            value.clear();
            availableAppointments.put(key, value);
            // ...
        }
    }

    public String convertBaseTenTime(double unFormattedTime) {

        int hours = (int) unFormattedTime;
        int minutes = (int) (unFormattedTime * 60) % 60;

        return (hours + ":" + minutes);
    }


}