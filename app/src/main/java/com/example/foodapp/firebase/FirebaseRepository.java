package com.example.foodapp.firebase;

import com.example.foodapp.firebase.entity.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.function.Consumer;

public class FirebaseRepository {
    private static FirebaseRepository instance;

    private final FirebaseDatabase database;

    private FirebaseRepository() {
        database =
                FirebaseDatabase.getInstance(FirebaseConfig.FIREBASE_REALTIME_DATABASE_URL);
    }

    public synchronized static FirebaseRepository getRealtimeDatabaseInstance() {
        if (instance == null) {
            instance = new FirebaseRepository();
        }

        return instance;
    }

    public Task<Void> saveNewUser(String email, Consumer<User> listener) {
        DatabaseReference userReference = database.getReference("users");

        User newUser = new User(email, "", null);
        listener.accept(newUser);

        return userReference.child(email).setValue(newUser);
    }

    public void fetchUser(String email, Consumer<User> listener) {
        DatabaseReference userReference = database.getReference("users");

        userReference.child(email).get().addOnCompleteListener(
                data -> {
                    if (!data.isComplete()) {
                        return;
                    }

                    DataSnapshot snapshot = data.getResult();

                    if (!snapshot.exists()) {
                        return;
                    }


                    User user = snapshot.getValue(User.class);
                    listener.accept(user);
                }
        );
    }
}
