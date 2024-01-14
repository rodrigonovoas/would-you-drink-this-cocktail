package com.rodrigonovoa.wouldyoudrinkthiscocktail

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodrigonovoa.wouldyoudrinkthiscocktail.data.DrinksResponse
import com.rodrigonovoa.wouldyoudrinkthiscocktail.retrofit.RetrofitClient
import kotlinx.coroutines.launch

class MainActivityViewModel: ViewModel() {
    var data = mutableStateOf<DrinksResponse?>(null)
    var isLoading = mutableStateOf(false)

    init {
        fetchData()
    }

    private fun fetchData() {
        isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.getCoffeeApiService()!!.getRandomCocktail()
                if (response.isSuccessful) {
                    data.value = response.body()
                }
                isLoading.value = false
            } catch (e: Exception) {
                Log.e("NetworkError", "Error during network call: ${e.message}")
            }

        }
    }
}