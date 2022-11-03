package com.example.foodapp.ui;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityMainBinding;
import com.example.foodapp.repository.model.Recipe;
import com.example.foodapp.ui.fragments.HomeFragment;
import com.example.foodapp.ui.fragments.LoginFragment;
import com.example.foodapp.ui.fragments.RecipeFragment;
import com.example.foodapp.ui.fragments.SavedRecipesFragment;
import com.example.foodapp.ui.fragments.SearchFragment;
import com.example.foodapp.ui.fragments.SignUpFragment;
import com.example.foodapp.ui.fragments.UserSettingFragment;
import com.example.foodapp.ui.util.Utils;
import com.example.foodapp.viewmodel.AuthViewModel;
import com.example.foodapp.viewmodel.RecipeViewModel;
import com.example.foodapp.viewmodel.UserViewModel;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GestureDetectorCompat gestureDetectorCompat;
    private ActivityMainBinding binding;
    private FirebaseAuth firebaseAuth;
    private UserViewModel userViewModel;
    private AuthViewModel authViewModel;
    private RecipeViewModel recipeViewModel;

    private final LoginFragment loginFragment = new LoginFragment();
    private final SignUpFragment signUpFragment = new SignUpFragment();
    private final HomeFragment homeFragment = new HomeFragment();
    private final SavedRecipesFragment savedRecipesFragment = new SavedRecipesFragment();
    private final UserSettingFragment userSettingFragment = new UserSettingFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupWindow();
        initFirebase();
        setupViewModel();
        setupFragments(savedInstanceState);
    }

    private void setupWindow() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
    }

    private void setupViewModel() {
        ViewModelProvider provider = new ViewModelProvider(this);
        userViewModel = provider.get(UserViewModel.class);
        authViewModel = provider.get(AuthViewModel.class);
        recipeViewModel = provider.get(RecipeViewModel.class);
    }

    private void setupFragments(Bundle savedInstanceState) {
        //login fragments
        loginFragment.setOnLoginSuccess(() -> {
            Utils.clearAllFragment(this);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment, RecipeFragment.class.getCanonicalName()).commit();
        });

        loginFragment.setShowSignUp(() -> {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, signUpFragment, LoginFragment.class.getCanonicalName()).addToBackStack(LoginFragment.class.getCanonicalName()).commit();
        });

        signUpFragment.setShowHome(() -> {
            Utils.clearAllFragment(this);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment, RecipeFragment.class.getCanonicalName()).commit();
        });

        if (firebaseAuth.getCurrentUser() != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
        } else {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, loginFragment).commit();
            }
        }

        //internal fragments
        homeFragment.setOnUserMenuItemClickListener(new SearchFragment.OnUserMenuItemClickListener() {
            @Override
            public void onSavedRecipesClick() {
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, savedRecipesFragment).addToBackStack(null).commit();
            }

            @Override
            public void onLogOutClick() {
                authViewModel.logout();
                Utils.clearAllFragment(MainActivity.this);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, loginFragment).commit();
            }

            @Override
            public void onHomeClick() {
                recipeViewModel.setDefaultSearchParams();
            }

            @Override
            public void onUserSettingsClick() {
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, userSettingFragment).addToBackStack(null).commit();
            }
        });

        savedRecipesFragment.setItemClickListener(recipe -> {
            List<Recipe> recipeList = userViewModel.getSavedRecipeListLiveData().getValue();
            if (recipeList != null) {
                int position = recipeList.indexOf(recipe);
                RecipeFragment recipeFragment = new RecipeFragment();
                recipeFragment.setRecipeList(new ArrayList<>(recipeList), position == -1 ? null : position);
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, recipeFragment).addToBackStack(null).commit();
            }
        });
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
    }
}