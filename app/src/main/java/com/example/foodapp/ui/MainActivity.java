package com.example.foodapp.ui;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodapp.R;
import com.example.foodapp.firebase.entity.UserPreference;
import com.example.foodapp.viewmodel.UserViewModel;
import com.google.firebase.FirebaseApp;
import com.example.foodapp.databinding.ActivityMainBinding;
import com.example.foodapp.ui.home.HomeFragment;
import com.example.foodapp.ui.sign_up.SignUpFragment;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private GestureDetectorCompat gestureDetectorCompat;
    private ActivityMainBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initFirebase();

        testApi();

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        } else {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SignUpFragment())
                        .commit();
            }
        }
    }

    private void testApi() {
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        UserPreference userPreference = new UserPreference();

        userViewModel.setUserPreferences(userPreference);
        userViewModel.fetchUserPreferences();
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
    }
}