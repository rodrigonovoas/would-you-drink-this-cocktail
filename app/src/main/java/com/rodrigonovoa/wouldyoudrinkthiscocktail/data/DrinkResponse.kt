package com.rodrigonovoa.wouldyoudrinkthiscocktail.data

data class DrinksResponse(
    val drinks: List<Drink>
)

data class Drink(
    val idDrink: String,
    val strDrink: String,
    val strDrinkAlternate: String?,
    val strTags: String?,
    val strVideo: String?,
    val strCategory: String,
    val strIBA: String?,
    val strAlcoholic: String,
    val strGlass: String,
    val strInstructions: String,
    val strInstructionsES: String?,
    val strInstructionsDE: String?,
    val strInstructionsFR: String?,
    val strInstructionsIT: String?,
    val strInstructionsZH_HANS: String?,
    val strInstructionsZH_HANT: String?,
    val strDrinkThumb: String,
    val strIngredient1: String?,
    val strIngredient2: String?,
    val strIngredient3: String?,
    val strMeasure1: String?,
    val strMeasure2: String?,
    val strMeasure3: String?,
    val strImageSource: String?,
    val strImageAttribution: String?,
    val strCreativeCommonsConfirmed: String,
    val dateModified: String
)
