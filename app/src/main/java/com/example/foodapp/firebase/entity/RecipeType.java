package com.example.foodapp.firebase.entity;

public enum RecipeType {
    LIKED("liked"),
    SAVED("saved");

    public final String value;

    RecipeType(String value) {
        this.value = value;
    }
}
