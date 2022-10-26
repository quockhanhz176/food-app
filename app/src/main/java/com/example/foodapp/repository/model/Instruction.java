package com.example.foodapp.repository.model;

import java.util.List;

public class Instruction {
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
}
