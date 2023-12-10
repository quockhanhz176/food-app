package com.example.foodapp.ui;

import static com.example.foodapp.ui.util.Utils.clearAllFragments;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodapp.R;
import com.example.foodapp.databinding.ActivityMainBinding;
import com.example.foodapp.ui.fragments.ChipFragment;
import com.example.foodapp.ui.fragments.HomeFragment;
import com.example.foodapp.ui.fragments.LoginFragment;
import com.example.foodapp.ui.fragments.SignUpFragment;
import com.example.foodapp.viewmodel.AuthViewModel;
import com.example.foodapp.viewmodel.UserViewModel;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    @Inject
    FirebaseAuth firebaseAuth;
    private UserViewModel userViewModel;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ActivityMainBinding.inflate(getLayoutInflater()).getRoot());

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
    }

    private void setupFragments(Bundle savedInstanceState) {
        if (firebaseAuth.getCurrentUser() != null) {
            showLoginSuccessFragment();
        } else if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, getLoginFragment()).commit();
        }
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
    }


    private LoginFragment getLoginFragment() {
        LoginFragment loginFragment = new LoginFragment();

        loginFragment.setOnLoginSuccess(() -> {
            Toast.makeText(this, "Login successfully. Have fun!", Toast.LENGTH_SHORT).show();
            showLoginSuccessFragment();
            userViewModel.initData();
        });

        loginFragment.setShowSignUp(() -> {
            SignUpFragment signUpFragment = setupSignUpFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    signUpFragment,
                    LoginFragment.class.getCanonicalName()
            ).addToBackStack(LoginFragment.class.getCanonicalName()).commit();
        });

        return loginFragment;
    }

    private SignUpFragment setupSignUpFragment() {
        SignUpFragment signUpFragment = new SignUpFragment();

        signUpFragment.setShowHome(() -> {
            clearAllFragments(getSupportFragmentManager());
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment(),
                    ChipFragment.class.getCanonicalName()
            ).commit();
            Toast.makeText(this, "Thank you for signing up. Have fun!", Toast.LENGTH_LONG).show();
        });

        return signUpFragment;
    }

    private void showLoginSuccessFragment() {
        clearAllFragments(getSupportFragmentManager());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, getHomeFragment()).commit();
    }

    private HomeFragment getHomeFragment() {
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setOnLogOutListener(() -> {
            authViewModel.logout();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, getLoginFragment()).commit();
            userViewModel.clearData();
        });
        return homeFragment;
    }
}