package com.example.loginschoolpool;
import androidx.annotation.NonNull;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {

    private DatabaseReference databaseUsersReference;
    private FirebaseDatabase firebaseReference;
    private String userID;


    public FirebaseDatabaseHelper() {
        firebaseReference = FirebaseDatabase.getInstance();
        databaseUsersReference = firebaseReference.getReference("Users");
    }
}



//    public void readUsers(){
//        databaseUsersReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                usersList.clear();
//                List<String>keys = new ArrayList<>();
//                for(DataSnapshot ds : dataSnapshot.getChildren()){
//                    User user = new User();
//                    user.getUsername(ds.child().)
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        })
//    }
//}
