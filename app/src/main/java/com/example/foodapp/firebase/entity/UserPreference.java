package com.example.foodapp.firebase.entity;

import com.example.foodapp.repository.enums.Cuisine;
import com.example.foodapp.repository.enums.Flavor;
import com.example.foodapp.repository.enums.Intolerance;
import com.example.foodapp.repository.enums.MealType;

import java.io.Serializable;
import java.util.ArrayList;

public class UserPreference implements Serializable {
    private final ArrayList<Cuisine> cuisineList;
    private final ArrayList<Flavor> flavorList;
    private final ArrayList<Intolerance> intoleranceList;
    private final ArrayList<MealType> mealTypeList;

    public UserPreference() {
        this.cuisineList = new ArrayList<>();
        this.flavorList = new ArrayList<>();
        this.intoleranceList = new ArrayList<>();
        this.mealTypeList = new ArrayList<>();
    }

    public UserPreference(ArrayList<Cuisine> cuisineList, ArrayList<Flavor> flavorList,
                          ArrayList<Intolerance> intoleranceList, ArrayList<MealType> mealTypeList) {
        this.cuisineList = cuisineList;
        this.flavorList = flavorList;
        this.intoleranceList = intoleranceList;
        this.mealTypeList = mealTypeList;
    }

    public ArrayList<Cuisine> getCuisineList() {
        return cuisineList;
    }

    public ArrayList<Flavor> getFlavorList() {
        return flavorList;
    }

    public ArrayList<Intolerance> getIntoleranceList() {
        return intoleranceList;
    }

    public ArrayList<MealType> getMealTypeList() {
        return mealTypeList;
    }
}
