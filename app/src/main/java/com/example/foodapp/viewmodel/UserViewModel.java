package com.example.foodapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.foodapp.repository.firebase.UserRepository;
import com.example.foodapp.repository.firebase.entity.RecipeType;
import com.example.foodapp.repository.firebase.entity.UserPreference;
import com.example.foodapp.repository.repositories.RecipeRepository;
import com.example.foodapp.repository.model.Recipe;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

@HiltViewModel
public class UserViewModel extends ViewModel {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final FirebaseAuth firebaseAuth;
    private final BehaviorSubject<UserPreference> userPreferenceSubject = BehaviorSubject.create();
    private final BehaviorSubject<List<Integer>> likedRecipeIdListSubject =
            BehaviorSubject.create();
    private final BehaviorSubject<List<Recipe>> savedRecipeListSubject =
            BehaviorSubject.createDefault(new ArrayList<>());
    private List<Recipe> savedRecipeList = new ArrayList<>();
    private FirebaseUser firebaseUser;
    private DatabaseReference userReference;

    @Inject
    public UserViewModel(
            UserRepository userRepository,
            FirebaseAuth firebaseAuth,
            RecipeRepository recipeRepository
    ) {
        this.userRepository = userRepository;
        this.firebaseAuth = firebaseAuth;
        this.recipeRepository = recipeRepository;
        initData();
    }

    public void initData() {
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null || firebaseUser.getEmail() == null ||
                firebaseUser.getEmail().trim().equals("")) {
            return;
        }

        userReference = userRepository.getUser(firebaseUser.getEmail());
        fetchRecipes(RecipeType.LIKED);
        fetchRecipes(RecipeType.SAVED);
        fetchUserPreferences();
    }

    public void clearData() {
        userPreferenceSubject.onNext(null);
        likedRecipeIdListSubject.onNext(new ArrayList<>());
        savedRecipeList = new ArrayList<>();
        savedRecipeListSubject.onNext(savedRecipeList);
    }

    public void fetchUserPreferences() {
        if (userRepository == null) {
            return;
        }
        userRepository.getUserPreference(userReference, userPreferenceSubject::onNext);
    }

    public void setUserPreferences(UserPreference userPreference) {
        if (userRepository == null) {
            return;
        }
        userRepository.setUserPreference(userReference, userPreference, null);
    }

    public void fetchRecipes(RecipeType recipeType) {
        if (userRepository == null) {
            return;
        }
        userRepository.getRecipes(
                userReference,
                recipeType,
                recipeType == RecipeType.LIKED ? likedRecipeIdListSubject::onNext :
                        recipeList -> recipeList.forEach(id -> recipeRepository.getRecipeById(id)
                                .subscribe(recipe -> {
                                    savedRecipeList.add(recipe);
                                    savedRecipeListSubject.onNext(savedRecipeList);
                                }, e -> {}))
        );
    }

    public void setRecipes(RecipeType recipeType, List<Recipe> recipeList) {
        if (userRepository == null) {
            return;
        }
        userRepository.setRecipes(
                userReference,
                recipeType,
                recipeList.stream().map(Recipe::getId).collect(Collectors.toList()),
                null
        );
        if (recipeType == RecipeType.SAVED) {
            savedRecipeList = recipeList;
            savedRecipeListSubject.onNext(savedRecipeList);
        } else {
            likedRecipeIdListSubject.onNext(recipeList.stream().map(Recipe::getId)
                    .collect(Collectors.toList()));
        }
    }

    public void addNewRecipe(RecipeType recipeType, Recipe recipe) {
        if (userRepository == null) {
            return;
        }
        if (recipeType == RecipeType.SAVED) {
            if (savedRecipeList.contains(recipe)) return;
        } else {
            List<Integer> likedIds = likedRecipeIdListSubject.getValue();
            if (likedIds != null && likedIds.contains(recipe.getId())) return;
        }
        try {
            userRepository.addRecipe(userReference, recipeType, recipe.getId(), null);
            if (recipeType == RecipeType.SAVED) {
                savedRecipeList.add(recipe);
                savedRecipeListSubject.onNext(savedRecipeList);
            } else {
                List<Integer> likedList = likedRecipeIdListSubject.getValue();
                likedList.add(recipe.getId());
                likedRecipeIdListSubject.onNext(likedList);
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
            userRepository.deleteRecipeById(userReference, recipeType, recipe.getId(), task -> {
                if (task.isSuccessful() && recipeType == RecipeType.SAVED) {
                    savedRecipeList.remove(recipe);
                    savedRecipeListSubject.onNext(savedRecipeList);
                } else {
                    List<Integer> likedList = likedRecipeIdListSubject.getValue();
                    if (likedList != null) {
                        likedList.removeIf(id -> id == recipe.getId());
                        likedRecipeIdListSubject.onNext(likedList);
                    }
                }
            });
        } catch (Exception e) {
            Log.e("FoodApp", e.toString());
        }

    }

    public BehaviorSubject<UserPreference> getUserPreferenceSubject() {
        return userPreferenceSubject;
    }

    public BehaviorSubject<List<Integer>> getLikedRecipeSubject() {
        return likedRecipeIdListSubject;
    }

    public BehaviorSubject<List<Recipe>> getSavedRecipeListSubject() {
        return savedRecipeListSubject;
    }
}
