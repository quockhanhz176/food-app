package com.example.foodapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.text.HtmlCompat;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.databinding.RecipeLayoutBinding;
import com.example.foodapp.repository.model.Recipe;
import com.example.foodapp.ui.customviews.NonScrollableRecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends PagingDataAdapter<Recipe, RecipeAdapter.RecipeViewHolder> {

    private final MotionLayout.TransitionListener transitionListener;
    private IOnRecipeButtonCheckedChangeListener onLikeButtonCheckedChange;
    private IOnRecipeButtonCheckedChangeListener onSaveButtonCheckedChange;
    private List<Integer> savedRecipeIdList = new ArrayList<>();
    private List<Integer> likedRecipeIdList = new ArrayList<>();
    private final List<RecipeViewHolder> viewHolderList = new ArrayList<>();

    public void setOnLikeButtonCheckedChange(IOnRecipeButtonCheckedChangeListener onLikeButtonCheckedChange) {
        this.onLikeButtonCheckedChange = onLikeButtonCheckedChange;
    }

    public void setOnSaveButtonCheckedChange(IOnRecipeButtonCheckedChangeListener onSaveButtonCheckedChange) {
        this.onSaveButtonCheckedChange = onSaveButtonCheckedChange;
    }

    public void setSavedRecipeIdList(List<Integer> savedRecipeIdList) {
        this.savedRecipeIdList = savedRecipeIdList;
        for (RecipeViewHolder holder : viewHolderList) {
            if (savedRecipeIdList.contains(holder.recipe.getId())) {
                holder.setSaved(true);
            }
        }
    }

    public void setLikedRecipeIdList(List<Integer> likedRecipeIdList) {
        this.likedRecipeIdList = likedRecipeIdList;
        for (RecipeViewHolder holder : viewHolderList) {
            if (likedRecipeIdList.contains(holder.recipe.getId())) {
                holder.setLiked(true);
            }
        }
    }

    public RecipeAdapter(MotionLayout.TransitionListener transitionListener) {
        super(new RecipeComparator());
        this.transitionListener = transitionListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecipeLayoutBinding binding = RecipeLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        if (transitionListener != null) {
            binding.getRoot().setTransitionListener(transitionListener);
        }
        RecipeViewHolder holder = new RecipeViewHolder(binding);
        viewHolderList.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = getItem(position);
        if (recipe != null) {
            holder.setRecipe(recipe);
        }
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        private Recipe recipe;
        private final RecipeLayoutBinding binding;

        private ImageView foodSiv;
        private TextView foodTitleTv;
        private TextView readyTimeContentTv;
        private TextView servingsContentTv;
        private TextView healthScoreContentTv;
        private MaterialCardView likeButtonMcv;
        private MaterialCardView saveButtonMcv;
        private ImageView likeButtonIv;
        private ImageView saveButtonIv;

        private ScrollView detailSv;
        private TextView svTitle;
        private TextView svSummaryTv;
        private TextView svInstructionTitleTv;
        private NonScrollableRecyclerView svInstructionRv;
        private InstructionAdapter adapter;

        public RecipeViewHolder(@NonNull RecipeLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            bindView();
        }

        public void setLiked(boolean value) {
            likeButtonIv.setSelected(value);
        }

        public void setSaved(boolean value) {
            saveButtonIv.setSelected(value);
        }

        public void setRecipe(Recipe recipe) {
            this.recipe = recipe;
            foodTitleTv.setText(recipe.getTitle());
            Picasso.get().load(recipe.getImage()).into(foodSiv);
            readyTimeContentTv.setText(
                    itemView.getResources().getQuantityString(
                            R.plurals.recipe_layout_subtitle_ready_time_content,
                            recipe.getReadyInMinutes(),
                            recipe.getReadyInMinutes()
                    )
            );
            servingsContentTv.setText(
                    itemView.getResources().getQuantityString(
                            R.plurals.recipe_layout_subtitle_servings_content,
                            recipe.getServings(),
                            recipe.getServings()
                    )
            );
            healthScoreContentTv.setText(
                    itemView.getResources().getString(
                            R.string.recipe_layout_subtitle_health_score_content,
                            recipe.getHealthScore()
                    )
            );
            if (likedRecipeIdList != null) {
                likeButtonIv.setSelected(likedRecipeIdList.contains(recipe.getId()));
            }
            if (savedRecipeIdList != null) {
                saveButtonIv.setSelected(savedRecipeIdList.contains(recipe.getId()));
            }

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
            svInstructionRv.requestLayout();
            svInstructionRv.postInvalidate();
        }

        private void bindView() {
            foodSiv = binding.foodSiv;
            foodTitleTv = binding.foodTitleTv;
            readyTimeContentTv = binding.readyTimeContentTv;
            servingsContentTv = binding.servingContentTv;
            healthScoreContentTv = binding.healthScoreContentTv;
            likeButtonMcv = binding.likeButtonMcv;
            saveButtonMcv = binding.saveButtonMcv;
            likeButtonIv = binding.likeButtonIv;
            saveButtonIv = binding.saveButtonIv;

            detailSv = binding.detailSv;
            svTitle = binding.svTitleTv;
            svSummaryTv = binding.svSummaryTv;
            svInstructionTitleTv = binding.svInstructionTitleTv;
            svInstructionRv = binding.svInstructionRv;

            adapter = new InstructionAdapter();
            svInstructionRv.setLayoutManager(new LinearLayoutManager(itemView.getContext()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            svInstructionRv.setAdapter(adapter);

            likeButtonMcv.setOnClickListener(view -> {
                boolean newState = !likeButtonIv.isSelected();
                likeButtonIv.setSelected(newState);
                if (onLikeButtonCheckedChange != null) {
                    onLikeButtonCheckedChange.onCheckedChange(recipe, newState);
                }
            });
            saveButtonMcv.setOnClickListener(view -> {
                boolean newState = !saveButtonIv.isSelected();
                saveButtonIv.setSelected(newState);
                if (onSaveButtonCheckedChange != null) {
                    onSaveButtonCheckedChange.onCheckedChange(recipe, newState);
                }
            });
        }

        public void resetLayout() {
            ((MotionLayout) itemView).jumpToState(R.id.introductionCs);
            detailSv.scrollTo(0, 0);

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

    public interface IOnRecipeButtonCheckedChangeListener {
        void onCheckedChange(Recipe recipe, boolean newCheckState);
    }
}
