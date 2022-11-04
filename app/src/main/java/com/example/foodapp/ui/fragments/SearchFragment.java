package com.example.foodapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MenuCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodapp.R;
import com.example.foodapp.viewmodel.RecipeViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SearchFragment extends Fragment {

    private SearchView searchSv;
    private ImageView userIconIv;
    private ConstraintLayout layout;
    private RecipeViewModel recipeViewModel;
    private List<OnUserMenuItemClickListener> onUserMenuItemCLickListenerList = new ArrayList<>();

    public void setOnUserMenuItemCLickListenerList(@NonNull OnUserMenuItemClickListener onUserMenuItemCLickListenerList) {
        this.onUserMenuItemCLickListenerList.add(onUserMenuItemCLickListenerList);
    }

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
        userIconIv = layout.findViewById(R.id.userIconIv);
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
        userIconIv.setOnClickListener(view -> {
                    PopupMenu popup = new PopupMenu(getContext(), view);
                    popup.getMenuInflater().inflate(R.menu.user_menu, popup.getMenu());
                    MenuCompat.setGroupDividerEnabled(popup.getMenu(), true);
                    popup.show();
                    popup.setOnMenuItemClickListener(menuItem -> {
                        switch (menuItem.getItemId()) {
                            case R.id.homeI:
                                onUserMenuItemCLickListenerList.forEach(OnUserMenuItemClickListener::onHomeClick);
                                return true;
                            case R.id.savedRecipesI:
                                onUserMenuItemCLickListenerList.forEach(OnUserMenuItemClickListener::onSavedRecipesClick);
                                return true;
                            case R.id.userSettingsI:
                                onUserMenuItemCLickListenerList.forEach(OnUserMenuItemClickListener::onUserSettingsClick);
                                return true;
                            case R.id.logOutI:
                                onUserMenuItemCLickListenerList.forEach(OnUserMenuItemClickListener::onLogOutClick);
                                return true;
                            default:
                                return false;
                        }
                    });
                }
        );
    }

    public static class OnUserMenuItemClickListener {
        public void onHomeClick() {
        }

        public void onSavedRecipesClick() {
        }

        public void onUserSettingsClick() {
        }

        public void onLogOutClick() {
        }
    }
}