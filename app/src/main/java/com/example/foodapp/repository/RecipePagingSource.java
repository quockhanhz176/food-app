package com.example.foodapp.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.example.foodapp.repository.model.Recipe;
import com.example.foodapp.repository.model.RecipeResponse;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RecipePagingSource extends RxPagingSource<Integer, Recipe> {

    @NonNull
    private RecipeRepository repo;
    @NonNull
    private String query;

    public RecipePagingSource(@NonNull RecipeRepository repo, @NonNull String query) {
        this.repo = repo;
        this.query = query;
    }

    @NonNull
    @Override
    public Single<LoadResult<Integer, Recipe>> loadSingle(@NonNull LoadParams<Integer> loadParams) {

        // Start refresh at page 1 if undefined.
        Integer nextPageNumber = loadParams.getKey();
        if (nextPageNumber == null) {
            nextPageNumber = 1;
        }

        return repo.searchRecipe(query, nextPageNumber)
                .subscribeOn(Schedulers.io())
                .map(
                        response -> {
                            return toLoadResult(response);
                        }
                )
                .onErrorReturn(LoadResult.Error::new);
    }

    private LoadResult<Integer, Recipe> toLoadResult(
            @NonNull RecipeResponse response) {
        return new LoadResult.Page<>(
                response.getRecipeList(),
                null,
                response.getKey() + 1,
                LoadResult.Page.COUNT_UNDEFINED,
                LoadResult.Page.COUNT_UNDEFINED);
    }


    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Recipe> pagingState) {
        Integer anchorPosition = pagingState.getAnchorPosition();
        if (anchorPosition == null) {
            return null;
        }

        LoadResult.Page<Integer, Recipe> anchorPage = pagingState.closestPageToPosition(anchorPosition);
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
