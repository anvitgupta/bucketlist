package com.cs4279.buckit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ViewImageActivity extends AppCompatActivity  {
    private FirebaseAuth firebaseAuth;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        firebaseAuth = FirebaseAuth.getInstance();

        String key = getIntent().getStringExtra("key");

        ImageView imageView = findViewById(R.id.imageView);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        GlideApp.with(getApplicationContext())
                .load(storageReference.child("AnvitGupta/" + key + ".jpg"))
                .into(imageView);
    }

}
