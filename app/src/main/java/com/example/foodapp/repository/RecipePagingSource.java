package com.example.foodapp.repository;

import static java.lang.Math.max;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.example.foodapp.repository.enums.Cuisine;
import com.example.foodapp.repository.enums.Flavor;
import com.example.foodapp.repository.enums.FoodTag;
import com.example.foodapp.repository.enums.Intolerance;
import com.example.foodapp.repository.enums.MealType;
import com.example.foodapp.repository.model.Recipe;
import com.example.foodapp.repository.model.RecipeResponse;
import com.example.foodapp.repository.network.IRecipeService;

import java.util.Collection;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RecipePagingSource extends RxPagingSource<Integer, Recipe> {

    private static final Integer STARTING_KEY = 1;
    @NonNull
    private final IRecipeService recipeService;
    @NonNull
    private final String query;
    private final Collection<Cuisine> cuisines;
    private final Collection<Flavor> flavors;
    private final Collection<Intolerance> intolerances;
    private final Collection<MealType> mealTypes;

    public RecipePagingSource(
            @NonNull IRecipeService recipeService,
            @NonNull String query,
            @Nullable Collection<Cuisine> cuisines,
            @Nullable Collection<Flavor> flavors,
            @Nullable Collection<Intolerance> intolerances,
            @Nullable Collection<MealType> mealTypes
    ) {
        this.recipeService = recipeService;
        this.query = query;
        this.cuisines = cuisines;
        this.flavors = flavors;
        this.intolerances = intolerances;
        this.mealTypes = mealTypes;
    }

    @NonNull
    @Override
    public Single<LoadResult<Integer, Recipe>> loadSingle(@NonNull LoadParams<Integer> loadParams) {

        // Start refresh at page 1 if undefined.
        Integer recipeId = loadParams.getKey();
        if (recipeId == null) {
            recipeId = 1;
        }
        int key = recipeId;

        String flavorString = flavors == null ? "" :
                flavors.stream().map(this::transformTag).collect(Collectors.joining(" "));
        String cuisineString = cuisines == null ? null :
                cuisines.stream().map(this::transformTag).collect(Collectors.joining(","));
        String intoleranceString = intolerances == null ? null :
                intolerances.stream().map(this::transformTag).collect(Collectors.joining(","));
        String mealTypeString = mealTypes == null ? null :
                mealTypes.stream().map(this::transformTag).collect(Collectors.joining(","));


        return recipeService.complexSearch(query + " " + flavorString,
                        recipeId - 1,
                        loadParams.getLoadSize(),
                        cuisineString,
                        intoleranceString,
                        mealTypeString
                ).firstOrError()
                .map(response -> toLoadResult(new RecipeResponse(response.getResults(), key),
                        loadParams
                )).onErrorReturn(LoadResult.Error::new).subscribeOn(Schedulers.io());
    }

    private String transformTag(FoodTag tag) {
        return tag.getValue();
    }

    private LoadResult<Integer, Recipe> toLoadResult(
            @NonNull RecipeResponse response, LoadParams<Integer> loadParams
    ) {
        return new LoadResult.Page<>(response.getRecipeList(),
                response.getKey() > STARTING_KEY ?
                        max(response.getKey() - loadParams.getLoadSize(), STARTING_KEY) : null,
                response.getKey() + loadParams.getLoadSize()
        );
    }


    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Recipe> pagingState) {
        Integer anchorPosition = pagingState.getAnchorPosition();
        if (anchorPosition == null) {
            return null;
        }

        LoadResult.Page<Integer, Recipe> anchorPage =
                pagingState.closestPageToPosition(anchorPosition);
        if (anchorPage == null) {
            return null;
        }

        Integer prevKey = anchorPage.getPrevKey();
        if (prevKey != null) {
            return prevKey + 1;
        }

        Integer nextKey = anchorPage.getNextKey();
        if (nextKey != null) {
            return nextKey - 1;
        }

        return null;
    }
}
