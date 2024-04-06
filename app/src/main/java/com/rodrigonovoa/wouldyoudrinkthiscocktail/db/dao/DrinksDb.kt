package com.rodrigonovoa.wouldyoudrinkthiscocktail.db.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rodrigonovoa.wouldyoudrinkthiscocktail.data.db.Drink


@Database(entities = [Drink::class], version = 1, exportSchema = false)
abstract class DrinksDb : RoomDatabase() {
    abstract fun drinkDao(): DrinkDao
}