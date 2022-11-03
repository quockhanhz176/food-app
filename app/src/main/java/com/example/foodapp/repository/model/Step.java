package com.example.foodapp.repository.model;

import androidx.annotation.NonNull;

import java.util.List;

public class Step {
    private int number;
    private String step;
    private List<Ingredient> ingredients;
    private List<Equipment> equipment;

    public int getNumber() {
        return number;
    }

    public String getStep() {
        return step;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Equipment> getEquipment() {
        return equipment;
    }

    @NonNull
    @Override
    public String toString() {
        return "Step{" +
                "number=" + number +
                ", step='" + step + '\'' +
                ", ingredients=" + ingredients +
                ", equipment=" + equipment +
                '}';
    }
}
