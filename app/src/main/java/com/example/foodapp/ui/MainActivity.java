package com.example.foodapp.ui;

import android.accounts.NetworkErrorException;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;

import com.example.foodapp.R;
import com.example.foodapp.firebase.FirebaseRepository;
import com.example.foodapp.firebase.entity.User;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.FirebaseApp;

import java.util.InputMismatchException;

public class MainActivity extends AppCompatActivity {

    private GestureDetectorCompat gestureDetectorCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFirebase();

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.container, new RecipeFragment())
//                    .commitNow();
//        }

        testApi();
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
    }

    private void testApi() {
        FirebaseRepository firebaseRepository = FirebaseRepository.getRealtimeDatabaseInstance();

        try {
            firebaseRepository.fetchUser("testing@test.com", (foundUser) -> {
                if(foundUser == null) {
                    return;
                }

                Log.e("MINH", foundUser.toString());
            });

        } catch (InputMismatchException exception) {
            Log.e("MINH", "Create new user failed");
        }

    }
}