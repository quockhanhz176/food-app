package com.example.foodapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.paging.PagingData;

import com.example.foodapp.R;
import com.example.foodapp.repository.model.Recipe;
import com.example.foodapp.ui.customviews.CustomMotionLayout;

public class HomeFragment extends Fragment {

    private CustomMotionLayout layout;
    private SearchFragment searchFragment;
    private RecipeFragment recipeFragment = new RecipeFragment();
    private SearchFragment.OnUserMenuItemClickListener onUserMenuItemClickListener;
    private Observer<PagingData<Recipe>> recipeObserver;

    public void setOnUserMenuItemClickListener(@NonNull SearchFragment.OnUserMenuItemClickListener onUserMenuItemClickListener) {
        this.onUserMenuItemClickListener = onUserMenuItemClickListener;
        if (searchFragment != null) {
            searchFragment.setOnUserMenuItemCLickListenerList(onUserMenuItemClickListener);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (CustomMotionLayout) inflater.inflate(R.layout.fragment_home, container, false);
        bindView();
        setupViewPager();

        return layout;
    }

    private void setupViewPager() {
        recipeFragment.setRecipeDetailTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {
                setMotionEnable(false);
            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                if (currentId == R.id.introductionCs)
                    setMotionEnable(true);
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {

            }

            private void setMotionEnable(boolean value) {
                layout.setAbsorptionMode(value ?
                        CustomMotionLayout.MotionAbsorptionMode.DRAG_DOWN :
                        CustomMotionLayout.MotionAbsorptionMode.NONE
                );
            }
        });
    }

    private void bindView() {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.recipeFcv, recipeFragment).commit();
        searchFragment = (SearchFragment) getChildFragmentManager().findFragmentById(R.id.searchFcv);
        if (searchFragment != null) {
            searchFragment.setOnSearchListener(query -> layout.transitionToState(R.id.notSearchCs));
            if (onUserMenuItemClickListener != null) {
                searchFragment.setOnUserMenuItemCLickListenerList(onUserMenuItemClickListener);
            }
            searchFragment.setOnUserMenuItemCLickListenerList(new SearchFragment.OnUserMenuItemClickListener() {
                @Override
                public void onHomeClick() {
                    layout.transitionToState(R.id.notSearchCs);
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        recipeFragment = null;
        searchFragment = null;
        layout = null;
    }
}