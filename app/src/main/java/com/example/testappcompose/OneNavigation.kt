package com.example.testappcompose

import kotlinx.serialization.Serializable

@Serializable
data object Home

@Serializable
data object Favorites

@Serializable
data class IngredientDetails(
    val ingredientName: String
)

@Serializable
data class CocktailDetails(
    val cocktailId: String
)

@Serializable
data class CocktailsSearch(
    val searchName: String,
    val nonAlcoholic: Boolean = false
)
