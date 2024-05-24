package com.example

import androidx.compose.runtime.snapshotFlow
import app.cash.turbine.test
import com.example.testappcompose.core.service.CocktailService
import com.example.testappcompose.screens.IngredientDetailData
import com.example.testappcompose.screens.IngredientDetailViewModel
import com.example.testappcompose.screens.ViewState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
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
class IngredientDetailViewModelTest {
    private val cocktailService = mockk<CocktailService> {
        coEvery { getCocktailIngredient(any()) } returns Result.success(DataMocks.ingredientMock)
        coEvery { getCocktailsBySearchName(any()) } returns Result.success(DataMocks.cocktailsMock)
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
    fun `assert loaded view state`() = runTest {
        val vm = viewModel()

        launch {
            snapshotFlow { vm.viewState.value }.test {
                val state = awaitItem()
                assertTrue(state is ViewState.Loaded)
                val loaded = state as ViewState.Loaded
                assertEquals(
                    IngredientDetailData(
                        name = "Vodka",
                        image = "https://www.thecocktaildb.com/images/ingredients/Vodka.png",
                        abv = "40",
                        description = "Vodka is a distilled beverage composed primarily of water and ethanol, sometimes with traces of impurities and flavorings.",
                        cocktails = DataMocks.cocktailsMock
                    ),
                    loaded.data
                )
            }
        }
        vm.loadData("Vodka")
    }

    @Test
    fun `assert loaded view state with getCocktailIngredient failure`() = runTest {
        coEvery { cocktailService.getCocktailIngredient(any()) } returns Result.failure(Throwable())

        val vm = viewModel()

        launch {
            snapshotFlow { vm.viewState.value }.test {
                val state = awaitItem()
                assertTrue(state is ViewState.Loaded)
                val loaded = state as ViewState.Loaded
                assertEquals(
                    IngredientDetailData(
                        name = "Vodka",
                        image = "https://www.thecocktaildb.com/images/ingredients/Vodka.png",
                        abv = null,
                        description = null,
                        cocktails = DataMocks.cocktailsMock
                    ),
                    loaded.data
                )
            }
        }
        vm.loadData("Vodka")
    }

    @Test
    fun `assert loaded view state with getCocktailsBySearchName empty`() = runTest {
        coEvery { cocktailService.getCocktailsBySearchName(any()) } returns Result.success(emptyList())

        val vm = viewModel()

        launch {
            snapshotFlow { vm.viewState.value }.test {
                val state = awaitItem()
                assertTrue(state is ViewState.Error)
            }
        }
        vm.loadData("Vodka")
    }

    @Test
    fun `assert loaded view state with getCocktailsBySearchName failure`() = runTest {
        coEvery { cocktailService.getCocktailsBySearchName(any()) } returns Result.failure(Throwable())

        val vm = viewModel()

        launch {
            snapshotFlow { vm.viewState.value }.test {
                val state = awaitItem()
                assertTrue(state is ViewState.Error)
            }
        }
        vm.loadData("Vodka")
    }
}
