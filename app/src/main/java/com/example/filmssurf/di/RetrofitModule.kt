package com.example.filmssurf.di

import com.example.filmssurf.api.FilmsApi
import com.example.filmssurf.other.Utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY))
            .build())
        .build()

    @Provides
    @Singleton
    fun provideApiFilms(retrofit: Retrofit): FilmsApi = retrofit.create(FilmsApi::class.java)
}