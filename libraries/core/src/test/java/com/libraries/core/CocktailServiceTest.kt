package com.libraries.core

import com.libraries.core.net.CocktailApi
import com.libraries.core.net.CocktailListResponse
import com.libraries.core.net.CocktailsResponse
import com.libraries.core.net.IngredientsResponse
import com.libraries.core.service.CocktailServiceImpl
import com.libraries.testing.ApiMocks.detailResponse
import com.libraries.testing.ApiMocks.ingredientResponse
import com.libraries.testing.ApiMocks.listResponse
import com.libraries.testing.DataMocks.cocktailMock
import com.libraries.testing.DataMocks.cocktailsMock
import com.libraries.testing.DataMocks.ingredientMock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CocktailServiceTest {

    private val cocktailApi = mockk<CocktailApi> {
        coEvery { getCocktailById(any()) } returns detailResponse
        coEvery { getCocktailsByIngredient(any()) } returns listResponse
        coEvery { getCocktailsByName(any()) } returns listResponse
        coEvery { getIngredients(any()) } returns ingredientResponse
        coEvery { getNonAlcoholic() } returns listResponse
    }

    private fun service() = CocktailServiceImpl(cocktailApi = cocktailApi)

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // getCocktailById
    @Test
    fun `result success for getCocktailById`() = runTest {
        val service = service()

        val result = service.getCocktailById("id")
        assertTrue(result.isSuccess)
        assertEquals(cocktailMock, result.getOrNull())
    }

    @Test
    fun `result failure for getCocktailById when drinks empty`() = runTest {
        coEvery { cocktailApi.getCocktailById(any()) } returns CocktailsResponse(drinks = emptyList())

        val service = service()

        val result = service.getCocktailById("id")
        assertTrue(result.isFailure)
    }

    @Test
    fun `result failure for getCocktailById when exception`() = runTest {
        coEvery { cocktailApi.getCocktailById(any()) } throws Throwable()

        val service = service()

        val result = service.getCocktailById("id")
        assertTrue(result.isFailure)
    }

    // getCocktailsBySearchName
    @Test
    fun `result success for getCocktailsBySearchName when search name is ingredient`() = runTest {
        val service = service()

        val result = service.getCocktailsBySearchName("orange juice")
        assertTrue(result.isSuccess)
        assertEquals(cocktailsMock, result.getOrNull())

        coVerify(exactly = 1) { cocktailApi.getCocktailsByIngredient(any()) }
        coVerify(exactly = 0) { cocktailApi.getCocktailsByName(any()) }
    }

    @Test
    fun `result success for getCocktailsBySearchName when search name is cocktail name and by ingredients empty`() = runTest {
        coEvery { cocktailApi.getCocktailsByIngredient(any()) } returns CocktailListResponse(drinks = emptyList())
        val service = service()

        val result = service.getCocktailsBySearchName("margarita")
        assertTrue(result.isSuccess)
        assertEquals(cocktailsMock, result.getOrNull())

        coVerify(exactly = 1) { cocktailApi.getCocktailsByIngredient(any()) }
        coVerify(exactly = 1) { cocktailApi.getCocktailsByName(any()) }
    }

    @Test
    fun `result success for getCocktailsBySearchName when search name is cocktail name and by ingredients null`() = runTest {
        coEvery { cocktailApi.getCocktailsByIngredient(any()) } returns CocktailListResponse(drinks = null)
        val service = service()

        val result = service.getCocktailsBySearchName("margarita")
        assertTrue(result.isSuccess)
        assertEquals(cocktailsMock, result.getOrNull())

        coVerify(exactly = 1) { cocktailApi.getCocktailsByIngredient(any()) }
        coVerify(exactly = 1) { cocktailApi.getCocktailsByName(any()) }
    }

    @Test
    fun `result success for getCocktailsBySearchName when search name is cocktail name and by ingredients exception`() = runTest {
        coEvery { cocktailApi.getCocktailsByIngredient(any()) } throws Throwable()
        val service = service()

        val result = service.getCocktailsBySearchName("margarita")
        assertTrue(result.isSuccess)
        assertEquals(cocktailsMock, result.getOrNull())

        coVerify(exactly = 1) { cocktailApi.getCocktailsByIngredient(any()) }
        coVerify(exactly = 1) { cocktailApi.getCocktailsByName(any()) }
    }

    @Test
    fun `result failure for getCocktailsBySearchName when search name for cocktail name empty`() = runTest {
        coEvery { cocktailApi.getCocktailsByIngredient(any()) } returns CocktailListResponse(drinks = null)
        coEvery { cocktailApi.getCocktailsByName(any()) } returns CocktailListResponse(drinks = emptyList())
        val service = service()

        val result = service.getCocktailsBySearchName("margarita")
        assertTrue(result.isFailure)

        coVerify(exactly = 1) { cocktailApi.getCocktailsByIngredient(any()) }
        coVerify(exactly = 1) { cocktailApi.getCocktailsByName(any()) }
    }

    @Test
    fun `result failure for getCocktailsBySearchName when search name for cocktail name null`() = runTest {
        coEvery { cocktailApi.getCocktailsByIngredient(any()) } returns CocktailListResponse(drinks = null)
        coEvery { cocktailApi.getCocktailsByName(any()) } returns CocktailListResponse(drinks = null)
        val service = service()

        val result = service.getCocktailsBySearchName("margarita")
        assertTrue(result.isFailure)

        coVerify(exactly = 1) { cocktailApi.getCocktailsByIngredient(any()) }
        coVerify(exactly = 1) { cocktailApi.getCocktailsByName(any()) }
    }

    @Test
    fun `result failure for getCocktailsBySearchName when search name for cocktail name exception`() = runTest {
        coEvery { cocktailApi.getCocktailsByIngredient(any()) } returns CocktailListResponse(drinks = null)
        coEvery { cocktailApi.getCocktailsByName(any()) } throws Throwable()
        val service = service()

        val result = service.getCocktailsBySearchName("margarita")
        assertTrue(result.isFailure)

        coVerify(exactly = 1) { cocktailApi.getCocktailsByIngredient(any()) }
        coVerify(exactly = 1) { cocktailApi.getCocktailsByName(any()) }
    }

    // getNonAlcoholic
    @Test
    fun `result success for getNonAlcoholic`() = runTest {
        val service = service()

        val result = service.getNonAlcoholic()
        assertTrue(result.isSuccess)
        assertEquals(cocktailsMock, result.getOrNull())
    }

    @Test
    fun `result failure for getNonAlcoholic when drinks empty`() = runTest {
        coEvery { cocktailApi.getNonAlcoholic() } returns CocktailListResponse(drinks = emptyList())

        val service = service()

        val result = service.getNonAlcoholic()
        assertTrue(result.isFailure)
    }

    @Test
    fun `result failure for getNonAlcoholic when drinks null`() = runTest {
        coEvery { cocktailApi.getNonAlcoholic() } returns CocktailListResponse(drinks = null)

        val service = service()

        val result = service.getNonAlcoholic()
        assertTrue(result.isFailure)
    }

    @Test
    fun `result failure for getNonAlcoholic when exception`() = runTest {
        coEvery { cocktailApi.getNonAlcoholic() } throws Throwable()

        val service = service()

        val result = service.getNonAlcoholic()
        assertTrue(result.isFailure)
    }

    // getCocktailIngredient
    @Test
    fun `result success for getCocktailIngredient`() = runTest {
        val service = service()

        val result = service.getCocktailIngredient("vodka")
        assertTrue(result.isSuccess)
        assertEquals(ingredientMock, result.getOrNull())
    }

    @Test
    fun `result failure for getCocktailIngredient when drinks empty`() = runTest {
        coEvery { cocktailApi.getIngredients(any()) } returns IngredientsResponse(ingredients = emptyList())

        val service = service()

        val result = service.getCocktailIngredient("vodka")
        assertTrue(result.isFailure)
    }

    @Test
    fun `result failure for getCocktailIngredient when exception`() = runTest {
        coEvery { cocktailApi.getIngredients(any()) } throws Throwable()

        val service = service()

        val result = service.getCocktailIngredient("vodka")
        assertTrue(result.isFailure)
    }
}
