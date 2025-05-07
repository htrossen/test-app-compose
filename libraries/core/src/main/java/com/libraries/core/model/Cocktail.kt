package com.libraries.core.model

import com.libraries.core.net.CocktailDetailResponse

data class Cocktail(
    val name: String,
    val image: String,
    val glass: String,
    val instructions: String,
    val ingredients: List<Ingredient>
)

data class Ingredient(
    val name: String,
    val amount: String
)

fun CocktailDetailResponse.toIngredients(): List<Ingredient> {
    val ingredients = mutableListOf<Ingredient>()
    ingredient1?.let {
        ingredients.add(
            Ingredient(it, measure1.orEmpty())
        )
    }
    ingredient2?.let {
        ingredients.add(
            Ingredient(it, measure2.orEmpty())
        )
    }
    ingredient3?.let {
        ingredients.add(
            Ingredient(it, measure3.orEmpty())
        )
    }
    ingredient4?.let {
        ingredients.add(
            Ingredient(it, measure4.orEmpty())
        )
    }
    ingredient5?.let {
        ingredients.add(
            Ingredient(it, measure5.orEmpty())
        )
    }
    ingredient6?.let {
        ingredients.add(
            Ingredient(it, measure6.orEmpty())
        )
    }
    ingredient7?.let {
        ingredients.add(
            Ingredient(it, measure7.orEmpty())
        )
    }
    ingredient8?.let {
        ingredients.add(
            Ingredient(it, measure8.orEmpty())
        )
    }
    ingredient9?.let {
        ingredients.add(
            Ingredient(it, measure9.orEmpty())
        )
    }
    ingredient10?.let {
        ingredients.add(
            Ingredient(it, measure10.orEmpty())
        )
    }
    ingredient11?.let {
        ingredients.add(
            Ingredient(it, measure11.orEmpty())
        )
    }
    ingredient12?.let {
        ingredients.add(
            Ingredient(it, measure12.orEmpty())
        )
    }
    ingredient13?.let {
        ingredients.add(
            Ingredient(it, measure13.orEmpty())
        )
    }
    ingredient14?.let {
        ingredients.add(
            Ingredient(it, measure14.orEmpty())
        )
    }
    ingredient15?.let {
        ingredients.add(
            Ingredient(it, measure15.orEmpty())
        )
    }
    return ingredients
}
