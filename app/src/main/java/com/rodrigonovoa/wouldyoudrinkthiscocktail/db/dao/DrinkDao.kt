package com.rodrigonovoa.wouldyoudrinkthiscocktail.db.dao

import androidx.room.*
import com.rodrigonovoa.wouldyoudrinkthiscocktail.data.db.Drink

@Dao
interface DrinkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrink(drink: Drink)

    @Update
    suspend fun updateDrink(drink: Drink)

    @Delete
    suspend fun deleteDrink(drink: Drink)

    @Query("SELECT * FROM Drink")
    suspend fun getAllDrinks(): List<Drink>

    @Query("DELETE FROM Drink")
    suspend fun deleteAllDrinks()
}