package com.example.foodapp.repository.di;

import com.example.foodapp.repository.firebase.FirebaseConfig;
import com.example.foodapp.repository.network.IRecipeService;
import com.example.foodapp.repository.network.OkHttpInterceptorProvider;
import com.example.foodapp.repository.network.OkHttpServiceProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.Interceptor;

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
    public static IRecipeService provideIRecipeService(
            @OkHttpContentTypeInterceptor Interceptor contentTypeInterceptor,
            @OkHttpAuthInterceptor Interceptor authInterceptor
    ) {
        return OkHttpServiceProvider.getRecipeService(contentTypeInterceptor, authInterceptor);
    }

    @OkHttpAuthInterceptor
    @Provides
    public static Interceptor provideAuthInterceptor() {
        return OkHttpInterceptorProvider.getApiKeyInterceptor();
    }

    @OkHttpContentTypeInterceptor
    @Provides
    public static Interceptor provideContentTypeInterceptor() {
        return OkHttpInterceptorProvider.getContentTypeInterceptor();
    }
}
