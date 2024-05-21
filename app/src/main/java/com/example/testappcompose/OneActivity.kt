package com.example.testappcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.testappcompose.screens.CocktailDetailPage
import com.example.testappcompose.screens.CocktailsPage
import com.example.testappcompose.screens.FavoritesPage
import com.example.testappcompose.screens.HomePage
import com.example.testappcompose.screens.IngredientDetailPage
import com.example.testappcompose.ui.theme.TestAppComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OneActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            TestAppComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = HOME) {
                        composable(HOME) {
                            HomePage(
                                navToSearchResults = { search ->
                                    navController.navigate(route = "$COCKTAILS/$search")
                                },
                                navToFavorites = {
                                    navController.navigate(route = FAVORITES)
                                },
                                navToIngredientDetails = { ingredientId ->
                                    navController.navigate(route = "$INGREDIENT_DETAILS/$ingredientId")
                                }
                            )
                        }
                        composable(FAVORITES) {
                            FavoritesPage(
                                navToCocktailDetails = { cocktailId ->
                                    navController.navigate(route = "$COCKTAIL_DETAILS/$cocktailId")
                                },
                                navBack = { navController.popBackStack() }
                            )
                        }
                        composable("$INGREDIENT_DETAILS/{$INGREDIENT_NAME}") { backStackEntry ->
                            val ingredientName = backStackEntry.arguments?.getString(INGREDIENT_NAME)
                            IngredientDetailPage(
                                ingredientName = ingredientName,
                                navToCocktailDetails = { cocktailId ->
                                    navController.navigate(route = "$COCKTAIL_DETAILS/$cocktailId")
                                },
                                navToViewAll = {
                                    navController.navigate(route = "$COCKTAILS/$ingredientName")
                                },
                                navBack = { navController.popBackStack() }
                            )
                        }
                        composable("$COCKTAILS/{$SEARCH_NAME}") { backStackEntry ->
                            val searchName = backStackEntry.arguments?.getString(SEARCH_NAME)
                            CocktailsPage(
                                searchName = searchName,
                                navToCocktailDetails = { cocktailId ->
                                    navController.navigate(route = "$COCKTAIL_DETAILS/$cocktailId")
                                },
                                navBack = { navController.popBackStack() }
                            )
                        }
                        composable("$COCKTAIL_DETAILS/{$COCKTAIL_ID}") { backStackEntry ->
                            val cocktailId = backStackEntry.arguments?.getString(COCKTAIL_ID)
                            CocktailDetailPage(
                                cocktailId = cocktailId,
                                navBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }

    // Routes and Params
    companion object {
        const val HOME = "home"
        const val FAVORITES = "favorites"
        const val INGREDIENT_DETAILS = "ingredientDetails"
        const val INGREDIENT_NAME = "ingredientName"
        const val COCKTAILS = "cocktails"
        const val SEARCH_NAME = "searchName"
        const val COCKTAIL_DETAILS = "cocktailDetails"
        const val COCKTAIL_ID = "cocktailId"
    }
}
