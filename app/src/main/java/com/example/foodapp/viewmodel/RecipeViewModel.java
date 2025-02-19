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
import com.example.foodapp.repository.repositories.RecipeRepository;
import com.example.foodapp.repository.enums.Cuisine;
import com.example.foodapp.repository.enums.Flavor;
import com.example.foodapp.repository.enums.Intolerance;
import com.example.foodapp.repository.enums.MealType;
import com.example.foodapp.repository.model.Recipe;

import java.util.Collection;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlinx.coroutines.CoroutineScope;

@HiltViewModel
public class RecipeViewModel extends AndroidViewModel {

    public LiveData<PagingData<Recipe>> getRecipeLiveData() {
        return recipeMutableLiveData;
    }
    @Inject
    public RecipeRepository recipeRepository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MutableLiveData<PagingData<Recipe>> recipeMutableLiveData =
            new MutableLiveData<>();

    @Inject
    public RecipeViewModel(@NonNull Application application) {
        super(application);
        setDefaultSearchParams();
    }

    public void setDefaultSearchParams() {
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

        Pager<Integer, Recipe> pager = new Pager<>(new PagingConfig(10, 5, false, 10, 20),
                () -> recipeRepository.getRecipePagingSource(query,
                        cuisines,
                        flavors,
                        intolerances,
                        mealTypes
                )
        );
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        Flowable<PagingData<Recipe>> recipeFlowable = PagingRx.getFlowable(pager);
        compositeDisposable.add(PagingRx.cachedIn(recipeFlowable.subscribeOn(Schedulers.io()),
                viewModelScope
        ).subscribe(recipeMutableLiveData::postValue));
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}