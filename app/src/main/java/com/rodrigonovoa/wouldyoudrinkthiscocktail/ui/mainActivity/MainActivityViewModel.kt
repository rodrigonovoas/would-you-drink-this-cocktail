package com.rodrigonovoa.wouldyoudrinkthiscocktail.ui.mainActivity

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodrigonovoa.wouldyoudrinkthiscocktail.data.DrinksResponse
import com.rodrigonovoa.wouldyoudrinkthiscocktail.repository.TheCocktailDbRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel(val repository: TheCocktailDbRepository): ViewModel() {
    private val _drink = MutableStateFlow(DrinksResponse(listOf()))
    val drink: Flow<DrinksResponse> = _drink

    var isLoading = mutableStateOf(false)

    init {
        getDrinkFromAPI()
    }

    private fun getDrinkFromAPI() {
        isLoading.value = true
        viewModelScope.launch {
            isLoading.value = false
            repository.getCocktail().collect() {
                it?.let {
                    _drink.value = it
                }
            }
        }
    }
}