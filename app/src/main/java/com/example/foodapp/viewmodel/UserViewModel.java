package com.example.foodapp.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foodapp.firebase.UserRepository;
import com.example.foodapp.firebase.entity.RecipeType;
import com.example.foodapp.firebase.entity.UserPreference;
import com.example.foodapp.repository.RecipeRepository;
import com.example.foodapp.repository.model.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository userRepository;
    private final MutableLiveData<UserPreference> userPreferenceLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Integer>> likedRecipeIdListLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Recipe>> savedRecipeListLiveData = new MutableLiveData<>(new ArrayList<>());
    private final RecipeRepository recipeRepository = RecipeRepository.getInstance();
    private List<Recipe> savedRecipeList = new ArrayList<>();

    public UserViewModel(@NonNull Application application) {
        super(application);

        FirebaseUser authInfo = FirebaseAuth.getInstance().getCurrentUser();
        if (authInfo == null || authInfo.getEmail() == null || authInfo.getEmail().trim().equals("")) {
            userRepository = null;
            return;
        }

        userRepository = UserRepository.getCurrentUser(authInfo.getEmail());
        fetchRecipes(RecipeType.LIKED);
        fetchRecipes(RecipeType.SAVED);
        fetchUserPreferences();
    }

    public void fetchUserPreferences() {
        if (userRepository == null) {
            return;
        }
        userRepository.getUserPreference(userPreferenceLiveData::postValue);
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
        userRepository.getRecipes(recipeType, recipeType == RecipeType.LIKED ?
                likedRecipeIdListLiveData::postValue :
                recipeList -> recipeList.forEach(
                        id -> recipeRepository.getRecipeById(id)
                                .doOnError(error -> Log.e("FoodApp", error.toString()))
                                .subscribe(recipe -> {
                                            savedRecipeList.add(recipe);
                                            savedRecipeListLiveData.postValue(savedRecipeList);
                                        }
                                )
                )
        );
    }

    public void setRecipes(RecipeType recipeType, List<Recipe> recipeList) {
        if (userRepository == null) {
            return;
        }
        userRepository.setRecipes(
                recipeType,
                recipeList.stream().map(Recipe::getId).collect(Collectors.toList()),
                null
        );
        if (recipeType == RecipeType.SAVED) {
            savedRecipeList = recipeList;
            savedRecipeListLiveData.postValue(savedRecipeList);
        } else {
            likedRecipeIdListLiveData.postValue(recipeList.stream().map(Recipe::getId).collect(Collectors.toList()));
        }
    }

    public void addNewRecipe(RecipeType recipeType, Recipe recipe) {
        if (userRepository == null) {
            return;
        }
        if (recipeType == RecipeType.SAVED) {
            if (savedRecipeList.contains(recipe)) return;
        } else {
            List<Integer> likedIds = likedRecipeIdListLiveData.getValue();
            if (likedIds != null && likedIds.contains(recipe.getId())) return;
        }
        try {
            userRepository.addRecipe(recipeType, recipe.getId(), null);
            if (recipeType == RecipeType.SAVED) {
                savedRecipeList.add(recipe);
                savedRecipeListLiveData.postValue(savedRecipeList);
            } else {
                List<Integer> likedList = likedRecipeIdListLiveData.getValue();
                likedList.add(recipe.getId());
                likedRecipeIdListLiveData.postValue(likedList);
            }
        } catch (Exception e) {
            Log.e("FoodApp", e.toString());
        }
    }

    public void removeRecipe(RecipeType recipeType, Recipe recipe) {
        if (userRepository == null) {
            return;
        }
        try {
            userRepository.deleteRecipeById(recipeType, recipe.getId(), task -> {
                if (task.isSuccessful() && recipeType == RecipeType.SAVED) {
                    savedRecipeList.remove(recipe);
                    savedRecipeListLiveData.postValue(savedRecipeList);
                } else {
                    List<Integer> likedList = likedRecipeIdListLiveData.getValue();
                    if (likedList != null) {
                        likedList.removeIf(id -> id == recipe.getId());
                        likedRecipeIdListLiveData.postValue(likedList);
                    }
                }
            });
        } catch (Exception e) {
            Log.e("FoodApp", e.toString());
        }

    }

    public LiveData<UserPreference> getUserPreferenceLiveData() {
        return userPreferenceLiveData;
    }

    public LiveData<List<Integer>> getLikedRecipeListLiveData() {
        return likedRecipeIdListLiveData;
    }

    public LiveData<List<Recipe>> getSavedRecipeListLiveData() {
        return savedRecipeListLiveData;
    }
}
