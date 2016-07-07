package com.rahulfakir.theboldcircle.StoreData;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.rahulfakir.theboldcircle.DataManipulation;
import com.rahulfakir.theboldcircle.R;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StoreChatActivity extends AppCompatActivity {
    private List<MessageObject> messageList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MessageListAdapter mAdapter;
    private ImageButton btnSendMessage;
    private TextView etMessage;
    private FirebaseDatabase mDatabase;
    private DatabaseReference messageRef;
    private ChildEventListener childEventListener;
    private String username = "", storeID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_chat);

        mDatabase = FirebaseDatabase.getInstance();

        btnSendMessage = (ImageButton) findViewById(R.id.btnSendMessage);
        etMessage = (TextView) findViewById(R.id.etMessage);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        DataManipulation encoder = new DataManipulation();
        username = encoder.encodeEmailToUsername(email);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            storeID = bundle.getString("storeID");
        }

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((etMessage.getText().toString().length() != 0) && (storeID.length() * username.length() != 0)) {


                    DateFormat df = new SimpleDateFormat("ddMMyyyyHHmmssSSS");
                    Date today = Calendar.getInstance().getTime();
                    String messageID = df.format(today);
                    messageID = messageID.substring(0, messageID.length() - 1) + 0;


                    DatabaseReference messageRef = mDatabase.getReference("messageData").child("messages");
                    messageRef.child(storeID).child(username).child(messageID).setValue(etMessage.getText().toString());
                    etMessage.setText("");
                   // recyclerView.scrollToPosition(messageList.size() - 1);
                }
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new MessageListAdapter(messageList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareMessageData();
    }

    private void prepareMessageData() {


        if (true) {//this is stupid, still need to figure out why I did this
            messageRef = mDatabase.getReference("messageData").child("messages").child(storeID).child(username);


            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                    MessageObject message = new MessageObject();
                    String messageID = null;

                    messageID = dataSnapshot.getKey().toString();
                    int messageType = Integer.valueOf(messageID.substring(messageID.length() - 1));
                    String dateStamp = messageID.substring(0, messageID.length() - 1);
                    dateStamp = dateStamp.substring(0, 2) + "/" + dateStamp.substring(2, 4) + "/" +
                            dateStamp.substring(4, 8) + " " + dateStamp.substring(8, 10) + ":" +
                            dateStamp.substring(10, 12);
                    message = new MessageObject(messageID, dataSnapshot.getValue().toString(), dateStamp, messageType);
                    messageList.add(message);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(StoreChatActivity.this, "Unable to send message", Toast.LENGTH_SHORT).show();
                }
            };
            messageRef.addChildEventListener(childEventListener);
        } else {
            Toast.makeText(StoreChatActivity.this, "Unable to send message", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        messageRef.removeEventListener(childEventListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

}