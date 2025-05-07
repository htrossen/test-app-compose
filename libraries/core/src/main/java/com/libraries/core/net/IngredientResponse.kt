package com.libraries.core.net

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IngredientsResponse(
    val ingredients: List<IngredientResponse>
)

@Serializable
data class IngredientResponse(
    @SerialName("idIngredient")
    val id: String,
    @SerialName("strIngredient")
    val name: String,
    @SerialName("strABV")
    val abv: String,
    @SerialName("strDescription")
    val description: String,
)
