package com.example.foodapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.repository.model.Recipe;
import com.squareup.picasso.Picasso;

public class RecipeAdapter extends PagingDataAdapter<Recipe, RecipeAdapter.RecipeViewHolder> {

    public RecipeAdapter(MotionLayout.TransitionListener transitionListener) {
        super(new RecipeComparator());
        this.transitionListener = transitionListener;
    }

    private final MotionLayout.TransitionListener transitionListener;

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MotionLayout view = (MotionLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_layout, parent, false);
        if (transitionListener != null) {
            view.setTransitionListener(transitionListener);
        }
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = getItem(position);
        if (recipe != null) holder.setRecipe(recipe);
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        private ImageView foodIv;
        private TextView foodTitleTv;
        private TextView descriptionTv;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            bindView();
        }

        public void setRecipe(Recipe recipe) {
            foodTitleTv.setText(recipe.getTitle());
            descriptionTv.setText(recipe.toString());
            Picasso.get().load(recipe.getImage()).into(foodIv);
        }

        private void bindView() {
            foodIv = itemView.findViewById(R.id.foodSiv);
            foodTitleTv = itemView.findViewById(R.id.foodTitleTv);
            descriptionTv = itemView.findViewById(R.id.descriptionTv);
        }

        public void resetLayout() {
            ((MotionLayout) itemView).jumpToState(R.id.introductionCs);
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
