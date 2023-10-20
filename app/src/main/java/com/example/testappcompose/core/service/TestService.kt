package com.example.testappcompose.core.service

import com.example.testappcompose.common.CarouselItem
import com.example.testappcompose.core.model.Cocktail
import com.example.testappcompose.core.model.toIngredients
import com.example.testappcompose.core.net.IngredientResponse
import com.example.testappcompose.core.net.TestApi

interface TestService: Service {
    suspend fun getCocktailById(id: String) : Result<Cocktail?>
    suspend fun getCocktailsByIngredient(ingredientName: String) : Result<List<CarouselItem>>
    suspend fun getCocktailIngredient(ingredientName: String) : Result<IngredientResponse>
}

internal class TestServiceImpl internal constructor(
    private val testApi: TestApi
) : TestService {
    override suspend fun getCocktailById(id: String): Result<Cocktail?> {
        return apiCall {
            testApi.getCocktailById(id).drinks.firstOrNull()?.let {
                Cocktail(
                    name = it.name,
                    image = it.image,
                    glass = it.glass,
                    instructions = it.instructions,
                    ingredients = it.toIngredients()
                )
            }
        }
    }

    override suspend fun getCocktailsByIngredient(
        ingredientName: String)
    : Result<List<CarouselItem>> {
        return apiCall {
            testApi.getCocktailByIngredient(ingredientName).drinks?.map {
                CarouselItem(id = it.id, it.imageUrl, title = it.name)
            } ?: emptyList()
        }
    }

    override suspend fun getCocktailIngredient(ingredientName: String): Result<IngredientResponse> {
        return apiCall {
            testApi.getIngredients(ingredientName).ingredients.first()
        }
    }
}
