package com.example.foodapp.repository.model;

import androidx.annotation.NonNull;

import java.util.List;

public class RecipeResponse {
    private List<Recipe> recipeList;
    private int key;

    public RecipeResponse(List<Recipe> recipeList, int key) {
        this.recipeList = recipeList;
        this.key = key;
    }

    public List<Recipe> getRecipeList() {
        return recipeList;
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    @NonNull
    @Override
    public String toString() {
        return "RecipeResponse{" +
                "recipeList=" + recipeList +
                ", key=" + key +
                '}';
    }
}
