package com.rodrigonovoa.wouldyoudrinkthiscocktail.useCase

import com.rodrigonovoa.wouldyoudrinkthiscocktail.data.db.Drink
import com.rodrigonovoa.wouldyoudrinkthiscocktail.db.dao.DrinkDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class InsertDrinkUseCase(private val drinkDao: DrinkDao) {
    operator fun invoke(drink: Drink): Flow<Boolean> = flow {
        val result = drinkDao.insertDrink(drink)
        emit(result > 0)
    }.catch { e ->
        emit(false)
    }
}