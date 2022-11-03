package com.example.foodapp.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.foodapp.firebase.entity.RecipeType;
import com.example.foodapp.firebase.entity.UserPreference;
import com.example.foodapp.viewmodel.utils.MD5Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.function.Consumer;

public class UserRepository {

    private static UserRepository instance;
    private final DatabaseReference currentUser;

    private UserRepository(@NonNull String email) {
        FirebaseDatabase database =
                FirebaseDatabase.getInstance(FirebaseConfig.FIREBASE_REALTIME_DATABASE_URL);

        String emailHash = MD5Util.md5Hex(email);
        if (emailHash == null) {
            this.currentUser = null;
            return;
        }

        this.currentUser = database.getReference("users").child(emailHash);
    }

    public synchronized static UserRepository getCurrentUser(String email) {
        if (instance == null) {
            instance = new UserRepository(email);
        }

        return instance;
    }

    public void setUserPreference(UserPreference userPreference, @Nullable Runnable onComplete)
            throws InputMismatchException {
        if (currentUser == null) {
            throw new InputMismatchException("Invalid email provided");
        }

        Map<String, Object> userUpdate = new HashMap<>();
        userUpdate.put("preferences", userPreference);

        currentUser.updateChildren(userUpdate).addOnCompleteListener((task) -> {
            if (onComplete == null) {
                return;
            }
            onComplete.run();
        });
    }

    public void getUserPreference(@Nullable Consumer<UserPreference> onComplete)
            throws InputMismatchException {
        if (currentUser == null) {
            throw new InputMismatchException("Invalid email provided");
        }

        currentUser.child("preferences").get().addOnCompleteListener(task -> {
            if (onComplete == null || !task.isComplete()) {
                return;
            }

            DataSnapshot snapshot = task.getResult();
            if (!snapshot.exists()) {
                return;
            }

            UserPreference userPreference = snapshot.getValue(UserPreference.class);
            onComplete.accept(userPreference);
        });
    }

    public void setRecipes(RecipeType recipeType, ArrayList<Integer> recipeIdList,
                           @Nullable Runnable onComplete)
            throws InputMismatchException {
        if (currentUser == null) {
            throw new InputMismatchException("Invalid email provided");
        }

        Map<String, Object> userUpdate = new HashMap<>();
        userUpdate.put(recipeType.value, recipeIdList);

        currentUser.updateChildren(userUpdate).addOnCompleteListener((task) -> {
            if (onComplete == null) {
                return;
            }
            onComplete.run();
        });
    }

    public void getRecipes(RecipeType recipeType, @Nullable Consumer<ArrayList<Integer>> onComplete)
            throws InputMismatchException {
        if (currentUser == null) {
            throw new InputMismatchException("Invalid email provided");
        }

        currentUser.child(recipeType.value).get().addOnCompleteListener(task -> {
            if (onComplete == null || !task.isComplete()) {
                return;
            }

            DataSnapshot snapshot = task.getResult();
            if (!snapshot.exists()) {
                return;
            }

            GenericTypeIndicator<ArrayList<Integer>> resultType =
                    new GenericTypeIndicator<ArrayList<Integer>>() {
                    };
            ArrayList<Integer> recipeIds = snapshot.getValue(resultType);

            onComplete.accept(recipeIds);
        });
    }

    public void addRecipe(RecipeType recipeType, int recipeId, @Nullable Runnable onComplete)
            throws InputMismatchException {
        if (currentUser == null) {
            throw new InputMismatchException("Invalid email provided");
        }

        String key = currentUser.child(recipeType.value).push().getKey();
        Map<String, Object> userUpdate = new HashMap<>();

        userUpdate.put("/" + recipeType.value + "/" + key, recipeId);
        currentUser.updateChildren(userUpdate);
    }
}

