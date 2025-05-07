package com.example.testappcompose

import app.cash.turbine.test
import com.example.testappcompose.screens.FavoritesViewModel
import com.libraries.core.repo.PersonalizationRepo
import com.libraries.testing.DataMocks.cocktailsMock
import com.libraries.testing.DataMocks.favoritesMock
import com.libraries.ui.ViewState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModelTest {

    private val personalizationRepo = mockk<PersonalizationRepo> {
        every { getFavorites() } returns flowOf(favoritesMock)
    }

    private fun viewModel() = FavoritesViewModel(personalizationRepo = personalizationRepo)

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `assert empty view state when no favorites`() = runTest {
        every { personalizationRepo.getFavorites() } returns flowOf(emptyList())

        val vm = viewModel()

        vm.viewState.test {
            assertEquals(ViewState.Empty::class.java, awaitItem().javaClass)
        }
    }

    @Test
    fun `assert loaded view state when favorites`() = runTest {
        val vm = viewModel()

        vm.viewState.test {
            val state = awaitItem()
            assertEquals(ViewState.Loaded::class.java, state.javaClass)
            val data = (state as ViewState.Loaded).data
            assertEquals(cocktailsMock, data)
        }
    }
}
