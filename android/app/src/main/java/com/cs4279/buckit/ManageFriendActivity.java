package com.cs4279.buckit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashSet;

public class ManageFriendActivity extends AppCompatActivity {
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
        // add functionality here
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

        DatabaseReference initialItemsReference = mDatabase.getReference("bucket_list_items");

        ValueEventListener initialItemsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemsList.clear();
                for (DataSnapshot itemSnapshot: dataSnapshot.getChildren()) {

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
        //write here
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener!=null){
        }
    }
}