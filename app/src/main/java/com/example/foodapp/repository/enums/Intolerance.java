package com.example.foodapp.repository.enums;

public enum Intolerance implements FoodTag {
    Dairy("Dairy"),
    Egg("Egg"),
    Gluten("Gluten"),
    Grain("Grain"),
    Peanut("Peanut"),
    Seafood("Seafood"),
    Sesame("Sesame"),
    Shellfish("Shellfish"),
    Soy("Soy"),
    TreeNut("TreeNut"),
    Wheat("Wheat");

    private final String value;

    Intolerance(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
