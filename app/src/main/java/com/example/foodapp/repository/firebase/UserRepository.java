package com.example.foodapp.repository.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.foodapp.repository.firebase.entity.RecipeType;
import com.example.foodapp.repository.firebase.entity.UserPreference;
import com.example.foodapp.viewmodel.utils.MD5Util;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserRepository {

    private final FirebaseDatabase database;

    public @Inject UserRepository(FirebaseDatabase firebaseDatabase) {
        this.database = firebaseDatabase;
    }

    public DatabaseReference getUser(String email) {
        String emailHash = MD5Util.md5Hex(email);
        if (emailHash == null) {
            return null;
        }

        return database.getReference("users").child(emailHash);
    }

    public void setUserPreference(
            @NotNull DatabaseReference user,
            UserPreference userPreference,
            @Nullable Consumer<Task> onComplete
    ) throws InputMismatchException {
        if (user == null) {
            throw new InputMismatchException("Parameter user must not be null");
        }

        Map<String, Object> userUpdate = new HashMap<>();
        userUpdate.put("preferences", userPreference);

        user.updateChildren(userUpdate).addOnCompleteListener((task) -> {
            if (onComplete == null) {
                return;
            }
            onComplete.accept(task);
        });
    }

    public void getUserPreference(
            @NotNull DatabaseReference user,
            @Nullable Consumer<UserPreference> onComplete
    ) throws InputMismatchException {
        if (user == null) {
            throw new InputMismatchException("Parameter user must not be null");
        }

        try {
            user.child("preferences").get().addOnCompleteListener(task -> {
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

    public void setRecipes(
            @NotNull DatabaseReference user,
            RecipeType recipeType,
            List<Integer> recipeIdList,
            @Nullable Consumer<Task> onComplete
    ) throws InputMismatchException {
        if (user == null) {
            throw new InputMismatchException("Parameter user must not be null");
        }

        Map<String, Object> userUpdate = new HashMap<>();
        userUpdate.put(recipeType.value, recipeIdList);

        user.updateChildren(userUpdate).addOnCompleteListener(onComplete::accept);
    }

    public void getRecipes(
            @NotNull DatabaseReference user,
            RecipeType recipeType,
            @Nullable Consumer<List<Integer>> onComplete
    ) throws InputMismatchException {
        if (user == null) {
            throw new InputMismatchException("Parameter user must not be null");
        }

        user.child(recipeType.value).get().addOnCompleteListener(task -> {
            if (onComplete == null || !task.isComplete()) {
                return;
            }

            DataSnapshot snapshot = task.getResult();
            if (!snapshot.exists()) {
                return;
            }

            GenericTypeIndicator<Map<String, Long>> resultType =
                    new GenericTypeIndicator<Map<String, Long>>() {};
            Map<String, Long> recipeIds = snapshot.getValue(resultType);
            List<Integer> recipeIdList =
                    recipeIds.values().stream().map(Long::intValue).collect(Collectors.toList());

            onComplete.accept(recipeIdList);
        });
    }


    public void addRecipe(
            @NotNull DatabaseReference user,
            RecipeType recipeType,
            int recipeId,
            @Nullable Consumer<Task> onComplete
    ) throws InputMismatchException {
        if (user == null) {
            throw new InputMismatchException("Parameter user must not be null");
        }

        Map<String, Object> userUpdate = new HashMap<>();

        userUpdate.put("/" + recipeType.value + "/" + recipeId, recipeId);
        user.updateChildren(userUpdate).addOnCompleteListener(task -> {
            if (onComplete == null) {
                return;
            }
            onComplete.accept(task);
        });
    }

    public void deleteRecipeById(
            @NotNull DatabaseReference user,
            RecipeType recipeType,
            int recipeId,
            @Nullable Consumer<Task> onComplete
    ) throws InputMismatchException {
        if (user == null) {
            throw new InputMismatchException("Parameter user must not be null");
        }

        Map<String, Object> userUpdate = new HashMap<>();

        userUpdate.put("/" + recipeType.value + "/" + recipeId, null);
        user.updateChildren(userUpdate).addOnCompleteListener(task -> {
            if (onComplete == null) {
                return;
            }
            onComplete.accept(task);
        });
    }
}

