package com.example.testappcompose

import app.cash.turbine.test
import com.example.testappcompose.screens.CocktailsListViewModel
import com.libraries.core.service.CocktailService
import com.libraries.testing.DataMocks.cocktailsMock
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
class CocktailsListViewModelTest {
    private val cocktailService = mockk<CocktailService> {
        coEvery { getNonAlcoholic() } returns Result.success(cocktailsMock)
        coEvery { getCocktailsBySearchName(any()) } returns Result.success(cocktailsMock)
    }

    private fun viewModel() = CocktailsListViewModel(cocktailService = cocktailService)

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `assert loaded view state for non-alcoholic`() = runTest {
        coEvery { cocktailService.getNonAlcoholic() }.coAnswers {
            delay(100) // Simulate loading
            Result.success(cocktailsMock)
        }

        val vm = viewModel()

        // Starts Uninitialized
        assertEquals(ViewState.Uninitialized::class.java, vm.viewState.value.javaClass)

        vm.loadData("", nonAlcoholic = true)
        vm.viewState.test {
            // Loading
            assertEquals(ViewState.Loading::class.java, awaitItem().javaClass)

            // Loaded
            val state = awaitItem()
            assertEquals(ViewState.Loaded::class.java, state.javaClass)
            val data = (state as ViewState.Loaded).data
            // No VM data transformation here
            assertEquals(cocktailsMock, data)

            // Calling loadData when already loaded does not do anything
            vm.loadData("", nonAlcoholic = true)
            expectNoEvents()
        }

        coVerify(exactly = 1) { cocktailService.getNonAlcoholic() }
    }

    @Test
    fun `assert error and then loaded view state on retry loadData for non-alcoholic`() = runTest {
        coEvery { cocktailService.getNonAlcoholic() } returnsMany listOf(
            Result.failure(Throwable()),
            Result.success(cocktailsMock)
        )

        val vm = viewModel()

        vm.loadData("", nonAlcoholic = true)
        vm.viewState.test {
            assertEquals(ViewState.Error::class.java, awaitItem().javaClass)
        }

        vm.loadData("", nonAlcoholic = true)
        vm.viewState.test {
            assertEquals(ViewState.Loaded::class.java, awaitItem().javaClass)
        }

        coVerify(exactly = 2) { cocktailService.getNonAlcoholic() }
    }

    @Test
    fun `assert loaded view state for search name`() = runTest {
        coEvery { cocktailService.getCocktailsBySearchName(any()) }.coAnswers {
            delay(100) // Simulate loading
            Result.success(cocktailsMock)
        }

        val vm = viewModel()

        // Starts Uninitialized
        assertEquals(ViewState.Uninitialized::class.java, vm.viewState.value.javaClass)

        vm.loadData("margarita", nonAlcoholic = false)
        vm.viewState.test {
            // Loading
            assertEquals(ViewState.Loading::class.java, awaitItem().javaClass)

            // Loaded
            val state = awaitItem()
            assertEquals(ViewState.Loaded::class.java, state.javaClass)
            val data = (state as ViewState.Loaded).data
            // No VM data transformation here
            assertEquals(cocktailsMock, data)

            // Calling loadData when already loaded does not do anything
            vm.loadData("margarita", nonAlcoholic = false)
            expectNoEvents()
        }

        coVerify(exactly = 1) { cocktailService.getCocktailsBySearchName(any()) }
    }

    @Test
    fun `assert error and then loaded view state on retry loadData for search name`() = runTest {
        coEvery { cocktailService.getCocktailsBySearchName(any()) } returnsMany listOf(
            Result.failure(Throwable()),
            Result.success(cocktailsMock)
        )

        val vm = viewModel()

        vm.loadData("margarita", nonAlcoholic = false)
        vm.viewState.test {
            assertEquals(ViewState.Error::class.java, awaitItem().javaClass)
        }

        vm.loadData("margarita", nonAlcoholic = false)
        vm.viewState.test {
            assertEquals(ViewState.Loaded::class.java, awaitItem().javaClass)
        }

        coVerify(exactly = 2) { cocktailService.getCocktailsBySearchName(any()) }
    }
}
