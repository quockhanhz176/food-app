package com.example.foodapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foodapp.firebase.UserRepository;
import com.example.foodapp.firebase.entity.RecipeType;
import com.example.foodapp.firebase.entity.UserPreference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class UserViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final MutableLiveData<UserPreference> recipePreferences;
    private final MutableLiveData<ArrayList<Integer>> likedRecipes;
    private final MutableLiveData<ArrayList<Integer>> savedRecipes;

    public UserViewModel(@NonNull Application application) {
        super(application);

        recipePreferences = new MutableLiveData<>();
        likedRecipes = new MutableLiveData<>();
        savedRecipes = new MutableLiveData<>();

        FirebaseUser authInfo = FirebaseAuth.getInstance().getCurrentUser();
        if (authInfo == null || authInfo.getEmail() == null || authInfo.getEmail().trim().equals(
                "")) {
            // TODO: Handle error here
            userRepository = null;
            return;
        }

        userRepository = UserRepository.getCurrentUser(authInfo.getEmail());
    }

    public void fetchUserPreferences() {
        if (userRepository == null) {
            return;
        }
        userRepository.getUserPreference(recipePreferences::postValue);
    }

    public void setUserPreferences(UserPreference userPreference) {
        if (userRepository == null) {
            return;
        }
        userRepository.setUserPreference(userPreference, null);
    }

    public void fetchRecipes(RecipeType recipeType) {
        if (userRepository == null) {
            return;
        }
        userRepository.getRecipes(recipeType, likedRecipes::postValue);
    }

    public void setRecipes(RecipeType recipeType, ArrayList<Integer> recipeIds) {
        if (userRepository == null) {
            return;
        }
        userRepository.setRecipes(recipeType, recipeIds, null);
    }

    public void addNewRecipe(RecipeType recipeType, int recipeId) {
        if (userRepository == null) {
            return;
        }
        userRepository.addRecipe(recipeType, recipeId, null);
    }

    public LiveData<UserPreference> getRecipePreferences() {
        return recipePreferences;
    }

    public LiveData<ArrayList<Integer>> getLikedRecipes() {
        return likedRecipes;
    }

    public LiveData<ArrayList<Integer>> getSavedRecipes() {
        return savedRecipes;
    }
}
