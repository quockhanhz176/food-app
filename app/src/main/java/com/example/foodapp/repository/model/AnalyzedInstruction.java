package com.example.foodapp.repository.model;

import androidx.annotation.NonNull;

import java.util.List;

public class AnalyzedInstruction {
    private String name;
    private List<Step> steps;

    public String getName() {
        return name;
    }

    public List<Step> getSteps() {
        return steps;
    }

    @NonNull
    @Override
    public String toString() {
        return "AnalyzedInstruction{" +
                "name='" + name + '\'' +
                ", steps=" + steps +
                '}';
    }
}
