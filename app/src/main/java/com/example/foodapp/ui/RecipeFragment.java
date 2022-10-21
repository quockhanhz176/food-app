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
import com.example.foodapp.ui.adapter.RecipeAdapter;
import com.example.foodapp.viewmodel.RecipeViewModel;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class RecipeFragment extends Fragment {

    private ViewPager2 surfVp2;
    private View layout;
    private RecipeViewModel recipeViewModel;
    private final CompositeDisposable disposable = new CompositeDisposable();

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
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                surfVp2.setUserInputEnabled(true);
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {

            }
        });

        surfVp2.setAdapter(adapter);
        surfVp2.setUserInputEnabled(false);
        surfVp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            private RecipeAdapter.RecipeViewHolder lastViewHolder;
            private int lastPosition = -1;
            private final RecyclerView rv = (RecyclerView) surfVp2.getChildAt(0);

            @Override
            public void onPageScrolled(int position, float positionOffset, @Px int positionOffsetPixels) {
                if (positionOffset != 0) {
                    if (lastViewHolder == null)
                        lastViewHolder = (RecipeAdapter.RecipeViewHolder) rv.findViewHolderForAdapterPosition(lastPosition);
                } else {
                    if (lastViewHolder != null) {
                        lastViewHolder.resetLayout();
                    }
                    lastViewHolder = null;
                    lastPosition = position;
                }
            }
        });
        disposable.add(
                recipeViewModel.recipeFlowable.subscribe(
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