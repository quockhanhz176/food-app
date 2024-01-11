package com.example.foodapp.repository.network

import com.example.foodapp.repository.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object OkHttpServiceProvider {
    @JvmStatic
    fun getRecipeService(
        contentTypeInterceptor: Interceptor,
        authInterceptor: Interceptor
    ): IRecipeService {
        val client = OkHttpClient.Builder()
            .addNetworkInterceptor(contentTypeInterceptor)
            .addNetworkInterceptor(authInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.RECIPE_BASE_URL)
            .client(client)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(IRecipeService::class.java)
    }
}