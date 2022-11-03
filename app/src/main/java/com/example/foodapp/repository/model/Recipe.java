package com.example.foodapp.repository.model;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class Recipe {
    private int id;
    private String title;
    private String image;
    private String imageType;
    private int readyInMinutes;
    private int servings;
    private int healthScore;
    private List<String> cuisines;
    private String summary;
    private List<AnalyzedInstruction> analyzedInstructions;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getImageType() {
        return imageType;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public int getServings() {
        return servings;
    }

    public int getHealthScore() {
        return healthScore;
    }

    public String getSummary() {
        return summary;
    }

    public List<String> getCuisines() {
        return cuisines;
    }

    public List<AnalyzedInstruction> getAnalyzedInstructions() {
        return analyzedInstructions;
    }

    @NonNull
    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", imageType='" + imageType + '\'' +
                ", readyInMinutes=" + readyInMinutes +
                ", servings=" + servings +
                ", healthScore=" + healthScore +
                ", cuisines=" + cuisines +
                ", summary='" + summary + '\'' +
                ", analyzedInstructions=" + analyzedInstructions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Recipe)) {
            return false;
        }
        Recipe recipe = (Recipe) o;
        return id == recipe.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, image, imageType, readyInMinutes, servings, healthScore,
                cuisines, analyzedInstructions);
    }
}
