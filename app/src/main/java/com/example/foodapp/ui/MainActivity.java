package com.example.foodapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;

import com.example.foodapp.R;
import com.example.foodapp.repository.api.RecipeApiService;
import com.example.foodapp.repository.api.enums.Cuisine;
import com.example.foodapp.repository.api.enums.Intolerance;
import com.example.foodapp.repository.api.enums.MealType;
import com.example.foodapp.repository.model.Recipe;
import com.example.foodapp.repository.api.repository.RecipeRepository;

public class MainActivity extends AppCompatActivity {

    private GestureDetectorCompat gestureDetectorCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new RecipeFragment())
                    .commitNow();
        }
    }
}