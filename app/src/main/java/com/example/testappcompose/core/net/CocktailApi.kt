package com.example.testappcompose.core.net

import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailApi {
    @GET("lookup.php")
    suspend fun getCocktailById(
        @Query("i") i: String
    ): CocktailsResponse

    @GET("filter.php")
    suspend fun getCocktailsByIngredient(
        @Query("i") i: String
    ): CocktailListResponse

    @GET("search.php")
    suspend fun getCocktailsByName(
        @Query("s") s: String
    ): CocktailListResponse

    @GET("search.php")
    suspend fun getIngredients(
        @Query("i") i: String
    ): IngredientsResponse
}
