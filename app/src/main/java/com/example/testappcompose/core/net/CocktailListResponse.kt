package com.example.testappcompose.core.net

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CocktailListResponse(
    val drinks: List<CocktailResponse>?
)

@Serializable
data class CocktailResponse(
    @SerialName("idDrink")
    val id: String,
    @SerialName("strDrink")
    val name: String,
    @SerialName("strDrinkThumb")
    val imageUrl: String,
)
