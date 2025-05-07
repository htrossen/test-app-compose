package com.example.testappcompose

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import com.example.testappcompose.screens.HomePage
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun home_page_carousel_is_displayed_and_interactive() {
        composeTestRule.setContent {
            HomePage(
                navToSearchResults = {},
                navToFavorites = {},
                navToMocktails = {},
                navToIngredientDetails = {}
            )
        }

        composeTestRule.onNodeWithText("Vodka").assertIsDisplayed()
        composeTestRule.onNodeWithText("Gin").assertIsDisplayed()
        composeTestRule.onNodeWithText("Gin").performTouchInput {
            swipeLeft()
        }
        composeTestRule.onNodeWithText("Tequila").assertIsDisplayed()
        composeTestRule.onNodeWithText("Rum").assertIsDisplayed()
        composeTestRule.onNodeWithText("Rum").performTouchInput {
            swipeLeft()
        }
        composeTestRule.onNodeWithText("Bourbon").assertIsDisplayed()
    }
}
