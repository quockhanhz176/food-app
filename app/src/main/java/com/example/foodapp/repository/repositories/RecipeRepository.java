package com.example.foodapp.repository.repositories;

import androidx.annotation.Nullable;

import com.example.foodapp.repository.RecipePagingSource;
import com.example.foodapp.repository.enums.Cuisine;
import com.example.foodapp.repository.enums.Flavor;
import com.example.foodapp.repository.enums.Intolerance;
import com.example.foodapp.repository.enums.MealType;
import com.example.foodapp.repository.model.Recipe;
import com.example.foodapp.repository.network.IRecipeService;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Observable;

@Singleton
public class RecipeRepository {
    private final IRecipeService recipeService;

    @Inject
    public RecipeRepository(IRecipeService recipeService) {
        this.recipeService = recipeService;
    }

    public RecipePagingSource getRecipePagingSource(
            String query,
            @Nullable Collection<Cuisine> cuisines,
            @Nullable Collection<Flavor> flavors,
            @Nullable Collection<Intolerance> intolerances,
            @Nullable Collection<MealType> mealTypes
    ) {
        return new RecipePagingSource(recipeService,
                query,
                cuisines,
                flavors,
                intolerances,
                mealTypes
        );
    }

    public Observable<Recipe> getRecipeById(int recipeId) {
        return recipeService.getRecipeById(recipeId);
    }
}
