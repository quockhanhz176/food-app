package com.example.foodapp.ui;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;

import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityMainBinding;
import com.example.foodapp.ui.fragments.HomeFragment;
import com.example.foodapp.ui.fragments.LoginFragment;
import com.example.foodapp.ui.fragments.RecipeFragment;
import com.example.foodapp.ui.fragments.SavedRecipesFragment;
import com.example.foodapp.ui.fragments.SearchFragment;
import com.example.foodapp.ui.fragments.SignUpFragment;
import com.example.foodapp.ui.util.Utils;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private GestureDetectorCompat gestureDetectorCompat;
    private ActivityMainBinding binding;
    private FirebaseAuth firebaseAuth;

    private final LoginFragment loginFragment = new LoginFragment();
    private final SignUpFragment signUpFragment = new SignUpFragment();
    private final HomeFragment homeFragment = new HomeFragment();
    private final SavedRecipesFragment savedRecipesFragment = new SavedRecipesFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupWindow();
        initFirebase();
        setupFragments(savedInstanceState);
    }

    private void setupWindow() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
    }

    private void setupFragments(Bundle savedInstanceState) {
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

        homeFragment.setOnUserMenuItemClickListener(new SearchFragment.OnUserMenuItemClickListener() {
            @Override
            public void onSavedRecipesClick() {
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, savedRecipesFragment).addToBackStack(null).commit();
            }
        });

        if (firebaseAuth.getCurrentUser() != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
        } else {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, loginFragment).commit();
            }
        }
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
    }
}