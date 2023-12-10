package com.example.foodapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.foodapp.R;
import com.example.foodapp.repository.model.Recipe;
import com.example.foodapp.ui.adapters.SavedRecipeAdapter;
import com.example.foodapp.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SavedRecipesFragment extends Fragment {

    private final SavedRecipeAdapter adapter = new SavedRecipeAdapter();
    private RecyclerView savedRecipesRv;
    private ConstraintLayout layout;
    private UserViewModel userViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (ConstraintLayout) inflater.inflate(R.layout.fragment_saved_recipes, container, false);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        bindView();
        setItemClickListener();
        return layout;
    }

    private void bindView() {
        savedRecipesRv = layout.findViewById(R.id.savedRecipesRv);
        userViewModel.getSavedRecipeListLiveData().observe(getViewLifecycleOwner(),
                list -> adapter.submitList(new ArrayList<>(list))
        );
        savedRecipesRv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        savedRecipesRv.setAdapter(adapter);
    }

    public void setItemClickListener() {
        adapter.setItemOnClickListener(recipe -> {
            List<Recipe> recipeList = userViewModel.getSavedRecipeListLiveData().getValue();
            if (recipeList != null) {
                int position = recipeList.indexOf(recipe);
                RecipeFragment recipeFragment = new RecipeFragment();
                recipeFragment.setRecipeList(new ArrayList<>(recipeList), position == -1 ? null :
                        position);
                getParentFragmentManager().beginTransaction().add(R.id.fragment_container,
                        recipeFragment).addToBackStack(null).commit();
            }
        });
    }
}