package com.example.foodapp.repository.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OkHttpAuthInterceptor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OkHttpContentTypeInterceptor