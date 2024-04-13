package com.rodrigonovoa.wouldyoudrinkthiscocktail.useCase

import com.rodrigonovoa.wouldyoudrinkthiscocktail.data.db.Drink
import com.rodrigonovoa.wouldyoudrinkthiscocktail.db.dao.DrinkDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetCocktailslDbUseCase(private val drinkDao: DrinkDao) {
    operator fun invoke(): Flow<List<Drink>> = flow {
        val result = drinkDao.getAllDrinks()
        emit(result)
    }.catch { e ->
        print(e)
        emit(listOf())
    }
}