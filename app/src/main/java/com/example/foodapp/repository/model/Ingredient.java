package com.example.foodapp.repository.model;

import androidx.annotation.NonNull;

public class Ingredient {
    private int id;
    private String name;
    private String localizedName;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocalizedName() {
        return localizedName;
    }

    @NonNull
    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", localizedName='" + localizedName + '\'' +
                '}';
    }
}
