package com.example.foodapp.repository;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.foodapp.repository.enums.Cuisine;
import com.example.foodapp.repository.enums.Flavor;
import com.example.foodapp.repository.enums.FoodTag;
import com.example.foodapp.repository.enums.Intolerance;
import com.example.foodapp.repository.enums.MealType;
import com.example.foodapp.repository.model.Recipe;
import com.example.foodapp.repository.model.RecipeResponse;

import java.util.Collection;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeRepository {

    private static final String RECIPE_BASE_URL = "https://api.spoonacular.com";
    private final IRecipeRepository recipeRepository;

    private static RecipeRepository instance;

    public synchronized static RecipeRepository getInstance() {
        if (instance == null) {
            instance = new RecipeRepository();
        }

        return instance;
    }

    private RecipeRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RECIPE_BASE_URL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build();

        recipeRepository = retrofit.create(IRecipeRepository.class);
    }

    public Single<RecipeResponse> searchRecipe(
            String query,
            @Nullable Collection<Cuisine> cuisines,
            @Nullable Collection<Flavor> flavors,
            @Nullable Collection<Intolerance> intolerances,
            @Nullable Collection<MealType> mealTypes,
            int firstRecipeId,
            int size
    ) {
        String flavorString = flavors == null ? "" :
                flavors.stream().map(this::transformTag).collect(Collectors.joining(" "));
        String cuisineString = cuisines == null ? null :
                cuisines.stream().map(this::transformTag).collect(Collectors.joining(","));
        String intoleranceString = intolerances == null ? null :
                intolerances.stream().map(this::transformTag).collect(Collectors.joining(","));
        String mealTypeString = mealTypes == null ? null :
                mealTypes.stream().map(this::transformTag).collect(Collectors.joining(","));

        return recipeRepository.complexSearch(
                query + " " + flavorString,
                firstRecipeId - 1,
                size,
                cuisineString,
                intoleranceString,
                mealTypeString
        ).firstOrError().map(
                response -> new RecipeResponse(response.getResults(), firstRecipeId)
        );
    }

    public Single<Recipe> getRecipeById(int recipeId) {
        return recipeRepository.getRecipeById(recipeId)
                .subscribeOn(Schedulers.io())
                .firstOrError();
    }

    private String transformTag(FoodTag tag) {
        return tag.getValue();
    }
}
