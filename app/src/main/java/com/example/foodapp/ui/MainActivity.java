package com.example.foodapp.ui;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityMainBinding;
import com.example.foodapp.repository.firebase.entity.UserPreference;
import com.example.foodapp.repository.model.Recipe;
import com.example.foodapp.ui.fragments.ChipFragment;
import com.example.foodapp.ui.fragments.HomeFragment;
import com.example.foodapp.ui.fragments.LoginFragment;
import com.example.foodapp.ui.fragments.RecipeFragment;
import com.example.foodapp.ui.fragments.SavedRecipesFragment;
import com.example.foodapp.ui.fragments.SearchFragment;
import com.example.foodapp.ui.fragments.SignUpFragment;
import com.example.foodapp.ui.fragments.UserSettingFragment;
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
//
//    private LoginFragment loginFragment = new LoginFragment();
//    private SignUpFragment signUpFragment = new SignUpFragment();
    private final HomeFragment homeFragment = new HomeFragment();
    private final SavedRecipesFragment savedRecipesFragment = new SavedRecipesFragment();
//    private final UserSettingFragment userSettingFragment = new UserSettingFragment();
//    private final ChipFragment preferenceFragment = new ChipFragment();

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
        LoginFragment loginFragment = setupLoginFragment();

        // temporarily disabling PreferenceFragment
//        preferenceFragment.setOnSubmitPreferences(() -> {
//            clearAllFragment();
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.fragment_container, new HomeFragment())
//                    .commit();
//        });

        if (firebaseAuth.getCurrentUser() != null) {
            showLoginSuccessFragment();
        } else if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new LoginFragment())
                    .commit();
        }

        // Internal fragments
        homeFragment.setOnUserMenuItemClickListener(new SearchFragment.OnUserMenuItemClickListener() {
            @Override
            public void onSavedRecipesClick() {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, savedRecipesFragment)
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onLogOutClick() {
                authViewModel.logout();
                clearAllFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, loginFragment)
                        .commit();
                userViewModel.clearData();
            }

            @Override
            public void onHomeClick() {
                recipeViewModel.setDefaultSearchParams();
            }

            @Override
            public void onUserSettingsClick() {
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                        new UserSettingFragment()).addToBackStack(null).commit();
            }
        });

        savedRecipesFragment.setItemClickListener(recipe -> {
            List<Recipe> recipeList = userViewModel.getSavedRecipeListLiveData().getValue();
            if (recipeList != null) {
                int position = recipeList.indexOf(recipe);
                RecipeFragment recipeFragment = new RecipeFragment();
                recipeFragment.setRecipeList(new ArrayList<>(recipeList), position == -1 ? null :
                        position);
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                        recipeFragment).addToBackStack(null).commit();
            }
        });
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void clearAllFragment() {
        try {
            FragmentManager manager = getSupportFragmentManager();
            for (int i = 0; i < manager.getBackStackEntryCount(); i++) {
                manager.popBackStack();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LoginFragment setupLoginFragment() {
        LoginFragment loginFragment = new LoginFragment();

        loginFragment.setOnLoginSuccess(() -> {
            Toast.makeText(this, "Login successfully. Have fun!", Toast.LENGTH_SHORT).show();
            showLoginSuccessFragment();
            userViewModel.initData();
        });

        loginFragment.setShowSignUp(() -> {
            SignUpFragment signUpFragment = setupSignUpFragment();
            getSupportFragmentManager()
                    .beginTransaction().add(R.id.fragment_container,
                            signUpFragment, LoginFragment.class.getCanonicalName())
                    .addToBackStack(LoginFragment.class.getCanonicalName())
                    .commit();
        });

        return loginFragment;
    }

    private SignUpFragment setupSignUpFragment() {
        SignUpFragment signUpFragment = new SignUpFragment();

        signUpFragment.setShowHome(() -> {
            clearAllFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, homeFragment,
                            ChipFragment.class.getCanonicalName())
                    .commit();
            Toast.makeText(this, "Thank you for signing up. Have fun!", Toast.LENGTH_LONG).show();
        });

        return signUpFragment;
    }

    private void showLoginSuccessFragment() {

        clearAllFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, homeFragment,
                        ChipFragment.class.getCanonicalName())
                .commit();
        userViewModel.getUserPreferenceLiveData().observe(this, new Observer<UserPreference>() {
            @Override
            public void onChanged(UserPreference userPreference) {
                if (userPreference != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, homeFragment)
                            .commit();
                    userViewModel.getUserPreferenceLiveData().removeObserver(this);
                }
            }
        });
    }
}