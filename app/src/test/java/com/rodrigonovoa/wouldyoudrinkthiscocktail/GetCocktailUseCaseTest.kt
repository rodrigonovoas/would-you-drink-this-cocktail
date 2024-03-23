package com.rodrigonovoa.wouldyoudrinkthiscocktail

import com.rodrigonovoa.wouldyoudrinkthiscocktail.data.DrinksResponse
import com.rodrigonovoa.wouldyoudrinkthiscocktail.repository.TheCocktailDbRepository
import com.rodrigonovoa.wouldyoudrinkthiscocktail.useCase.ApiResult
import com.rodrigonovoa.wouldyoudrinkthiscocktail.useCase.GetCocktailUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class GetCocktailUseCaseTest {

    private lateinit var repository: TheCocktailDbRepository
    private lateinit var getCocktailUseCase: GetCocktailUseCase

    @Before
    fun setUp() {
        repository = mock()
        getCocktailUseCase = GetCocktailUseCase(repository)
    }

    @Test
    fun `invoke returns first loading status`() = runTest {
        // When & Then
        val results = getCocktailUseCase().toList()

        // Assert
        assertTrue(results.first().status == ApiResult.Status.LOADING)
    }

    @Test
    fun `invoke returns success when repository succeeds`() = runTest {
        // Given
        val expectedResultSize = 2
        val drinksResponseMocked = DrinksResponse(listOf())
        `when`(repository.getCocktail()).thenReturn(flowOf(DrinksResponse(listOf())))

        // When & Then
        val results = getCocktailUseCase().toList()

        val firstResult = results.first()
        val secondResult = results[1]

        // Assert
        assertEquals(expectedResultSize, results.size)
        assertTrue(firstResult.status == ApiResult.Status.LOADING)
        assertTrue(secondResult.status == ApiResult.Status.SUCCESS)
        assertEquals(drinksResponseMocked, secondResult.data)
    }

    @Test
    fun `invoke returns exception error when repository throws error`() = runTest {
        // Given
        val expectedResultSize = 2
        val errorMessage = "Exception error"
        `when`(repository.getCocktail()).thenThrow(RuntimeException(errorMessage))

        // When & Then
        val results = getCocktailUseCase().toList()

        val firstResult = results.first()
        val secondResult = results[1]

        // Assert
        assertEquals(expectedResultSize, results.size)
        assertTrue(firstResult.status == ApiResult.Status.LOADING)
        assertTrue(secondResult.status == ApiResult.Status.ERROR)
        assertEquals(secondResult.message, errorMessage)
    }

}
