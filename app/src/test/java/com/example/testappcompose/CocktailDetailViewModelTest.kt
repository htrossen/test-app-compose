package com.example.testappcompose

import app.cash.turbine.test
import com.example.testappcompose.screens.CocktailDetailViewModel
import com.libraries.core.repo.PersonalizationRepo
import com.libraries.core.service.CocktailService
import com.libraries.testing.DataMocks.cocktailMock
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
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CocktailDetailViewModelTest {

    private val cocktailService = mockk<CocktailService> {
        coEvery { getCocktailById(any()) } returns Result.success(cocktailMock)
    }

    private val personalizationRepo = mockk<PersonalizationRepo> {
        coEvery { isFavorites(any()) } returns false
        coEvery { saveFavorites(any()) } returns Unit
        coEvery { deleteFavorite(any()) } returns Unit
    }

    private fun viewModel() = CocktailDetailViewModel(
        cocktailService = cocktailService,
        personalizationRepo = personalizationRepo
    )

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
        coEvery { cocktailService.getCocktailById(any()) }.coAnswers {
            delay(100) // Simulate loading
            Result.success(cocktailMock)
        }

        val vm = viewModel()

        // Starts Uninitialized
        assertEquals(ViewState.Uninitialized::class.java, vm.viewState.value.javaClass)

        vm.loadData("15346")
        vm.viewState.test {
            // Loading
            assertEquals(ViewState.Loading::class.java, awaitItem().javaClass)

            // Loaded
            val state = awaitItem()
            assertEquals(ViewState.Loaded::class.java, state.javaClass)
            val data = (state as ViewState.Loaded).data
            // No VM data transformation here
            assertEquals(cocktailMock, data)

            // Calling loadData when already loaded does not do anything
            vm.loadData("15346")
            expectNoEvents()
        }

        coVerify(exactly = 1) { cocktailService.getCocktailById(any()) }
    }

    @Test
    fun `assert error and then loaded view state on retry loadData`() = runTest {
        coEvery { cocktailService.getCocktailById(any()) } returnsMany listOf(
            Result.failure(Throwable()),
            Result.success(cocktailMock)
        )

        val vm = viewModel()

        vm.loadData("15346")
        vm.viewState.test {
            assertEquals(ViewState.Error::class.java, awaitItem().javaClass)
        }

        vm.loadData("15346")
        vm.viewState.test {
            assertEquals(ViewState.Loaded::class.java, awaitItem().javaClass)
        }

        coVerify(exactly = 2) { cocktailService.getCocktailById(any()) }
    }

    @Test
    fun `save and remove favorites calls expected repo functions`() {
        val vm = viewModel()

        vm.loadData("15346")

        // Not a favorite
        assertFalse(vm.favorite.value)

        vm.saveOrRemoveAsFavorite("15346", cocktail = cocktailMock)

        // Now a favorite
        assertTrue(vm.favorite.value)
        coVerify(exactly = 1) { personalizationRepo.saveFavorites(any()) }

        vm.saveOrRemoveAsFavorite("15346", cocktail = cocktailMock)

        // Removed from favorites
        assertFalse(vm.favorite.value)
        coVerify(exactly = 1) { personalizationRepo.deleteFavorite(any()) }
    }
}
