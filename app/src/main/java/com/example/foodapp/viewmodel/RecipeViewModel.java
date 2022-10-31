package com.example.foodapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import com.example.foodapp.repository.RecipePagingSource;
import com.example.foodapp.repository.RecipeRepository;
import com.example.foodapp.repository.enums.Cuisine;
import com.example.foodapp.repository.enums.Flavor;
import com.example.foodapp.repository.enums.Intolerance;
import com.example.foodapp.repository.enums.MealType;
import com.example.foodapp.repository.model.Recipe;

import java.util.Collection;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import kotlinx.coroutines.CoroutineScope;

public class RecipeViewModel extends AndroidViewModel {

    public LiveData<PagingData<Recipe>> getRecipeLiveData() {
        return recipeMutableLiveData;
    }

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final RecipeRepository recipeRepository;

    private final MutableLiveData<PagingData<Recipe>> recipeMutableLiveData = new MutableLiveData<>();

    public RecipeViewModel(@NonNull Application application) {
        super(application);
        recipeRepository = new RecipeRepository(application);
        setSearchParams("", null, null, null, null);
    }

    public void setSearchParams(
            @NonNull String query,
            @Nullable Collection<Cuisine> cuisines,
            @Nullable Collection<Flavor> flavors,
            @Nullable Collection<Intolerance> intolerances,
            @Nullable Collection<MealType> mealTypes
    ) {
        compositeDisposable.clear();
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        Pager<Integer, Recipe> pager = new Pager<>(
                new PagingConfig(5),
                () -> new RecipePagingSource(recipeRepository, query, cuisines, flavors, intolerances, mealTypes)
        );
        Flowable<PagingData<Recipe>> recipeFlowable = PagingRx.getFlowable(pager);
        compositeDisposable.add(recipeFlowable.subscribe(
                recipeMutableLiveData::postValue
        ));
        PagingRx.cachedIn(recipeFlowable, viewModelScope);
    }
}