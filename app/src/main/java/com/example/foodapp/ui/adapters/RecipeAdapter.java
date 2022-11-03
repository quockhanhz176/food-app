package com.example.foodapp.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.text.HtmlCompat;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.repository.model.Recipe;
import com.example.foodapp.ui.customviews.NonScrollableListView;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeAdapter extends PagingDataAdapter<Recipe, RecipeAdapter.RecipeViewHolder> {

    private final MotionLayout.TransitionListener transitionListener;

    public RecipeAdapter(MotionLayout.TransitionListener transitionListener) {
        super(new RecipeComparator());
        this.transitionListener = transitionListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MotionLayout view =
                (MotionLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_layout, parent, false);
        if (transitionListener != null) {
            view.setTransitionListener(transitionListener);
        }
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = getItem(position);
        if (recipe != null) {
            holder.setRecipe(recipe);
        }
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        private ImageView foodIv;
        private TextView foodTitleTv;
        private TextView readyTimeContentTv;
        private TextView servingsContent;
        private TextView healthContentScore;
        private MaterialCardView likeButtonMcv;
        private MaterialCardView saveButtonMcv;
        private ImageView likeButtonIv;
        private ImageView saveButtonIv;

        private ScrollView detailSv;
        private TextView svTitle;
        private TextView svSummaryTv;
        private TextView svInstructionTitleTv;
        private NonScrollableListView svInstructionLv;
        private InstructionAdapter adapter;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            bindView();
        }

        public void setRecipe(Recipe recipe) {
            foodTitleTv.setText(recipe.getTitle());
            Picasso.get().load(recipe.getImage()).into(foodIv);
            readyTimeContentTv.setText(
                    itemView.getResources().getQuantityString(
                            R.plurals.recipe_layout_subtitle_ready_time_content,
                            recipe.getReadyInMinutes(),
                            recipe.getReadyInMinutes()
                    )
            );
            servingsContent.setText(
                    itemView.getResources().getQuantityString(
                            R.plurals.recipe_layout_subtitle_servings_content,
                            recipe.getServings(),
                            recipe.getServings()
                    )
            );
            healthContentScore.setText(
                    itemView.getResources().getString(
                            R.string.recipe_layout_subtitle_health_score_content,
                            recipe.getHealthScore()
                    )
            );

            svTitle.setText(recipe.getTitle());
            svSummaryTv.setText(
                    HtmlCompat.fromHtml(recipe.getSummary(), HtmlCompat.FROM_HTML_MODE_LEGACY)
            );
            if (recipe.getAnalyzedInstructions().isEmpty()) {
                adapter.setData(new ArrayList<>());
                svInstructionTitleTv.setText("");
            } else {
                adapter.setData(recipe.getAnalyzedInstructions().get(0).getSteps());
                svInstructionTitleTv.setText(
                        itemView.getResources().getString(R.string.recipe_sv_instruction_title)
                );
            }
            svInstructionLv.requestLayout();
            svInstructionLv.invalidate();
        }

        private void bindView() {
            foodIv = itemView.findViewById(R.id.foodSiv);
            foodTitleTv = itemView.findViewById(R.id.foodTitleTv);
            readyTimeContentTv = itemView.findViewById(R.id.readyTimeContentTv);
            servingsContent = itemView.findViewById(R.id.servingContentTv);
            healthContentScore = itemView.findViewById(R.id.healthScoreContentTv);
            likeButtonMcv = itemView.findViewById(R.id.likeButtonMcv);
            saveButtonMcv = itemView.findViewById(R.id.saveButtonMcv);
            likeButtonIv = itemView.findViewById(R.id.likeButtonIv);
            saveButtonIv = itemView.findViewById(R.id.saveButtonIv);

            detailSv = itemView.findViewById(R.id.detailSv);
            svTitle = itemView.findViewById(R.id.svTitleTv);
            svSummaryTv = itemView.findViewById(R.id.svSummaryTv);
            svInstructionTitleTv = itemView.findViewById(R.id.svInstructionTitleTv);
            svInstructionLv = itemView.findViewById(R.id.svInstructionLv);
            adapter = new InstructionAdapter();
            svInstructionLv.setAdapter(adapter);

            likeButtonMcv.setOnClickListener(view -> {
                likeButtonIv.setSelected(!likeButtonIv.isSelected());
            });
            saveButtonMcv.setOnClickListener(view -> {
                saveButtonIv.setSelected(!saveButtonIv.isSelected());
            });
        }

        public void resetLayout() {
            ((MotionLayout) itemView).jumpToState(R.id.introductionCs);
            detailSv.scrollTo(0, 0);

        }

        public void setUserEnabled(boolean value) {
            ((MotionLayout) itemView).enableTransition(R.id.detailT, value);
//            detailSv.setOnTouchListener((view, motionEvent) -> false);
        }
    }

    private static class RecipeComparator extends DiffUtil.ItemCallback<Recipe> {

        @Override
        public boolean areItemsTheSame(@NonNull Recipe oldItem, @NonNull Recipe newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Recipe oldItem, @NonNull Recipe newItem) {
            return oldItem.equals(newItem);
        }
    }
}
