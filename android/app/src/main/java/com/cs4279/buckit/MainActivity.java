package com.cs4279.buckit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    Button deleteAccountButton,logoutButton;
    FloatingActionButton addItemFAB;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener  authStateListener;
    private LinearLayout cardsLayout;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView1);
        deleteAccountButton = findViewById(R.id.deleteAccountButton);
        logoutButton = findViewById(R.id.logoutButton);
        addItemFAB = findViewById(R.id.addItemFAB);

        cardsLayout = findViewById(R.id.cardsList);

        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null){
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }
        };

        final FirebaseUser user  = firebaseAuth.getCurrentUser();
        textView.setText("Account: " + user.getEmail());

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user!=null){
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(),"User deleted",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
                                        finish();
                                    }
                                }
                            });
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });

        addItemFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AddItemActivity.class));
                // do NOT finish();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference newItemsReference = mDatabase.child("bucket_list_items");

        ValueEventListener newItemListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Metadata object and use the values to update the UI
                Item newItem = dataSnapshot.getValue(Item.class);

                CardView card = new CardView(getApplicationContext());

                // Set the CardView layoutParams
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                card.setLayoutParams(params);

                // Set CardView corner radius
                card.setRadius(9);

                // Set cardView content padding
                card.setContentPadding(15, 15, 15, 15);

                // Set a background color for CardView
                card.setCardBackgroundColor(Color.parseColor("#FFC6D6C3"));

                // Set the CardView maximum elevation
                card.setMaxCardElevation(15);

                // Set CardView elevation
                card.setCardElevation(9);

                // Initialize a new TextView to put in CardView
                TextView tv = new TextView(getApplicationContext());
                tv.setLayoutParams(params);
                tv.setText(newItem.getDescription());
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);

                // Put the TextView in CardView
                card.addView(tv);

                cardsLayout.addView(card);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO
                // Getting Metadata failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        newItemsReference.addValueEventListener(newItemListener);

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener!=null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}