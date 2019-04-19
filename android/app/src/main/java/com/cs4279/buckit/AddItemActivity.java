package com.cs4279.buckit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddItemActivity extends AppCompatActivity {

    TextInputEditText newItemTitle;
    TextInputEditText newItemText;
    Button submitButton;
    Button chooseButton;
    ImageView imageView;
    private DatabaseReference mDatabase;
    private int count;
    FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private final int PICK_IMAGE_REQUEST = 539;
    Uri filePath;
    ProgressDialog pd;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        newItemTitle = findViewById(R.id.newItemTitleField);
        newItemText = findViewById(R.id.newItemDescriptionField);
        submitButton = findViewById(R.id.submitButton);
        chooseButton = findViewById(R.id.chooseButton);
        imageView = findViewById(R.id.uploadImage);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference("AnvitGupta");
        pd = new ProgressDialog(this);
        pd.setMessage("Uploading....");

        // This is obsolete and can be deleted: No need for Metadata on length of list, switch to push() method instead.
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
        // From above comment to here is probably not needed anymore


        final DatabaseReference newItemsReference = mDatabase.child("bucket_list_items");
        final DatabaseReference personalBucketListReference = mDatabase.child("users").child(firebaseAuth.getCurrentUser().getUid()).child("personal_list");
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                String newItemTitleStr = newItemTitle.getText().toString();
                String newItemTextStr = newItemText.getText().toString();

                if(TextUtils.isEmpty(newItemTextStr) || TextUtils.isEmpty(newItemTitleStr)){
                    Toast.makeText(getApplicationContext(),"Please fill in the required fields",Toast.LENGTH_SHORT).show();
                    return;
                }

                // Send to Real-time Database
                //++count;
                //mDatabase.child("demo").child("" + count).setValue(newItemStr);
                //mDatabase.child("demo_count").setValue("" + count);

                DatabaseReference pushedReference = newItemsReference.push();


                boolean hasPhoto = false;

                // Upload image
                if(filePath != null) {
                    hasPhoto = true;

                    StorageReference childRef = storageReference.child(pushedReference.getKey() + ".jpg");

                    //uploading the image
                    UploadTask uploadTask = childRef.putFile(filePath);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(AddItemActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddItemActivity.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(AddItemActivity.this, "Select an image", Toast.LENGTH_SHORT).show();
                }


                // Upload BuckIt Item information
                long cur_timestamp = System.currentTimeMillis() / 1000L;

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = df.format(c);

                Item newItem = new Item(pushedReference.getKey(), newItemTitleStr, newItemTextStr, firebaseAuth.getCurrentUser().getDisplayName(), firebaseAuth.getCurrentUser().getUid(), formattedDate, false, hasPhoto, cur_timestamp, -1, 0.0);
                pushedReference.setValue(newItem);
                String itemKey = pushedReference.getKey();
                personalBucketListReference.push().setValue(itemKey);

                pd.dismiss();
                finish();
            }
        });

        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                // Getting image from gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                // Setting image to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
