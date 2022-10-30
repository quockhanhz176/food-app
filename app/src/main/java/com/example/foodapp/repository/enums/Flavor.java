package com.example.foodapp.repository.enums;

public enum Flavor implements FoodTag {
    Sweet("Sweet"),
    Salty("Salty"),
    Sour("Sour"),
    Bitter("Bitter");

    private final String value;

    Flavor(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
