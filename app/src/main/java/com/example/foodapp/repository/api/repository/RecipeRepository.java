package com.example.foodapp.repository.api.repository;

import com.example.foodapp.AppConfig;
import com.example.foodapp.repository.model.RecipeResult;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface RecipeRepository {
    @Headers({
            "Accept: application/json",
            "User-Agent: FoodApp",
            "x-api-key: " + AppConfig.RECIPE_API_KEY,
    })
    @GET("recipes/complexSearch?instructionsRequired=true" +
            "&addRecipeInformation=true")
    Observable<RecipeResult> complexSearch(
            @Query("query") String query,
            @Query("offset") int offset,
            @Query("number") int number,
            @Query("cuisine") String cuisine,
            @Query("intolerances") String intolerances,
            @Query("type") String type
    );

}
