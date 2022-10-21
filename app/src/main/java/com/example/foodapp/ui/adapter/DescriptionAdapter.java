package com.example.foodapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.R;
import com.example.foodapp.repository.model.Recipe;

public class DescriptionAdapter extends RecyclerView.Adapter {

    public static class DescriptionViewHolder extends RecyclerView.ViewHolder {
        private final TextView descriptionTv;

        public DescriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            descriptionTv = itemView.findViewById(R.id.descriptionTv);
        }

        public void setDescription(String description) {
            descriptionTv.setText(description);
        }

    }

    public static class IntroductionViewHolder extends RecyclerView.ViewHolder {
        private final TextView introductionTv;

        public IntroductionViewHolder(@NonNull View itemView) {
            super(itemView);
            introductionTv = itemView.findViewById(R.id.introductionTv);
        }

        public void setIntroduction(String introduction) {
            introductionTv.setText(introduction);
        }

    }

    private Recipe recipe;

    public void bindRecipe(Recipe recipe) {
        this.recipe = recipe;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.introduction_view, parent, false);
                return new IntroductionViewHolder(view);
            }
            default: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.description_view, parent, false);
                return new DescriptionViewHolder(view);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position % 2 == 0) {
            ((IntroductionViewHolder) holder).setIntroduction(recipe.getName());
        } else {
            ((DescriptionViewHolder) holder).setDescription(recipe.getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }
}
