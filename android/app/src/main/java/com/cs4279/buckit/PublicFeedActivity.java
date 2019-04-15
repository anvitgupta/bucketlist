package com.cs4279.buckit;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;

public class PublicFeedActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener  authStateListener;
    private RecyclerView cardsLayout;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseDatabase mDatabase;
    private ArrayList<Item> itemsList;
    private HashSet<String> personalItemIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_public_feed);

        cardsLayout = findViewById(R.id.cardsList);
        itemsList = new ArrayList<Item>();

        personalItemIds = (HashSet<String>) getIntent().getSerializableExtra("personalItemIds");

        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }
        };

        mDatabase = FirebaseDatabase.getInstance();

        final DatabaseReference initialItemsReference = mDatabase.getReference("bucket_list_items");

        ValueEventListener initialItemsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemsList.clear();
                for (DataSnapshot itemSnapshot: dataSnapshot.getChildren()) {
                    Item newItem = itemSnapshot.getValue(Item.class);
                    if (personalItemIds.contains(itemSnapshot.getKey())) {
                        newItem.setIsInPersonalList(true);  // set to false by default
                    }
                    itemsList.add(newItem);
                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO
                // Getting Metadata failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        initialItemsReference.addValueEventListener(initialItemsListener);

        final DatabaseReference personalListReference = mDatabase.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).child("personal_list");

        class ExtendedCardClickListener extends CardClickListener {
            @Override
            public void onClick(View v) {
                // Should not be called until the setter methods from CardClickListener() have been called
                if (buttonType == MARK_AS_DONE) {
                    Intent i = new Intent(getApplicationContext(), MarkAsCompleteActivity.class);
                    i.putExtra("cardID", cardID);
                    startActivity(i);
                    finish();
                } else if (buttonType == ADD_TO_BUCKIT) {
                    // Get the info from selected card via itemsList and cardID
                    // Cannot use the index of the card in the list in case it changes between getting and setting the CardClickListener
                    String title = "", description = "", creator = "", date = "";
                    int timestamp = -1;
                    for (Item i : itemsList) {
                        if (i.getKey() == cardID) {
                            title = i.getTitle();
                            description = i.getDescription();
                            creator = i.getCreator();
                            date = i.getDate();
                            timestamp = i.getTimestamp();
                            break;
                        }
                    }

                    DatabaseReference pushedReference = initialItemsReference.push();
                    Item newItem = new Item(pushedReference.getKey(), title, description, creator, date, false, timestamp, 1.0);
                    pushedReference.setValue(newItem);
                    String itemKey = pushedReference.getKey();
                    personalListReference.push().setValue(itemKey);

                    // Hack to re-run onBindViewHolder in mAdapter
                    Intent i = new Intent(getApplicationContext(), PublicFeedActivity.class);
                    personalItemIds.add(pushedReference.getKey());  // Temporarily add ID to local variable
                    i.putExtra("personalItemIds", personalItemIds);
                    startActivity(i);
                    finish();
                }
            }
        }

        ArrayList<CardClickListener> listeners = new ArrayList<CardClickListener>();
        for (int i = 0; i < (itemsList.size() == 0 ? 50 : itemsList.size()); ++i) {
            listeners.add(new ExtendedCardClickListener());
        }

        mAdapter = new ItemsAdapter(itemsList, listeners);
        cardsLayout.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        cardsLayout.setLayoutManager(mLayoutManager);

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
