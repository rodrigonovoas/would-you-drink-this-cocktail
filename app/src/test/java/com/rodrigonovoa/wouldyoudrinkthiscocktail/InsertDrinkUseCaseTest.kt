package com.rodrigonovoa.wouldyoudrinkthiscocktail

import com.rodrigonovoa.wouldyoudrinkthiscocktail.data.db.Drink
import com.rodrigonovoa.wouldyoudrinkthiscocktail.db.dao.DrinkDao
import com.rodrigonovoa.wouldyoudrinkthiscocktail.useCase.InsertDrinkUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class InsertDrinkUseCaseTest {
    private lateinit var insertDrinkUseCase: InsertDrinkUseCase
    private lateinit var dao: DrinkDao

    @Before
    fun setUp() {
        dao = mock()
        insertDrinkUseCase = InsertDrinkUseCase(dao)
    }

    @Test
    fun `insert drink in db is success with TRUE`() = runTest {
        val drink = Drink("","", "", "", "", "")

        Mockito.`when`(dao.insertDrink(drink)).thenReturn(1)

        val result = insertDrinkUseCase.invoke(drink).first()

        assertTrue(result)
    }

    @Test
    fun `insert drink in db fails with FALSE`() = runTest {
        val drink = Drink("","", "", "", "", "")

        Mockito.`when`(dao.insertDrink(drink)).thenReturn(0)

        val result = insertDrinkUseCase.invoke(drink).first()

        assertFalse(result)
    }

    @Test
    fun `insert drink in db fails with EXCEPTION`() = runTest {
        val drink = Drink("","", "", "", "", "")

        Mockito.`when`(dao.insertDrink(drink)).thenThrow(RuntimeException("Database write failure"))

        val result = insertDrinkUseCase.invoke(drink).first()

        assertFalse(result)
    }

}