package com.example.testappcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.testappcompose.screens.CocktailDetailPage
import com.example.testappcompose.screens.CocktailsPage
import com.example.testappcompose.screens.FavoritesPage
import com.example.testappcompose.screens.HomePage
import com.example.testappcompose.screens.IngredientDetailPage
import com.libraries.ui.theme.TestAppComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OneActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            TestAppComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = Home) {
                        composable<Home> {
                            HomePage(
                                navToSearchResults = { search ->
                                    navController.navigate(CocktailsSearch(search))
                                },
                                navToFavorites = {
                                    navController.navigate(Favorites)
                                },
                                navToMocktails = {
                                    navController.navigate(
                                        CocktailsSearch(searchName = "", nonAlcoholic = true)
                                    )
                                },
                                navToIngredientDetails = { ingredientId ->
                                    navController.navigate(IngredientDetails(ingredientId))
                                }
                            )
                        }
                        composable<Favorites> {
                            FavoritesPage(
                                navToCocktailDetails = { cocktailId ->
                                    navController.navigate(CocktailDetails(cocktailId))
                                },
                                navBack = { navController.popBackStack() }
                            )
                        }
                        composable<IngredientDetails> { backStackEntry ->
                            val ingredientName =
                                backStackEntry.toRoute<IngredientDetails>().ingredientName
                            IngredientDetailPage(
                                ingredientName = ingredientName,
                                navToCocktailDetails = { cocktailId ->
                                    navController.navigate(CocktailDetails(cocktailId))
                                },
                                navToViewAll = {
                                    navController.navigate(CocktailsSearch(ingredientName))
                                },
                                navBack = { navController.popBackStack() }
                            )
                        }
                        composable<CocktailsSearch> { backStackEntry ->
                            val route = backStackEntry.toRoute<CocktailsSearch>()
                            CocktailsPage(
                                searchName = route.searchName,
                                nonAlcoholic = route.nonAlcoholic,
                                navToCocktailDetails = { cocktailId ->
                                    navController.navigate(CocktailDetails(cocktailId))
                                },
                                navBack = { navController.popBackStack() }
                            )
                        }
                        composable<CocktailDetails> { backStackEntry ->
                            CocktailDetailPage(
                                cocktailId = backStackEntry.toRoute<CocktailDetails>().cocktailId,
                                navBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
