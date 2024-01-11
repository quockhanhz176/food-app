package com.example.foodapp.repository.network

import com.example.foodapp.AppConfig
import okhttp3.Interceptor

object OkHttpInterceptorProvider {
    @JvmStatic
    val contentTypeInterceptor
        get() = Interceptor { chain ->
            val request = chain
                .request()
                .newBuilder()
                .header("Accept", "application/json").build()
            chain.proceed(request)
        }

    @JvmStatic
    val apiKeyInterceptor
        get() = Interceptor { chain ->
            val request = chain
                .request()
                .newBuilder()
                .header("x-api-key", AppConfig.RECIPE_API_KEY).build()
            chain.proceed(request)
        }
}