package com.example.foodapp.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.foodapp.firebase.entity.RecipeType;
import com.example.foodapp.firebase.entity.UserPreference;
import com.example.foodapp.viewmodel.utils.MD5Util;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

    public void setUserPreference(UserPreference userPreference,
                                  @Nullable Consumer<Task> onComplete)
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
            onComplete.accept(task);
        });
    }

    public void getUserPreference(@Nullable Consumer<UserPreference> onComplete)
            throws InputMismatchException {
        if (currentUser == null) {
            throw new InputMismatchException("Invalid email provided");
        }

        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setRecipes(RecipeType recipeType, List<Integer> recipeIdList,
                           @Nullable Consumer<Task> onComplete)
            throws InputMismatchException {
        if (onComplete == null || currentUser == null) {
            throw new InputMismatchException("Invalid email provided");
        }

        Map<String, Object> userUpdate = new HashMap<>();
        userUpdate.put(recipeType.value, recipeIdList);

        currentUser.updateChildren(userUpdate).addOnCompleteListener(onComplete::accept);
    }

    public void getRecipes(RecipeType recipeType, @Nullable Consumer<List<Integer>> onComplete)
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

            GenericTypeIndicator<Map<String, Long>> resultType =
                    new GenericTypeIndicator<Map<String, Long>>() {
                    };
            Map<String, Long> recipeIds = snapshot.getValue(resultType);
            List<Integer> recipeIdList = recipeIds.values().stream().map(Long::intValue).collect(Collectors.toList());

            onComplete.accept(recipeIdList);
        });
    }


    public void addRecipe(RecipeType recipeType, int recipeId, @Nullable Consumer<Task> onComplete)
            throws InputMismatchException {
        if (currentUser == null) {
            throw new InputMismatchException("Invalid email provided");
        }

        Map<String, Object> userUpdate = new HashMap<>();

        userUpdate.put("/" + recipeType.value + "/" + recipeId, recipeId);
        currentUser.updateChildren(userUpdate).addOnCompleteListener(task -> {
            if (onComplete == null) {
                return;
            }
            onComplete.accept(task);
        });
    }

    public void deleteRecipeById(
            RecipeType recipeType, int recipeId,
            @Nullable Consumer<Task> onComplete
    ) throws InputMismatchException {
        if (currentUser == null) {
            throw new InputMismatchException("Invalid email provided");
        }

        Map<String, Object> userUpdate = new HashMap<>();

        userUpdate.put("/" + recipeType.value + "/" + recipeId, null);
        currentUser.updateChildren(userUpdate).addOnCompleteListener(task -> {
            if (onComplete == null) {
                return;
            }
            onComplete.accept(task);
        });
    }
}

