package com.rodrigonovoa.wouldyoudrinkthiscocktail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rodrigonovoa.wouldyoudrinkthiscocktail.data.api.DrinkResponse
import com.rodrigonovoa.wouldyoudrinkthiscocktail.data.api.DrinksResponse
import com.rodrigonovoa.wouldyoudrinkthiscocktail.data.db.Drink
import com.rodrigonovoa.wouldyoudrinkthiscocktail.repository.ApiResult
import com.rodrigonovoa.wouldyoudrinkthiscocktail.ui.mainActivity.MainActivityViewModel
import com.rodrigonovoa.wouldyoudrinkthiscocktail.useCase.GetCocktailUseCase
import com.rodrigonovoa.wouldyoudrinkthiscocktail.useCase.InsertDrinkUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MainActivityViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getCocktailUseCase: GetCocktailUseCase
    private lateinit var insertDrinkUseCase: InsertDrinkUseCase
    private lateinit var mainActivityViewModel: MainActivityViewModel

    @Before
    fun setUp() {
        getCocktailUseCase = Mockito.mock()
        insertDrinkUseCase = Mockito.mock()
        mainActivityViewModel = MainActivityViewModel(getCocktailUseCase, insertDrinkUseCase)

        Dispatchers.setMain(TestCoroutineDispatcher())
    }


    @Test
    fun `getDrinkFromAPI returns drink object successful`() = runTest {
        val mockDrinksResponse = DrinksResponse(listOf())
        Mockito.`when`(getCocktailUseCase.invoke()).thenReturn(
            flowOf(
                ApiResult.loading(),
                ApiResult.success(mockDrinksResponse)
            )
        )

        // When & Then
        mainActivityViewModel.getDrinkFromAPI()

        // Assert
        Assert.assertTrue(mainActivityViewModel.drink.first() != null)
        Assert.assertTrue(mainActivityViewModel.isLoading.value == false)
    }

    @Test
    fun `insertDrink with filled DrinkResponse inserts drink successful`() = runTest {
        val mockDrinkResponse = DrinkResponse("", "", "", "",
        "", "")
        val mockDrink = Drink("", "", "", "", "")

        Mockito.`when`(insertDrinkUseCase.invoke(mockDrink)).thenReturn(flowOf(true))

        // When & Then
        mainActivityViewModel.insertDrink(mockDrinkResponse)

        // Assert
        Assert.assertTrue(mainActivityViewModel.drinkInserted.first() == true)
    }

    @Test
    fun `insertDrink with NULL DrinkResponse does not insert`() = runTest {
        // When & Then
        mainActivityViewModel.insertDrink(null)

        // Assert
        Assert.assertTrue(mainActivityViewModel.drinkInserted.first() == false)
    }

    @Test
    fun `resetDrinkInserted turns _drinkInserted into FALSE`() = runTest {
        // When & Then
        mainActivityViewModel.resetDrinkInserted()

        // Assert
        Assert.assertTrue(mainActivityViewModel.drinkInserted.first() == false)
    }

}

@ExperimentalCoroutinesApi
class MainDispatcherRule(val dispatcher: TestDispatcher = StandardTestDispatcher()): TestWatcher() {

    override fun starting(description: Description?) = Dispatchers.setMain(dispatcher)

    override fun finished(description: Description?) = Dispatchers.resetMain()

}