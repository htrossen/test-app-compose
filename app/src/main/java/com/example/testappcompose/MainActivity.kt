package com.example.testappcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.testappcompose.ui.theme.TestAppComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestAppComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = "list") {
                        composable("list") {
                            HomePage(
                                navToIngredientDetails = { ingredientId ->
                                    navController.navigate(route = "ingredientDetails/$ingredientId")
                                }
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
                        composable("cocktails/{ingredientName}") { backStackEntry ->
                            val ingredientName = backStackEntry.arguments?.getString("ingredientName")
                            CocktailsPage(
                                ingredientName = ingredientName,
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
