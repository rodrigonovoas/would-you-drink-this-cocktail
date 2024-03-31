package com.rodrigonovoa.wouldyoudrinkthiscocktail.repository

import android.util.Log
import com.rodrigonovoa.wouldyoudrinkthiscocktail.data.api.DrinksResponse
import com.rodrigonovoa.wouldyoudrinkthiscocktail.retrofit.TheCocktailDbService
import kotlinx.coroutines.flow.flow

class TheCocktailDbRepository(val service: TheCocktailDbService?) {
    fun getCocktail() = flow<DrinksResponse?> {
        try {
            val response = service?.getRandomCocktail()
            if (response?.isSuccessful == true) {
                emit(response?.body())
            } else {
                emit(null)
            }
        } catch (e: Exception) {
            Log.e("NetworkError", "Error during network call: ${e.message}")
        }
    }
}