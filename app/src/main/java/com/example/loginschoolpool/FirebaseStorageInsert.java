package com.example.loginschoolpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class FirebaseStorageInsert extends AppCompatActivity {
    private StorageReference storageReference;


    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_storage_insert);
        storageReference  = FirebaseStorage.getInstance().getReference();

        Uri uri = Uri.fromFile(new File("C:\\Users\\images_for_schpool\\Barak.jpg"));

        final StorageReference ref = storageReference.child("user_images/barak_pic.jpg");

        ImageView imageView = (ImageView)findViewById(R.id.imageView_user);

        Glide.with(this /* context */)
                .load(storageReference)
                .into(imageView);


    }




    }

