package com.example.foodapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import com.example.foodapp.repository.RecipePagingSource;
import com.example.foodapp.repository.RecipeRepository;
import com.example.foodapp.repository.model.Recipe;

import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.CoroutineScope;

public class RecipeViewModel extends AndroidViewModel {

    private final RecipeRepository recipeRepository;

    public Flowable<PagingData<Recipe>> recipeFlowable;

    public RecipeViewModel(@NonNull Application application) {
        super(application);
        recipeRepository = new RecipeRepository(application);
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        Pager<Integer, Recipe> pager = new Pager<>(
                new PagingConfig(20),
                () -> new RecipePagingSource(recipeRepository, "")
        );
        recipeFlowable = PagingRx.getFlowable(pager);
        PagingRx.cachedIn(recipeFlowable, viewModelScope);
    }
}