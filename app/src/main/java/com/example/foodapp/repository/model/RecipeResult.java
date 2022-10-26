package com.example.foodapp.repository.model;

import java.util.List;

public class RecipeResult {
    private List<Recipe> results;
    private int number;
    private int offset;
    private int totalResults;

    public List<Recipe> getResults() {
        return results;
    }

    public int getNumber() {
        return number;
    }

    public int getOffset() {
        return offset;
    }

    public int getTotalResults() {
        return totalResults;
    }
}
