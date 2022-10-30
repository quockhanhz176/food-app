package com.example.foodapp.repository.enums;

public enum Cuisine implements FoodTag {
    African("African"),
    American("American"),
    British("British"),
    Cajun("Cajun"),
    Caribbean("Caribbean"),
    Chinese("Chinese"),
    EasternEuropean("EasternEuropean"),
    European("European"),
    French("French"),
    German("German"),
    Greek("Greek"),
    Indian("Indian"),
    Irish("Irish"),
    Italian("Italian"),
    Japanese("Japanese"),
    Jewish("Jewish"),
    Korean("Korean"),
    LatinAmerican("LatinAmerican"),
    Mediterranean("Mediterranean"),
    Mexican("Mexican"),
    MiddleEastern("MiddleEastern"),
    Nordic("Nordic"),
    Southern("Southern"),
    Spanish("Spanish"),
    Thai("Thai"),
    Vietnamese("Vietnamese");

    private final String value;

    Cuisine(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}


