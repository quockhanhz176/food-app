package com.example.foodapp.repository.api.enums;

public enum Flavor implements FoodTag {
    Sweet("Sweet"),
    Salty("Salty"),
    Sour("Sour"),
    Bitter("Bitter"),
    Umami("Umami");

    public final String value;

    Flavor(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
