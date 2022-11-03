package com.example.foodapp;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.foodapp.firebase.UserRepository;
import com.example.foodapp.firebase.entity.RecipeType;
import com.example.foodapp.firebase.entity.UserPreference;
import com.example.foodapp.repository.enums.Cuisine;
import com.example.foodapp.repository.enums.Flavor;
import com.example.foodapp.repository.enums.Intolerance;
import com.example.foodapp.repository.enums.MealType;
import com.google.firebase.FirebaseApp;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UserRepositoryTest {
    private final UserRepository userRepository;

    public UserRepositoryTest() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FirebaseApp.initializeApp(appContext);

        String testUser = "testing@test.com";
        this.userRepository = UserRepository.getCurrentUser(testUser);
    }

    @Test
    public void testConnection() {
        assertNotEquals(userRepository, null);
    }

    @Test
    public void testSetUserPreference() {
        ArrayList<Cuisine> cuisines = new ArrayList<>();
        ArrayList<Flavor> flavors = new ArrayList<>();
        ArrayList<Intolerance> intolerances = new ArrayList<>();
        ArrayList<MealType> mealTypes = new ArrayList<>();

        cuisines.add(Cuisine.Vietnamese);
        flavors.add(Flavor.Salty);
        intolerances.add(Intolerance.Gluten);
        mealTypes.add(MealType.Dessert);

        UserPreference userPreference = new UserPreference(
                cuisines, flavors, intolerances, mealTypes
        );

        userRepository.setUserPreference(userPreference, task -> assertTrue(task.isSuccessful()));
    }

    @Test
    public void testGetUserPreference() {
        userRepository.getUserPreference(userPreference -> assertNotEquals(userPreference, null));
    }

    @Test
    public void testSetRecipes() {
        ArrayList<Integer> recipeIds = new ArrayList<>();
        recipeIds.add(662363);

        userRepository.setRecipes(RecipeType.LIKED, recipeIds, task -> assertTrue(task.isSuccessful()));
    }

    @Test
    public void testGetRecipes() {
        userRepository.getRecipes(RecipeType.LIKED, recipeIds -> {
            assertNotEquals(recipeIds, null);
            assertNotEquals(recipeIds.size(), 0);
        });
    }

    @Test
    public void testAddRecipe() {
        userRepository.addRecipe(RecipeType.LIKED, 647372, task -> assertTrue(task.isSuccessful()));
    }

    @Test
    public void testDeleteRecipeById() {
        userRepository.deleteRecipeById(RecipeType.LIKED, 647372, task -> assertTrue(task.isSuccessful()));
    }
}
