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
                    NavHost(navController, startDestination = "home") {
                        composable("home") {
                            HomePage(
                                navToSearchResults = { search ->
                                    navController.navigate(route = "cocktails/$search")
                                },
                                navToFavorites = {
                                    navController.navigate(route = "favorites")
                                },
                                navToIngredientDetails = { ingredientId ->
                                    navController.navigate(route = "ingredientDetails/$ingredientId")
                                }
                            )
                        }
                        composable("favorites") {
                            FavoritesPage(
                                navToCocktailDetails = { cocktailId ->
                                    navController.navigate(route = "cocktailDetails/$cocktailId")
                                },
                                navBack = { navController.popBackStack() }
                            )
                        }
                        composable("ingredientDetails/{ingredientName}") { backStackEntry ->
                            val ingredientName = backStackEntry.arguments?.getString("ingredientName")
                            IngredientDetailPage(
                                ingredientName = ingredientName,
                                navToCocktailDetails = { cocktailId ->
                                    navController.navigate(route = "cocktailDetails/$cocktailId")
                                },
                                navToViewAll = {
                                    navController.navigate(route = "cocktails/$ingredientName")
                                },
                                navBack = { navController.popBackStack() }
                            )
                        }
                        composable("cocktails/{searchName}") { backStackEntry ->
                            val searchName = backStackEntry.arguments?.getString("searchName")
                            CocktailsPage(
                                searchName = searchName,
                                navToCocktailDetails = { cocktailId ->
                                    navController.navigate(route = "cocktailDetails/$cocktailId")
                                },
                                navBack = { navController.popBackStack() }
                            )
                        }
                        composable("cocktailDetails/{cocktailId}") { backStackEntry ->
                            val cocktailId = backStackEntry.arguments?.getString("cocktailId")
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
}
