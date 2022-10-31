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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.foodapp.R;
import com.example.foodapp.ui.adapters.RecipeAdapter;
import com.example.foodapp.ui.customviews.CustomMotionLayout;
import com.example.foodapp.viewmodel.RecipeViewModel;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class RecipeFragment extends Fragment {

    private ViewPager2 surfVp2;
    private CustomMotionLayout layout;
    private RecipeViewModel recipeViewModel;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private int surfVp2lastPosition = -1;
    private SearchFragment searchFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        layout = (CustomMotionLayout) inflater.inflate(R.layout.fragment_recipe, container, false);
        recipeViewModel = new ViewModelProvider(requireActivity()).get(RecipeViewModel.class);
        bindView();
        setupLayout();
        setupViewPager();

        return layout;
    }

    private void setupLayout() {
        layout.addTransitionListener(new MotionLayout.TransitionListener() {
            private final RecyclerView rv = (RecyclerView) surfVp2.getChildAt(0);

            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {
                setMotionEnable(false);
            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                if (currentId != R.id.searchCs) {
                    setMotionEnable(true);
                }
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {

            }

            private void setMotionEnable(boolean value) {
                RecipeAdapter.RecipeViewHolder viewHolder = (RecipeAdapter.RecipeViewHolder) rv.findViewHolderForAdapterPosition(surfVp2lastPosition);
                if (viewHolder != null) {
                    viewHolder.setUserEnabled(value);
                    surfVp2.setUserInputEnabled(value);
                }
            }
        });
        searchFragment.setOnSearchListener(query -> layout.transitionToState(R.id.notSearchCs));
    }

    private void setupViewPager() {
        RecipeAdapter adapter = new RecipeAdapter(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {
                surfVp2.setUserInputEnabled(false);
                setMotionEnable(false);
            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                surfVp2.setUserInputEnabled(true);
                if (currentId == R.id.introductionCs)
                    setMotionEnable(true);
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {

            }

            private void setMotionEnable(boolean value) {
                layout.enableTransition(R.id.searchT, value);
                layout.setAbsorptionMode(value ?
                        CustomMotionLayout.MotionAbsorptionMode.DRAG_DOWN :
                        CustomMotionLayout.MotionAbsorptionMode.NONE
                );
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

//        recipeViewModel.setSearchParams("fish", null, null, Arrays.asList(Intolerance.Egg), Arrays.asList(MealType.MainCourse));
//        recipeViewModel.setSearchParams("", Collections.singletonList(Cuisine.Mexican), null, null, null);
        recipeViewModel.getRecipeLiveData().observe(
                getViewLifecycleOwner(),
                recipePagingData -> adapter.submitData(getLifecycle(), recipePagingData)
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        disposable.clear();
    }

    private void bindView() {
        surfVp2 = layout.findViewById(R.id.surfVp2);
        searchFragment = (SearchFragment) getChildFragmentManager().findFragmentById(R.id.searchFcv);
    }
}