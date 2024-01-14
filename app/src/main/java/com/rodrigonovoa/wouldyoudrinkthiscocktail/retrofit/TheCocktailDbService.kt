package com.rodrigonovoa.wouldyoudrinkthiscocktail.retrofit

import com.rodrigonovoa.wouldyoudrinkthiscocktail.data.DrinksResponse
import retrofit2.Response
import retrofit2.http.GET

interface TheCocktailDbService {
    @GET("random.php")
    suspend fun getRandomCocktail(): Response<DrinksResponse>
}