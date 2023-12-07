package com.example.foodapp.repository.firebase.entity;

public enum RecipeType {
    LIKED("liked"),
    SAVED("saved");

    public final String value;

    RecipeType(String value) {
        this.value = value;
    }
}
