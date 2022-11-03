package com.example.foodapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodapp.R;
import com.example.foodapp.viewmodel.RecipeViewModel;

import java.util.function.Consumer;

public class SearchFragment extends Fragment {

    private SearchView searchSv;
    private ConstraintLayout layout;
    private RecipeViewModel recipeViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (ConstraintLayout) inflater.inflate(R.layout.fragment_search, container, false);
        recipeViewModel = new ViewModelProvider(requireActivity()).get(RecipeViewModel.class);
        bindView();
        bindAction();
        return layout;
    }

    private Consumer<String> onSearchListener;

    public void setOnSearchListener(Consumer<String> onSearchListener) {
        this.onSearchListener = onSearchListener;
    }

    private void bindView() {
        searchSv = layout.findViewById(R.id.searchSv);
    }

    private void bindAction() {
        searchSv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recipeViewModel.setSearchParams(query, null, null, null, null);
                if (onSearchListener != null) onSearchListener.accept(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}