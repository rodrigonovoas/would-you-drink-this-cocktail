package com.rodrigonovoa.wouldyoudrinkthiscocktail

import com.rodrigonovoa.wouldyoudrinkthiscocktail.data.db.Drink
import com.rodrigonovoa.wouldyoudrinkthiscocktail.db.dao.DrinkDao
import com.rodrigonovoa.wouldyoudrinkthiscocktail.useCase.GetCocktailslDbUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class GetCocktailsDbUseCaseTest {

    private lateinit var drinkDao: DrinkDao
    private lateinit var getCocktailsDbUseCase: GetCocktailslDbUseCase

    @Before
    fun setUp() {
        drinkDao = Mockito.mock()
        getCocktailsDbUseCase = GetCocktailslDbUseCase(drinkDao)
    }

    @Test
    fun `getAllDrinks returns drinks when dao succeeds`() = runTest {
        // Given
        val mockedList = listOf(
            Drink("",null, null, null, null, null),
            Drink("",null, null, null, null, null)
        )

        val expectedResultSize = 2
        Mockito.`when`(drinkDao.getAllDrinks()).thenReturn(mockedList)

        // When & Then
        val results = getCocktailsDbUseCase().first()

        // Assert
        Assert.assertEquals(expectedResultSize, results.size)
        Assert.assertEquals(mockedList, results)
    }

    @Test
    fun `getAllDrinks returns empty list when exception occurs`() = runTest {
        val errorMessage = "error"
        val expectedResultSize = 0
        Mockito.`when`(drinkDao.getAllDrinks()).thenThrow(RuntimeException(errorMessage))

        // When & Then
        val results = getCocktailsDbUseCase().first()

        // Assert
        Assert.assertEquals(expectedResultSize, results.size)
        Assert.assertEquals(listOf<Drink>(), results)
    }
}