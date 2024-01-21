package com.rodrigonovoa.wouldyoudrinkthiscocktail

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodrigonovoa.wouldyoudrinkthiscocktail.data.DrinksResponse
import com.rodrigonovoa.wouldyoudrinkthiscocktail.retrofit.RetrofitClient
import kotlinx.coroutines.launch

class MainActivityViewModel: ViewModel() {
    private var _drink = mutableStateOf<DrinksResponse?>(null)

    val drink: State<DrinksResponse?>
        get() = _drink

    var isLoading = mutableStateOf(false)

    init {
        getDrintFromAPI()
    }

    private fun getDrintFromAPI() {
        isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.getDrinkApiService()!!.getRandomCocktail()
                if (response.isSuccessful) {
                    _drink.value = response.body()
                }
                isLoading.value = false
            } catch (e: Exception) {
                Log.e("NetworkError", "Error during network call: ${e.message}")
            }

        }
    }
}