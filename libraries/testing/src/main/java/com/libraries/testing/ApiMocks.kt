package com.libraries.testing

import com.libraries.core.net.CocktailDetailResponse
import com.libraries.core.net.CocktailListResponse
import com.libraries.core.net.CocktailResponse
import com.libraries.core.net.CocktailsResponse
import com.libraries.core.net.IngredientsResponse
import com.libraries.testing.DataMocks.ingredientMock

object ApiMocks {

    val detailResponse = CocktailsResponse(
        drinks = listOf(
            CocktailDetailResponse(
                name = "155 Belmont",
                image = "https://www.thecocktaildb.com/images/media/drink/yqvvqs1475667388.jpg",
                glass = "White wine glass",
                instructions = "Blend with ice.",
                ingredient1 = "Dark rum",
                ingredient2 = "Light rum",
                ingredient3 = "Vodka",
                ingredient4 = "Orange juice",
                measure1 = "1 shot",
                measure2 = "2 shots",
                measure3 = "1 shot",
                measure4 = "1 shot",
            )
        )
    )

    val listResponse = CocktailListResponse(
        drinks = listOf(
            CocktailResponse(
                id = "15346",
                name = "155 Belmont",
                imageUrl = "https://www.thecocktaildb.com/images/media/drink/yqvvqs1475667388.jpg",
            ),
            CocktailResponse(
                id = "17105",
                name = "501 Blue",
                imageUrl = "https://www.thecocktaildb.com/images/media/drink/ywxwqs1461867097.jpg",
            )
        )
    )

    val ingredientResponse = IngredientsResponse(
        ingredients = listOf(
            ingredientMock
        )
    )
}
