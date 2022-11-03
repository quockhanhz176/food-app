package com.example.foodapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.repository.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.function.Consumer;

public class SavedRecipeAdapter extends ListAdapter<Recipe, SavedRecipeAdapter.SavedRecipeViewHolder> {

    public SavedRecipeAdapter() {
        super(new RecipeComparator());
    }

    private Consumer<Recipe> itemOnClickListener;

    public void setItemOnClickListener(Consumer<Recipe> itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    @NonNull
    @Override
    public SavedRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_recipe_layout, parent, false);
        return new SavedRecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedRecipeViewHolder holder, int position) {
        holder.setRecipe(getItem(position));
    }

    public class SavedRecipeViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageIv;
        private TextView titleTv;
        private Recipe recipe;

        public SavedRecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(
                    view -> {
                        if (itemOnClickListener != null) {
                            itemOnClickListener.accept(recipe);
                        }
                    }
            );
            bindView();
        }

        private void bindView() {
            imageIv = itemView.findViewById(R.id.itemImageIv);
            titleTv = itemView.findViewById(R.id.itemTitleTv);
        }

        public Recipe getRecipe() {
            return recipe;
        }

        public void setRecipe(Recipe recipe) {
            this.recipe = recipe;
            Picasso.get().load(recipe.getImage()).into(imageIv);
            titleTv.setText(recipe.getTitle());
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
