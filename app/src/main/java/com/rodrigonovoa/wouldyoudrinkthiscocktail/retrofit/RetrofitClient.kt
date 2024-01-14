package com.rodrigonovoa.wouldyoudrinkthiscocktail.retrofit

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val apiKey = "1"

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.thecocktaildb.com/api/json/v1/$apiKey/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    fun getCoffeeApiService(): TheCocktailDbService? {
        try {
            return retrofit.create(TheCocktailDbService::class.java)
        } catch (e: Exception) {
            // Log the exception or handle it as needed
            Log.e("RetrofitError", "Error creating Retrofit service: ${e.message}")
            return null
        }
    }
}