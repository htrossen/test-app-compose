package com.example.testappcompose.core.service

import com.example.testappcompose.common.CarouselItem
import com.example.testappcompose.core.model.Cocktail
import com.example.testappcompose.core.model.toIngredients
import com.example.testappcompose.core.net.CocktailApi
import com.example.testappcompose.core.net.IngredientResponse

interface CocktailService : Service {
    suspend fun getCocktailById(id: String): Result<Cocktail?>
    suspend fun getCocktailsBySearchName(searchName: String): Result<List<CarouselItem>>
    suspend fun getCocktailIngredient(ingredientName: String): Result<IngredientResponse>
}

internal class CocktailServiceImpl internal constructor(
    private val cocktailApi: CocktailApi
) : CocktailService {
    override suspend fun getCocktailById(id: String): Result<Cocktail?> {
        return apiCall {
            cocktailApi.getCocktailById(id).drinks.firstOrNull()?.let {
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

    override suspend fun getCocktailsBySearchName(searchName: String): Result<List<CarouselItem>> {
        return apiCall {
            cocktailApi.getCocktailsByIngredient(searchName).drinks?.map {
                CarouselItem(id = it.id, it.imageUrl, title = it.name)
            }
        }.mapCatching {
            if (it.isNullOrEmpty()) {
                getCocktailsByName(searchName)
            } else it
        }.recoverCatching {
            getCocktailsByName(searchName)
        }
    }

    private suspend fun getCocktailsByName(searchName: String): List<CarouselItem> {
        return apiCall {
            cocktailApi.getCocktailsByName(searchName).drinks?.map {
                CarouselItem(id = it.id, it.imageUrl, title = it.name)
            } ?: emptyList()
        }.getOrDefault(emptyList())
    }

    override suspend fun getCocktailIngredient(ingredientName: String): Result<IngredientResponse> {
        return apiCall {
            cocktailApi.getIngredients(ingredientName).ingredients.first()
        }
    }
}
