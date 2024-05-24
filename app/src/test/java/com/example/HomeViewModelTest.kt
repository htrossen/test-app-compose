package com.example

import androidx.compose.runtime.snapshotFlow
import app.cash.turbine.test
import com.example.testappcompose.common.CarouselItem
import com.example.testappcompose.screens.HomeViewModel
import com.example.testappcompose.screens.ViewState
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class HomeViewModelTest {

    private fun viewModel() = HomeViewModel()

    @Test
    fun `assert loaded view state`() = runTest {
        val vm = viewModel()

        launch {
            snapshotFlow { vm.viewState.value }.test {
                val state = awaitItem()
                assertTrue(state is ViewState.Loaded)
                val loaded = state as ViewState.Loaded
                assertEquals(6, loaded.data.size)
                assertEquals(
                    CarouselItem("Vodka", "https://www.thecocktaildb.com/images/ingredients/Vodka.png", "Vodka"),
                    loaded.data[0]
                )
                assertEquals(
                    CarouselItem("Gin", "https://www.thecocktaildb.com/images/ingredients/Gin.png", "Gin"),
                    loaded.data[1]
                )
                assertEquals(
                    CarouselItem("Tequila", "https://www.thecocktaildb.com/images/ingredients/Tequila.png", "Tequila"),
                    loaded.data[2]
                )
                assertEquals(
                    CarouselItem("Rum", "https://www.thecocktaildb.com/images/ingredients/Rum.png", "Rum"),
                    loaded.data[3]
                )
                assertEquals(
                    CarouselItem("Bourbon", "https://www.thecocktaildb.com/images/ingredients/Bourbon.png", "Bourbon"),
                    loaded.data[4]
                )
                assertEquals(
                    CarouselItem("Whiskey", "https://www.thecocktaildb.com/images/ingredients/Whiskey.png", "Whiskey"),
                    loaded.data[5]
                )
            }
        }
    }
}
