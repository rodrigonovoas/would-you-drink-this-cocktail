package com.rodrigonovoa.wouldyoudrinkthiscocktail.ui.mainActivity

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodrigonovoa.wouldyoudrinkthiscocktail.data.DrinksResponse
import com.rodrigonovoa.wouldyoudrinkthiscocktail.useCase.ApiResult
import com.rodrigonovoa.wouldyoudrinkthiscocktail.useCase.GetCocktailUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel(private val getCocktailUseCase: GetCocktailUseCase) : ViewModel() {
    private val _drink = MutableStateFlow<ApiResult<DrinksResponse?>>(ApiResult.loading())
    val drink: Flow<ApiResult<DrinksResponse?>> = _drink
    var isLoading = mutableStateOf(false)

    init {
        getDrinkFromAPI()
    }

    private fun getDrinkFromAPI() {
        viewModelScope.launch {
            getCocktailUseCase().collect { result ->
                _drink.value = result
                isLoading.value = result.status == ApiResult.Status.LOADING
            }
        }
    }
}