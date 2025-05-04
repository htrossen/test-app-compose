package com.libraries.core.service

import com.libraries.core.model.Cocktail
import com.libraries.core.model.toIngredients
import com.libraries.core.net.CocktailApi
import com.libraries.core.net.IngredientResponse
import com.libraries.ui.components.CarouselItem

interface CocktailService : Service {
    suspend fun getCocktailById(id: String): Result<Cocktail?>
    suspend fun getCocktailsBySearchName(searchName: String): Result<List<CarouselItem>>
    suspend fun getNonAlcoholic(): Result<List<CarouselItem>>
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

    override suspend fun getNonAlcoholic(): Result<List<CarouselItem>> {
        return apiCall {
            cocktailApi.getNonAlcoholic().drinks?.map {
                CarouselItem(id = it.id, it.imageUrl, title = it.name)
            } ?: emptyList()
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
