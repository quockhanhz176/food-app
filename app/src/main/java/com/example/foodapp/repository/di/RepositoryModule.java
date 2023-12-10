package com.example.foodapp.repository.di;

import static com.example.foodapp.repository.Constants.RECIPE_BASE_URL;

import com.example.foodapp.repository.IRecipeService;
import com.example.foodapp.repository.firebase.FirebaseConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class RepositoryModule {

    @Singleton
    @Provides
    public static FirebaseDatabase provideFirebaseDatabase() {
        return FirebaseDatabase.getInstance(FirebaseConfig.FIREBASE_REALTIME_DATABASE_URL);
    }

    @Provides
    public static FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Singleton
    @Provides
    public static IRecipeService provideIRecipeService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(RECIPE_BASE_URL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(IRecipeService.class);
    }
}
