package com.example.foodapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagingData;

import com.example.foodapp.R;
import com.example.foodapp.repository.model.Recipe;
import com.example.foodapp.ui.customviews.CustomMotionLayout;
import com.example.foodapp.viewmodel.AuthViewModel;
import com.example.foodapp.viewmodel.RecipeViewModel;
import com.example.foodapp.viewmodel.UserViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private CustomMotionLayout layout;
    private SearchFragment searchFragment;
    private RecipeFragment recipeFragment = new RecipeFragment();
    private AuthViewModel authViewModel;
    private UserViewModel userViewModel;
    private RecipeViewModel recipeViewModel;
    private Runnable onLogOutListener;

    public void setOnLogOutListener(Runnable onLogOutListener) {
        this.onLogOutListener = onLogOutListener;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        recipeViewModel = new ViewModelProvider(requireActivity()).get(RecipeViewModel.class);
    }

    private final SearchFragment.OnUserMenuItemClickListener onUserMenuItemClickListener =
            new SearchFragment.OnUserMenuItemClickListener() {
                @Override
                public void onSavedRecipesClick() {
                    getParentFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, new SavedRecipesFragment())
                            .addToBackStack(null).commit();
                }

                @Override
                public void onLogOutClick() {
                    if (onLogOutListener != null) {
                        onLogOutListener.run();
                    }
                }

                @Override
                public void onHomeClick() {
                    recipeViewModel.setDefaultSearchParams();
                }

                @Override
                public void onUserSettingsClick() {
                    getParentFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, new UserSettingFragment())
                            .addToBackStack(null).commit();
                }
            };

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        layout = (CustomMotionLayout) inflater.inflate(R.layout.fragment_home, container, false);
        bindView();
        setupViewPager();

        return layout;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        authViewModel = null;
        userViewModel = null;
        recipeViewModel = null;
    }

    private void setupViewPager() {
        recipeFragment.setRecipeDetailTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {
                setMotionEnable(false);
            }

            @Override
            public void onTransitionChange(
                    MotionLayout motionLayout, int startId, int endId, float progress
            ) {}

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                if (currentId == R.id.introductionCs) setMotionEnable(true);
            }

            @Override
            public void onTransitionTrigger(
                    MotionLayout motionLayout, int triggerId, boolean positive, float progress
            ) {}

            private void setMotionEnable(boolean value) {
                layout.setAbsorptionMode(value ? CustomMotionLayout.MotionAbsorptionMode.DRAG_DOWN :
                        CustomMotionLayout.MotionAbsorptionMode.NONE);
            }
        });
    }

    private void bindView() {
        getChildFragmentManager().beginTransaction().replace(R.id.recipeFcv, recipeFragment)
                .commit();

        searchFragment =
                (SearchFragment) getChildFragmentManager().findFragmentById(R.id.searchFcv);
        if (searchFragment != null) {
            searchFragment.setOnSearchListener(query -> layout.transitionToState(R.id.notSearchCs));
            searchFragment.setOnUserMenuItemCLickListenerList(onUserMenuItemClickListener);
            searchFragment.setOnUserMenuItemCLickListenerList(new SearchFragment.OnUserMenuItemClickListener() {
                @Override
                public void onHomeClick() {
                    layout.transitionToState(R.id.notSearchCs);
                }
            });
        }
    }
}