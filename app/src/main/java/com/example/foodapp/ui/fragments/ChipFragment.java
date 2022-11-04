package com.example.foodapp.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodapp.R;
import com.example.foodapp.firebase.entity.UserPreference;
import com.example.foodapp.repository.enums.Cuisine;
import com.example.foodapp.repository.enums.Flavor;
import com.example.foodapp.repository.enums.FoodTag;
import com.example.foodapp.repository.enums.Intolerance;
import com.example.foodapp.repository.enums.MealType;
import com.example.foodapp.viewmodel.UserViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;

import java.util.ArrayList;


public class ChipFragment extends Fragment {

    private ArrayList<Cuisine> selectedCuisines;
    private ArrayList<Flavor> selectedFlavors;
    private ArrayList<Intolerance> selectedIntolerances;
    private ArrayList<MealType> selectedMealTypes;

    private ChipGroup cuisineGroup;
    private ChipGroup flavorGroup;
    private ChipGroup intoleranceGroup;
    private ChipGroup mealTypeGroup;

    private Button submitBtn;

    private Runnable onSubmitPreferences;

    private UserViewModel viewModel;

    private boolean shouldShowUserPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        selectedCuisines = new ArrayList<>();
        selectedFlavors = new ArrayList<>();
        selectedIntolerances = new ArrayList<>();
        selectedMealTypes = new ArrayList<>();

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        UserPreference userPreference = viewModel.getUserPreferenceLiveData().getValue();
        if (userPreference != null) {
            shouldShowUserPreference = false;
        } else {
            shouldShowUserPreference = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preferences, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindingView(view);
        bindingAction();

        setupChipGroups(view);
    }

    private void bindingView(View view) {
        cuisineGroup = view.findViewById(R.id.cuisine_group);
        flavorGroup = view.findViewById(R.id.flavor_group);
        intoleranceGroup = view.findViewById(R.id.intolerance_group);
        mealTypeGroup = view.findViewById(R.id.type_group);

        submitBtn = view.findViewById(R.id.submitBtn);
    }

    private void bindingAction() {
        submitBtn.setOnClickListener(this::handleSubmitPreferences);
    }

    private void handleSubmitPreferences(View view) {
        UserPreference userPreference = new UserPreference(selectedCuisines, selectedFlavors,
                selectedIntolerances, selectedMealTypes);
        viewModel.setUserPreferences(userPreference);

        onSubmitPreferences.run();
    }

    private void setupChipGroups(View view) {
        Cuisine[] cuisines = Cuisine.values();
        for (Cuisine cuisine : cuisines) {
            cuisineGroup.addView(generatePreferenceChip(cuisine, cuisineGroup));
        }

        Flavor[] flavors = Flavor.values();
        for (Flavor flavor : flavors) {
            flavorGroup.addView(generatePreferenceChip(flavor, flavorGroup));
        }

        Intolerance[] intolerances = Intolerance.values();
        for (Intolerance intolerance : intolerances) {
            intoleranceGroup.addView(generatePreferenceChip(intolerance, intoleranceGroup));
        }

        MealType[] mealTypes = MealType.values();
        for (MealType mealType : mealTypes) {
            mealTypeGroup.addView(generatePreferenceChip(mealType, mealTypeGroup));
        }
    }

    private Chip generatePreferenceChip(FoodTag foodTag, ChipGroup chipGroup) {
        Chip preferenceChip =
                (Chip) getLayoutInflater().inflate(R.layout.custom_chip_layout, chipGroup, false);

        preferenceChip.setText(foodTag.getValue());
        preferenceChip
                .setOnCheckedChangeListener(
                        (compoundButton, checked) -> handlePreferenceChipSelect(foodTag, checked)
                );

        return preferenceChip;
    }

    // TODO: Refactor this with currying if possible
    private void handlePreferenceChipSelect(FoodTag foodTag, boolean checked) {
        // Cuisines
        if (foodTag instanceof Cuisine) {
            if (checked) {
                selectedCuisines.add((Cuisine) foodTag);
            } else {
                selectedCuisines.remove(foodTag);
            }

            return;
        }

        // Flavors
        if (foodTag instanceof Flavor) {
            if (checked) {
                selectedFlavors.add((Flavor) foodTag);
            } else {
                selectedFlavors.remove(foodTag);
            }

            return;
        }

        // Intolerances
        if (foodTag instanceof Intolerance) {
            if (checked) {
                selectedIntolerances.add((Intolerance) foodTag);
            } else {
                selectedIntolerances.remove(foodTag);
            }

            return;
        }

        // Meal types
        if (foodTag instanceof MealType) {
            if (checked) {
                selectedMealTypes.add((MealType) foodTag);
            } else {

                selectedMealTypes.remove(foodTag);
            }
        }
    }

    public ArrayList<Cuisine> getSelectedCuisines() {
        return selectedCuisines;
    }

    public ArrayList<Flavor> getSelectedFlavors() {
        return selectedFlavors;
    }

    public ArrayList<Intolerance> getSelectedIntolerances() {
        return selectedIntolerances;
    }

    public ArrayList<MealType> getSelectedMealTypes() {
        return selectedMealTypes;
    }

    public void setSelectedCuisines(ArrayList<Cuisine> selectedCuisines) {
        this.selectedCuisines = selectedCuisines;
    }

    public void setSelectedFlavors(ArrayList<Flavor> selectedFlavors) {
        this.selectedFlavors = selectedFlavors;
    }

    public void setSelectedIntolerances(ArrayList<Intolerance> selectedIntolerances) {
        this.selectedIntolerances = selectedIntolerances;
    }

    public void setSelectedMealTypes(ArrayList<MealType> selectedMealTypes) {
        this.selectedMealTypes = selectedMealTypes;
    }

    public void setOnSubmitPreferences(Runnable onSubmitPreferences) {
        this.onSubmitPreferences = onSubmitPreferences;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}