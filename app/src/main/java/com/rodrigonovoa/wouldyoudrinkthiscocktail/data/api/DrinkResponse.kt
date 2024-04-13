package com.rodrigonovoa.wouldyoudrinkthiscocktail.data.api

import com.rodrigonovoa.wouldyoudrinkthiscocktail.data.db.Drink

data class DrinksResponse(
    val drinks: List<DrinkResponse>
)

data class DrinkResponse(
    val idDrink: String,
    val strDrink: String,
    val strCategory: String,
    val strAlcoholic: String,
    val strInstructions: String,
    val strDrinkThumb: String
) {
    fun fromResponseToDrink(): Drink {
        return Drink(
            idDrink,
            strDrink,
            strCategory,
            strAlcoholic,
            strInstructions,
            strDrinkThumb
        )
    }
}
