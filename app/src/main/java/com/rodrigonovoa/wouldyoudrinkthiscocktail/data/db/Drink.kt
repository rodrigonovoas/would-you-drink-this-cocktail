package com.rodrigonovoa.wouldyoudrinkthiscocktail.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Drink(
    @PrimaryKey val id: Int,
    val name: String?,
    val category: String?,
    val type: String?,
    val instructions: String?
)