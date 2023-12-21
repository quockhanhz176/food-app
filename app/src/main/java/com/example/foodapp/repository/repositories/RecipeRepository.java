package com.example.foodapp.repository.repositories;

import androidx.annotation.Nullable;

import com.example.foodapp.repository.IRecipeService;
import com.example.foodapp.repository.RecipePagingSource;
import com.example.foodapp.repository.enums.Cuisine;
import com.example.foodapp.repository.enums.Flavor;
import com.example.foodapp.repository.enums.Intolerance;
import com.example.foodapp.repository.enums.MealType;
import com.example.foodapp.repository.model.Recipe;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

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

    public Single<Recipe> getRecipeById(int recipeId) {
        return recipeService.getRecipeById(recipeId).subscribeOn(Schedulers.io()).firstOrError();
    }
}
