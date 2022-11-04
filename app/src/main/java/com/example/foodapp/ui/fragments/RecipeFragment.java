package com.example.foodapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagingData;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.foodapp.R;
import com.example.foodapp.firebase.entity.RecipeType;
import com.example.foodapp.repository.model.Recipe;
import com.example.foodapp.ui.adapters.RecipeAdapter;
import com.example.foodapp.viewmodel.RecipeViewModel;
import com.example.foodapp.viewmodel.UserViewModel;

import java.util.List;
import java.util.stream.Collectors;

public class RecipeFragment extends Fragment {

    private ViewPager2 surfVp2;
    private RecipeViewModel recipeViewModel;
    private UserViewModel userViewModel;
    private int surfVp2lastPosition = -1;
    private RecipeAdapter adapter;
    private MotionLayout.TransitionListener recipeDetailTransitionListener;
    Observer<PagingData<Recipe>> observer;

    private boolean isSavedRecipeList = false;
    private List<Recipe> savedRecipeList;
    private Integer itemPosition;

    {
        adapter = new RecipeAdapter(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {
                surfVp2.setUserInputEnabled(false);
                if (recipeDetailTransitionListener != null)
                    recipeDetailTransitionListener.onTransitionStarted(motionLayout, startId, endId);
            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {
                if (recipeDetailTransitionListener != null)
                    recipeDetailTransitionListener.onTransitionChange(motionLayout, startId, endId, progress);
            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                surfVp2.setUserInputEnabled(true);
                if (recipeDetailTransitionListener != null)
                    recipeDetailTransitionListener.onTransitionCompleted(motionLayout, currentId);
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {
                if (recipeDetailTransitionListener != null)
                    recipeDetailTransitionListener.onTransitionTrigger(motionLayout, triggerId, positive, progress);
            }
        });
    }

    public void setRecipeDetailTransitionListener(MotionLayout.TransitionListener recipeDetailTransitionListener) {
        this.recipeDetailTransitionListener = recipeDetailTransitionListener;
    }

    public void setRecipeList(List<Recipe> list, @Nullable Integer position) {
        isSavedRecipeList = true;
        savedRecipeList = list;
        itemPosition = position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        surfVp2 = (ViewPager2) inflater.inflate(R.layout.fragment_recipe, container, false);
        setupViewModels();
        setupViewPager();

        return surfVp2;
    }

    private void setupViewModels() {
        recipeViewModel = new ViewModelProvider(requireActivity()).get(RecipeViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    }

    private void setupViewPager() {
        userViewModel.getLikedRecipeListLiveData().observe(getViewLifecycleOwner(), adapter::setLikedRecipeIdList);
        userViewModel.getSavedRecipeListLiveData().observe(
                getViewLifecycleOwner(),
                list -> adapter.setSavedRecipeIdList(list.stream().map(Recipe::getId).collect(Collectors.toList()))
        );
        adapter.setOnLikeButtonCheckedChange((recipe, newCheckState) -> {
            if (newCheckState) {
                userViewModel.addNewRecipe(RecipeType.LIKED, recipe);
            } else {
                userViewModel.removeRecipe(RecipeType.LIKED, recipe);
            }
        });
        adapter.setOnSaveButtonCheckedChange((recipe, newCheckState) -> {
            if (newCheckState) {
                userViewModel.addNewRecipe(RecipeType.SAVED, recipe);
            } else {
                userViewModel.removeRecipe(RecipeType.SAVED, recipe);
            }
        });

        surfVp2.setAdapter(adapter);
        surfVp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            private RecipeAdapter.RecipeViewHolder lastViewHolder;
            private final RecyclerView rv = (RecyclerView) surfVp2.getChildAt(0);

            @Override
            public void onPageScrolled(int position, float positionOffset, @Px int positionOffsetPixels) {
                if (positionOffset != 0) {
                    if (lastViewHolder == null)
                        lastViewHolder = (RecipeAdapter.RecipeViewHolder) rv.findViewHolderForAdapterPosition(surfVp2lastPosition);
                } else {
                    if (lastViewHolder != null) {
                        lastViewHolder.resetLayout();
                    }
                    lastViewHolder = null;
                    surfVp2lastPosition = position;
                }
            }
        });

        if (!isSavedRecipeList) {
            if (observer == null) {
                observer = (PagingData<Recipe> recipePagingData) -> adapter.submitData(requireActivity().getLifecycle(), recipePagingData);
                recipeViewModel.getRecipeLiveData().observe(
                        getViewLifecycleOwner(), observer
                );
            }
        } else {
            adapter.submitData(getLifecycle(), PagingData.from(savedRecipeList));
            if (itemPosition != null) {
                surfVp2.post(() -> surfVp2.setCurrentItem(itemPosition, false));
            }
        }
    }
}