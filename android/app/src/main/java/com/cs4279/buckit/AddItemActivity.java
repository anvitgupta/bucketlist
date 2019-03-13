package com.cs4279.buckit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddItemActivity extends AppCompatActivity {

    TextInputEditText newItemText;
    Button submitButton;
    private DatabaseReference mDatabase;
    private int count;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        newItemText = (TextInputEditText) findViewById(R.id.newItemField);
        submitButton = findViewById(R.id.submitButton);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        // TODO: No need for Metadata on lenght of list, switch to push() method instead.
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Metadata object and use the values to update the UI
                Metadata count_data = dataSnapshot.getValue(Metadata.class);
                count = Integer.parseInt(count_data.demo_count);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO
                // Getting Metadata failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.addValueEventListener(postListener);
        // From the TO-DO to here is probably not needed anymore


        final DatabaseReference newItemsReference = mDatabase.child("bucket_list_items");
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newItemStr = newItemText.getText().toString();

                if(TextUtils.isEmpty(newItemStr)){
                    Toast.makeText(getApplicationContext(),"Please fill in the required fields",Toast.LENGTH_SHORT).show();
                    return;
                }

                // Send to Real-time Database
                ++count;
                mDatabase.child("demo").child("" + count).setValue(newItemStr);
                mDatabase.child("demo_count").setValue("" + count);

                Item newItem = new Item(newItemStr, newItemStr, "Admin", "03-13-19", 1.0);
                newItemsReference.push().setValue(newItem);

                finish();
            }
        });

    }

}
