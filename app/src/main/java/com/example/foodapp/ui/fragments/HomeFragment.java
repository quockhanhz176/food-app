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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import com.example.foodapp.R;
import com.example.foodapp.ui.customviews.CustomMotionLayout;
import com.example.foodapp.viewmodel.AuthViewModel;
import com.example.foodapp.viewmodel.RecipeViewModel;
import com.example.foodapp.viewmodel.UserViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private CustomMotionLayout layout;
    private final RecipeFragment recipeFragment = new RecipeFragment();
    private AuthViewModel authViewModel;
    private UserViewModel userViewModel;
    private RecipeViewModel recipeViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        recipeViewModel = new ViewModelProvider(requireActivity()).get(RecipeViewModel.class);
        if (userViewModel.getFirebaseUserSubject().getValue() == null) {
            NavHostFragment.findNavController(HomeFragment.this)
                    .navigate(HomeFragmentDirections.actionHomeToLoginGraph());
        }
    }

    private final SearchFragment.OnUserMenuItemClickListener onUserMenuItemClickListener =
            new SearchFragment.OnUserMenuItemClickListener() {
                @Override
                public void onSavedRecipesClick() {
                    NavHostFragment.findNavController(HomeFragment.this)
                            .navigate(HomeFragmentDirections.actionHomeToSaved());
                }

                @Override
                public void onLogOutClick() {
                    authViewModel.logout();
                    NavHostFragment.findNavController(HomeFragment.this)
                            .navigate(HomeFragmentDirections.actionHomeToLoginGraph());
                }

                @Override
                public void onHomeClick() {
                    recipeViewModel.setDefaultSearchParams();
                    layout.transitionToState(R.id.notSearchCs);
                }

                @Override
                public void onUserSettingsClick() {
                    NavHostFragment.findNavController(HomeFragment.this)
                            .navigate(HomeFragmentDirections.actionHomeToSettings());
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

        SearchFragment searchFragment =
                (SearchFragment) getChildFragmentManager().findFragmentById(R.id.searchFcv);
        if (searchFragment != null) {
            searchFragment.setOnSearchListener(query -> layout.transitionToState(R.id.notSearchCs));
            searchFragment.setOnUserMenuItemCLickListener(onUserMenuItemClickListener);
        }
    }
}