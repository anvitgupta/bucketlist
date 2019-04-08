package com.cs4279.buckit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MarkAsCompleteActivity extends AppCompatActivity {

    TextInputEditText newItemText;
    Button submitButton;
    private DatabaseReference mDatabase;
    private int count;
    FirebaseAuth firebaseAuth;
    private String cardID;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_as_complete);

        newItemText = (TextInputEditText) findViewById(R.id.newItemField);
        submitButton = findViewById(R.id.submitButton);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        cardID = getIntent().getStringExtra("cardID");


        final DatabaseReference itemReference = mDatabase.child("bucket_list_items").child(cardID);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set boolean completed flag in firebase to true
                itemReference.child("completed").setValue(true);

                // TODO: Create post indicating that item is complete

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

}
