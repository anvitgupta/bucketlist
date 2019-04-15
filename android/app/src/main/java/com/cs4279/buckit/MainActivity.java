package com.cs4279.buckit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    Button deleteAccountButton,logoutButton,publicButton;
    FloatingActionButton addItemFAB;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener  authStateListener;
    private RecyclerView cardsLayout;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseDatabase mDatabase;
    private ArrayList<Item> itemsList;
    private HashSet<String> personalItemIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView1);
        deleteAccountButton = findViewById(R.id.deleteAccountButton);
        logoutButton = findViewById(R.id.logoutButton);
        publicButton = findViewById(R.id.publicButton);
        addItemFAB = findViewById(R.id.addItemFAB);

        cardsLayout = findViewById(R.id.cardsList);
        itemsList = new ArrayList<Item>();
        personalItemIDs = new HashSet<String>();

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

        publicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),PublicFeedActivity.class);
                i.putExtra("personalItemIds", personalItemIDs);
                startActivity(i);
            }
        });

        addItemFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AddItemActivity.class));
                // do NOT finish();
            }
        });

        mDatabase = FirebaseDatabase.getInstance();

        final DatabaseReference personalListReference = mDatabase.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).child("personal_list");

        ValueEventListener personalListListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                personalItemIDs.clear();
                for (DataSnapshot itemIDSnapshot : dataSnapshot.getChildren()) {
                    String itemID = itemIDSnapshot.getValue(String.class);
                    personalItemIDs.add(itemID);
                }

                // After updating personal bucket list IDs, fetch the appropriate posts
                DatabaseReference initialItemsReference = mDatabase.getReference("bucket_list_items");

                ValueEventListener initialItemsListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        itemsList.clear();
                        for (DataSnapshot itemSnapshot: dataSnapshot.getChildren()) {
                            if (!personalItemIDs.contains(itemSnapshot.getKey())) continue;
                            Item newItem = itemSnapshot.getValue(Item.class);
                            newItem.setIsInPersonalList(true);  // set to false by default

                            int start_size = itemsList.size();
                            for (int i = 0; i < start_size; ++i) {
                                if (itemsList.get(i).getTimestamp() < newItem.getTimestamp()) {
                                    itemsList.set(i, newItem);
                                    break;
                                }
                            }
                            if (itemsList.size() == start_size) {
                                itemsList.add(newItem);
                            }
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
                initialItemsReference.addListenerForSingleValueEvent(initialItemsListener); // Use this for now, perhaps switch to child event listener later?
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        personalListReference.addValueEventListener(personalListListener);

        final DatabaseReference newItemsReference = mDatabase.getReference().child("bucket_list_items");

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
                    String title = "", description = "", original_creator = "", date = "";
                    int timestamp = -1;
                    for (Item i : itemsList) {
                        if (i.getKey() == cardID) {
                            title = i.getTitle();
                            description = i.getDescription();
                            original_creator = i.getOriginalCreator();
                            date = i.getDate();
                            timestamp = i.getTimestamp();
                            break;
                        }
                    }

                    DatabaseReference pushedReference = newItemsReference.push();
                    Item newItem = new Item(pushedReference.getKey(), title, description, original_creator, firebaseAuth.getCurrentUser().getUid(), date, false, timestamp, -1, 1.0);
                    pushedReference.setValue(newItem);
                    String itemKey = pushedReference.getKey();
                    personalListReference.push().setValue(itemKey);

                    // Hack to re-run onBindViewHolder in mAdapter
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }

        ArrayList<CardClickListener> listeners = new ArrayList<CardClickListener>();
        for (int i = 0; i < 50; ++i) {
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