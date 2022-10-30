package com.example.foodapp.repository.api.enums;

public enum MealType implements FoodTag {
    MainCourse("main course"),
    SideDish("side dish"),
    Dessert("dessert"),
    Appetizer("appetizer"),
    Salad("salad"),
    Bread("bread"),
    Breakfast("breakfast"),
    Soup("soup"),
    Beverage("beverage"),
    Sauce("sauce");

    public final String value;

    MealType(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
