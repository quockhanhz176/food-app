package com.example.foodapp.ui;

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
import com.example.foodapp.repository.enums.Cuisine;
import com.example.foodapp.ui.adapter.RecipeAdapter;
import com.example.foodapp.viewmodel.RecipeViewModel;

import java.util.Collections;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class RecipeFragment extends Fragment {

    private final CompositeDisposable disposable = new CompositeDisposable();
    private ViewPager2 surfVp2;
    private View layout;
    private RecipeViewModel recipeViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_recipe, container, false);
        recipeViewModel = new ViewModelProvider(getActivity()).get(RecipeViewModel.class);
        bindView();
        setupViewPager();

        return layout;
    }

    private void setupViewPager() {
        RecipeAdapter adapter = new RecipeAdapter(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {
                surfVp2.setUserInputEnabled(false);
            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId,
                                           float progress) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                surfVp2.setUserInputEnabled(true);
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId,
                                            boolean positive, float progress) {

            }
        });

        surfVp2.setAdapter(adapter);
        surfVp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            private final RecyclerView rv = (RecyclerView) surfVp2.getChildAt(0);
            private RecipeAdapter.RecipeViewHolder lastViewHolder;
            private int lastPosition = -1;

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       @Px int positionOffsetPixels) {
                if (positionOffset != 0) {
                    if (lastViewHolder == null) {
                        lastViewHolder =
                                (RecipeAdapter.RecipeViewHolder) rv.findViewHolderForAdapterPosition(lastPosition);
                    }
                } else {
                    if (lastViewHolder != null) {
                        lastViewHolder.resetLayout();
                    }
                    lastViewHolder = null;
                    lastPosition = position;
                }
            }
        });

//        recipeViewModel.setSearchParams("fish", null, null, Arrays.asList(Intolerance.Egg),
//        Arrays.asList(MealType.MainCourse));
        recipeViewModel.setSearchParams("", Collections.singletonList(Cuisine.Vietnamese), null,
                null, null);
        disposable.add(
                recipeViewModel.getRecipeFlowable().subscribe(
                        recipePagingData -> adapter.submitData(getLifecycle(), recipePagingData)
                )
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        disposable.clear();
    }

    private void bindView() {
        surfVp2 = layout.findViewById(R.id.surfVp2);
    }
}