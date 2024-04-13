package com.rodrigonovoa.wouldyoudrinkthiscocktail.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rodrigonovoa.wouldyoudrinkthiscocktail.data.api.DrinkResponse
import com.rodrigonovoa.wouldyoudrinkthiscocktail.data.api.DrinksResponse

@Entity
data class Drink(
    @PrimaryKey val id: String,
    val name: String?,
    val category: String?,
    val type: String?,
    val instructions: String?,
    val thumb: String?
) {
    fun toDrinkResponse(): DrinksResponse {
        return DrinksResponse(
            listOf<DrinkResponse>(
                DrinkResponse(
                    id,
                    name ?: "",
                    category ?: "",
                    type ?: "",
                    instructions ?: "",
                    thumb ?: ""
                )
            )
        )
    }
}