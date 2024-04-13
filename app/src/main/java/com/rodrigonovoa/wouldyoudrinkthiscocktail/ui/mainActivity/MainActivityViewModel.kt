package com.rodrigonovoa.wouldyoudrinkthiscocktail.ui.mainActivity

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rodrigonovoa.wouldyoudrinkthiscocktail.data.api.DrinkResponse
import com.rodrigonovoa.wouldyoudrinkthiscocktail.data.api.DrinksResponse
import com.rodrigonovoa.wouldyoudrinkthiscocktail.data.db.Drink
import com.rodrigonovoa.wouldyoudrinkthiscocktail.repository.ApiResult
import com.rodrigonovoa.wouldyoudrinkthiscocktail.useCase.GetCocktailUseCase
import com.rodrigonovoa.wouldyoudrinkthiscocktail.useCase.GetCocktailslDbUseCase
import com.rodrigonovoa.wouldyoudrinkthiscocktail.useCase.InsertDrinkUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val getCocktailUseCase: GetCocktailUseCase,
    private val getCocktailsDbUseCase: GetCocktailslDbUseCase,
    private val insertDrinkUseCase: InsertDrinkUseCase
) : ViewModel() {

    private val _drink = MutableStateFlow<ApiResult<DrinksResponse?>>(ApiResult.loading())
    val drink: Flow<ApiResult<DrinksResponse?>> = _drink

    private val _drinks = MutableStateFlow<List<Drink>>(listOf())
    val drinks: Flow<List<Drink>> = _drinks

    private val _drinkInserted = MutableStateFlow<Boolean>(false)
    val drinkInserted = _drinkInserted.asSharedFlow()

    var isLoading = mutableStateOf(false)

    init {
        getDrinkFromAPI()
        getDrinksFromDb()
    }

    fun getDrinkFromAPI() {
        viewModelScope.launch {
            getCocktailUseCase().collect { result ->
                _drink.value = result
                isLoading.value = result.status == ApiResult.Status.LOADING
            }
        }
    }

    fun getDrinksFromDb() {
        viewModelScope.launch {
            getCocktailsDbUseCase().collect { result ->
                _drinks.value = result
            }
        }
    }

    fun loadDrink(drink: Drink) {
        _drink.value = ApiResult.success( drink.toDrinkResponse() )
    }

    fun insertDrink(drink: DrinkResponse?) {
        val drink = drink?.fromResponseToDrink() ?: return

        viewModelScope.launch {
            insertDrinkUseCase(drink).collect { result ->
                _drinkInserted.value = result
            }
        }
    }

    fun resetDrinkInserted() {
        _drinkInserted.value = false
    }
}