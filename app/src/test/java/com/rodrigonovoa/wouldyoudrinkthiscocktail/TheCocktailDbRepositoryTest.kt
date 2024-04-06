package com.rodrigonovoa.wouldyoudrinkthiscocktail

import android.util.Log
import com.rodrigonovoa.wouldyoudrinkthiscocktail.data.api.DrinksResponse
import com.rodrigonovoa.wouldyoudrinkthiscocktail.repository.TheCocktailDbRepository
import com.rodrigonovoa.wouldyoudrinkthiscocktail.retrofit.TheCocktailDbService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.mockStatic
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class TheCocktailDbRepositoryTest {

    private lateinit var service: TheCocktailDbService
    private lateinit var repository: TheCocktailDbRepository

    @Before
    fun setUp() {
        service = Mockito.mock()
        repository = TheCocktailDbRepository(service)
    }

    @Test
    fun `getCocktail returns body when response isSuccessful`() = runTest {
        // Given
        val drinksResponseMocked = Response.success(DrinksResponse(listOf()))
        Mockito.`when`(service.getRandomCocktail()).thenReturn(drinksResponseMocked)

        // When & Then
        val result = repository.getCocktail()

        result.collect() {
            Assert.assertTrue(it != null)
        }
    }

    @Test
    fun `getCocktail returns null when response isSuccessful is false`() = runTest {
        // Give
        val errorCode = 404 // HTTP status code
        val errorMessage = "Not Found"
        val errorResponseBody = errorMessage.toResponseBody(null)
        val mockErrorResponse = Response.error<DrinksResponse>(errorCode, errorResponseBody)

        Mockito.`when`(service.getRandomCocktail()).thenReturn(mockErrorResponse)

        // When & Then
        val result = repository.getCocktail()

        result.collect() {
            Assert.assertTrue(it == null)
        }
    }

    @Test
    fun `getCocktail throws error when service fails`() = runTest {
        val errorMessage = "error"
        // Given
        val mock = mockStatic(Log::class.java)
        mock.`when`<Any> { Log.e("NetworkError", "Error during network call: ${errorMessage}") }.thenReturn(0)
        Mockito.`when`(service.getRandomCocktail()).thenThrow(RuntimeException(errorMessage))


        // When & Then
        val result = repository.getCocktail()

        result.collect() {
            Assert.assertTrue(it == null)
        }
    }
}