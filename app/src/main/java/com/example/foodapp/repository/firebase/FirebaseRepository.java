package com.example.foodapp.repository.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.foodapp.repository.firebase.entity.User;
import com.example.foodapp.viewmodel.utils.MD5Utils;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FirebaseRepository {

    private final FirebaseDatabase database;

    @Inject
    FirebaseRepository(FirebaseDatabase database) {
        this.database = database;
    }

    public void saveNewUser(@NonNull String email, @Nullable Consumer<Task> onComplete) throws InputMismatchException {
        DatabaseReference userReference = database.getReference("users");

        User newUser = new User(email, "", null);

        String emailHash = MD5Utils.md5Hex(email);
        if (emailHash == null) {
            throw new InputMismatchException("Not a valid email address");
        }

        Map<String, Object> userUpdate = new HashMap<>();
        userUpdate.put(emailHash, newUser);

        userReference.updateChildren(userUpdate).addOnCompleteListener(task -> {
            if (onComplete == null) {
                return;
            }
            onComplete.accept(task);
        });
    }

    public void fetchUser(String email, @Nullable Consumer<User> onComplete) {
        DatabaseReference userReference = database.getReference("users");

        String emailHash = MD5Utils.md5Hex(email);
        if (emailHash == null) {
            throw new InputMismatchException("Not a valid email address");
        }

        userReference.child(emailHash).get().addOnCompleteListener(
                task -> {
                    if (onComplete == null || !task.isComplete()) {
                        return;
                    }

                    DataSnapshot snapshot = task.getResult();

                    if (!snapshot.exists()) {
                        return;
                    }


                    User user = snapshot.getValue(User.class);
                    onComplete.accept(user);
                }
        );
    }
}
