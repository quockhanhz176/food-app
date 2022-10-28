package com.example.foodapp.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseRepository {
    private static FirebaseRepository instance;

    private FirebaseDatabase database;

    private FirebaseRepository() {
        database =
                FirebaseDatabase.getInstance(FirebaseConfig.FIREBASE_REALTIME_DATABASE_URL);
    }

    public synchronized static FirebaseRepository getRealtimeDatabaseInstance() {
        if(instance == null) {
            instance = new FirebaseRepository();
        }

        return instance;
    }

    public void saveNewUser(String email) {
        DatabaseReference userReference = database.getReference("user");
    }
}
