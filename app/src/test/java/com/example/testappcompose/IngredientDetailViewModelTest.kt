package com.example.testappcompose

import app.cash.turbine.test
import com.example.testappcompose.screens.IngredientDetailData
import com.example.testappcompose.screens.IngredientDetailViewModel
import com.libraries.core.service.CocktailService
import com.libraries.testing.DataMocks.cocktailsMock
import com.libraries.testing.DataMocks.ingredientMock
import com.libraries.ui.ViewState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class IngredientDetailViewModelTest {
    private val cocktailService = mockk<CocktailService> {
        coEvery { getCocktailIngredient(any()) } returns Result.success(ingredientMock)
        coEvery { getCocktailsBySearchName(any()) } returns Result.success(cocktailsMock)
    }

    private fun viewModel() = IngredientDetailViewModel(cocktailService = cocktailService)

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `assert loaded view state when both calls successful`() = runTest {
        coEvery { cocktailService.getCocktailIngredient(any()) }.coAnswers {
            delay(100) // Simulate loading
            Result.success(ingredientMock)
        }

        val vm = viewModel()

        // Starts Uninitialized
        assertEquals(ViewState.Uninitialized::class.java, vm.viewState.value.javaClass)

        vm.loadData("Vodka")
        vm.viewState.test {
            // Loading
            assertEquals(ViewState.Loading::class.java, awaitItem().javaClass)

            // Loaded
            val state = awaitItem()
            assertEquals(ViewState.Loaded::class.java, state.javaClass)
            val data = (state as ViewState.Loaded).data
            assertEquals(
                IngredientDetailData(
                    name = "Vodka",
                    image = "https://www.thecocktaildb.com/images/ingredients/Vodka.png",
                    abv = "40",
                    description = "Vodka is a distilled beverage composed primarily of water and ethanol, sometimes with traces of impurities and flavorings.",
                    cocktails = cocktailsMock
                ),
                data
            )

            // Calling loadData when already loaded does not do anything
            vm.loadData("Vodka")
            expectNoEvents()
        }

        coVerify(exactly = 1) { cocktailService.getCocktailIngredient(any()) }
        coVerify(exactly = 1) { cocktailService.getCocktailsBySearchName(any()) }
    }

    @Test
    fun `assert loaded view state even with getCocktailIngredient failure`() = runTest {
        coEvery { cocktailService.getCocktailIngredient(any()) } returns Result.failure(Throwable())

        val vm = viewModel()

        vm.loadData("Vodka")
        vm.viewState.test {
            val state = awaitItem()
            assertEquals(ViewState.Loaded::class.java, state.javaClass)
            val data = (state as ViewState.Loaded).data
            assertEquals(
                IngredientDetailData(
                    name = "Vodka",
                    image = "https://www.thecocktaildb.com/images/ingredients/Vodka.png",
                    abv = null,
                    description = null,
                    cocktails = cocktailsMock
                ),
                data
            )
        }
    }

    @Test
    fun `assert error and then loaded view state on retry loadData`() = runTest {
        coEvery { cocktailService.getCocktailsBySearchName(any()) } returnsMany listOf(
            Result.failure(Throwable()),
            Result.success(cocktailsMock)
        )

        val vm = viewModel()

        vm.loadData("Vodka")
        vm.viewState.test {
            assertEquals(ViewState.Error::class.java, awaitItem().javaClass)
        }

        vm.loadData("Vodka")
        vm.viewState.test {
            assertEquals(ViewState.Loaded::class.java, awaitItem().javaClass)
        }

        coVerify(exactly = 2) { cocktailService.getCocktailIngredient(any()) }
        coVerify(exactly = 2) { cocktailService.getCocktailsBySearchName(any()) }
    }
}
