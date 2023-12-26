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

import com.example.foodapp.repository.repositories.RecipeRepository;
import com.example.foodapp.repository.enums.Cuisine;
import com.example.foodapp.repository.enums.Flavor;
import com.example.foodapp.repository.enums.Intolerance;
import com.example.foodapp.repository.enums.MealType;
import com.example.foodapp.repository.model.Recipe;

import java.util.Collection;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import kotlinx.coroutines.CoroutineScope;

@HiltViewModel
public class RecipeViewModel extends AndroidViewModel {
    public RecipeRepository recipeRepository;
    private CompositeDisposable disposables = new CompositeDisposable();
    private final BehaviorSubject<PagingData<Recipe>> recipePagingDataSubject =
            BehaviorSubject.create();


    @Inject
    public RecipeViewModel(@NonNull Application application, RecipeRepository recipeRepository) {
        super(application);
        this.recipeRepository = recipeRepository;
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
        disposables.dispose();
        disposables = new CompositeDisposable();

        Pager<Integer, Recipe> pager = new Pager<>(new PagingConfig(10, 5, false, 10, 20),
                () -> recipeRepository.getRecipePagingSource(query,
                        cuisines,
                        flavors,
                        intolerances,
                        mealTypes
                )
        );
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        Observable<PagingData<Recipe>> recipeFlowable = PagingRx.getObservable(pager);
        disposables.add(recipeFlowable.subscribeOn(Schedulers.io())
                .subscribe(recipePagingDataSubject::onNext, recipePagingDataSubject::onError));
    }

    public BehaviorSubject<PagingData<Recipe>> getRecipePagingDataSubject() {
        return recipePagingDataSubject;
    }
}