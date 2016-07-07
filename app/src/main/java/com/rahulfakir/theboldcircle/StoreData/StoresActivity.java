package com.rahulfakir.theboldcircle.StoreData;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rahulfakir.theboldcircle.R;

import java.util.ArrayList;
import java.util.List;

public class StoresActivity extends AppCompatActivity {
    private List<StoreObject> storeList = new ArrayList<>();
    private RecyclerView recyclerView;
    private StoreListAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        Bundle extras = getIntent().getExtras();
        int listType = 0;
        if (extras != null) {
            listType = extras.getInt("listType");
            // and get whatever type user account id is
        }
        mAdapter = new StoreListAdapter(storeList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                StoreObject store = storeList.get(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        prepareStoreData();
    }

    private void prepareStoreData() {
        findViewById(R.id.lytLoadingPanel).setVisibility(View.VISIBLE);
        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference storesRef = mDatabase.getReference("storeData").child("storeDetails");

        storesRef.orderByChild("name").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshotStores: dataSnapshot.getChildren()) {
                            final StoreObject store = postSnapshotStores.getValue(StoreObject.class);
                            store.setID(postSnapshotStores.getKey());

                            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference hoursRef = mDatabase.getReference("storeData")
                                    .child("storeSchedules").child(store.getID());

                            hoursRef.addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String[] days = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday" } ;
                                            for (int i = 0; i < days.length; i++){
                                                //  System.out.println(dataSnapshot.getKey() + " " + dataSnapshot.child(days[i]).child("open"));
                                                if ((Boolean)(dataSnapshot.child(days[i]).child("isOpen").getValue())) {
                                                    store.setHours(i, 0, Double.valueOf(dataSnapshot.child(days[i]).child("hours").child("open").getValue().toString()));
                                                    store.setHours(i, 1, Double.valueOf(dataSnapshot.child(days[i]).child("hours").child("close").getValue().toString()));
                                                }
                                            }
                                            mAdapter.notifyDataSetChanged();
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                            storeList.add(store);
                        }
                        mAdapter.notifyDataSetChanged();
                        findViewById(R.id.lytLoadingPanel).setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private StoresActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final StoresActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }



}