package com.rodrigonovoa.wouldyoudrinkthiscocktail.useCase

import com.rodrigonovoa.wouldyoudrinkthiscocktail.data.api.DrinksResponse
import com.rodrigonovoa.wouldyoudrinkthiscocktail.repository.ApiResult
import com.rodrigonovoa.wouldyoudrinkthiscocktail.repository.TheCocktailDbRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetCocktailUseCase(private val repository: TheCocktailDbRepository) {

    operator fun invoke(): Flow<ApiResult<DrinksResponse?>> = flow {
        emit(ApiResult.loading())
        repository.getCocktail().collect { drinksResponse ->
            emit(ApiResult.success(drinksResponse))
        }
    }.catch { exception ->
        emit(ApiResult.error(exception.message ?: "An unexpected error occurred"))
    }
}
