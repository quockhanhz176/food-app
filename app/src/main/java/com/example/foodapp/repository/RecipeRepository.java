package com.example.foodapp.repository;

import android.app.Application;

import androidx.annotation.Nullable;

import com.example.foodapp.repository.api.RecipeApiService;
import com.example.foodapp.repository.api.enums.Cuisine;
import com.example.foodapp.repository.api.enums.Flavor;
import com.example.foodapp.repository.api.enums.FoodTag;
import com.example.foodapp.repository.api.enums.Intolerance;
import com.example.foodapp.repository.api.enums.MealType;
import com.example.foodapp.repository.model.RecipeResponse;

import java.util.Collection;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Single;

public class RecipeRepository {

    private Application application;

    public RecipeRepository(Application application) {
        this.application = application;
    }

    public Single<RecipeResponse> searchRecipe(
            String query,
            @Nullable Collection<Cuisine> cuisines,
            @Nullable Collection<Flavor> flavors,
            @Nullable Collection<Intolerance> intolerances,
            @Nullable Collection<MealType> mealTypes,
            int nextPageNumber
    ) {
        String flavorString = flavors == null ? "" :
                flavors.stream().map(this::transformTag).collect(Collectors.joining(" "));
        String cuisineString = cuisines == null ? null :
                cuisines.stream().map(this::transformTag).collect(Collectors.joining(","));
        String intoleranceString = intolerances == null ? null :
                intolerances.stream().map(this::transformTag).collect(Collectors.joining(","));
        String mealTypeString = mealTypes == null ? null :
                mealTypes.stream().map(this::transformTag).collect(Collectors.joining(","));

        RecipeApiService apiService = RecipeApiService.getInstance();
        com.example.foodapp.repository.api.repository.RecipeRepository recipeRepository = apiService.getRecipeRepository();
        return recipeRepository.complexSearch(
                query + " " + flavorString,
                nextPageNumber - 1,
                1,
                cuisineString,
                intoleranceString,
                mealTypeString
        ).firstOrError().map(
                response -> new RecipeResponse(response.getResults(), nextPageNumber)
        );
    }

    private String transformTag(FoodTag tag) {
        return tag.getValue();
    }
}
