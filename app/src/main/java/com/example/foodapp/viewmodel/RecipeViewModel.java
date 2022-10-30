package com.example.foodapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import com.example.foodapp.repository.RecipePagingSource;
import com.example.foodapp.repository.RecipeRepository;
import com.example.foodapp.repository.api.enums.Cuisine;
import com.example.foodapp.repository.api.enums.Flavor;
import com.example.foodapp.repository.api.enums.Intolerance;
import com.example.foodapp.repository.api.enums.MealType;
import com.example.foodapp.repository.model.Recipe;

import java.util.Collection;

import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.CoroutineScope;

public class RecipeViewModel extends AndroidViewModel {

    private final RecipeRepository recipeRepository;

    private Flowable<PagingData<Recipe>> recipeFlowable;

    public RecipeViewModel(@NonNull Application application) {
        super(application);
        recipeRepository = new RecipeRepository(application);
    }

    public Flowable<PagingData<Recipe>> getRecipeFlowable() {
        return recipeFlowable;
    }

    public void setSearchParams(
            @NonNull String query,
            @Nullable Collection<Cuisine> cuisines,
            @Nullable Collection<Flavor> flavors,
            @Nullable Collection<Intolerance> intolerances,
            @Nullable Collection<MealType> mealTypes
    ) {
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        Pager<Integer, Recipe> pager = new Pager<>(
                new PagingConfig(20),
                () -> new RecipePagingSource(recipeRepository, query, cuisines, flavors,
                        intolerances, mealTypes)
        );
        recipeFlowable = PagingRx.getFlowable(pager);
        PagingRx.cachedIn(recipeFlowable, viewModelScope);
    }
}