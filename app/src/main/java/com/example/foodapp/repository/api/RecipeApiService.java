package com.example.foodapp.repository.api;

import com.example.foodapp.repository.api.repository.RecipeRepository;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeApiService {
    private static final String RECIPE_BASE_URL = "https://api.spoonacular.com";

    private static RecipeApiService recipeApiService;
    private final RecipeRepository recipeRepository;

    private RecipeApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RECIPE_BASE_URL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build();

        recipeRepository = retrofit.create(RecipeRepository.class);
    }

    public static synchronized RecipeApiService getInstance() {
        if (recipeApiService == null) {
            recipeApiService = new RecipeApiService();
        }

        return recipeApiService;
    }

    public RecipeRepository getRecipeRepository() {
        return recipeRepository;
    }
}
