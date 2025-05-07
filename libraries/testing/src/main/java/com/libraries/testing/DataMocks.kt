package com.libraries.testing

import com.libraries.core.database.Favorite
import com.libraries.core.model.Cocktail
import com.libraries.core.model.Ingredient
import com.libraries.core.net.IngredientResponse
import com.libraries.ui.components.CarouselItem

object DataMocks {

    val ingredientMock = IngredientResponse(
        id = "1",
        name = "Vodka",
        abv = "40",
        description = "Vodka is a distilled beverage composed primarily of water and ethanol, sometimes with traces of impurities and flavorings.",
    )

    val cocktailsMock = listOf(
        CarouselItem(
            id = "15346",
            title = "155 Belmont",
            imageUrl = "https://www.thecocktaildb.com/images/media/drink/yqvvqs1475667388.jpg",
        ),
        CarouselItem(
            id = "17105",
            title = "501 Blue",
            imageUrl = "https://www.thecocktaildb.com/images/media/drink/ywxwqs1461867097.jpg",
        )
    )

    val favoritesMock = listOf(
        Favorite(
            id = "15346",
            name = "155 Belmont",
            imageUrl = "https://www.thecocktaildb.com/images/media/drink/yqvvqs1475667388.jpg",
        ),
        Favorite(
            id = "17105",
            name = "501 Blue",
            imageUrl = "https://www.thecocktaildb.com/images/media/drink/ywxwqs1461867097.jpg",
        )
    )

    val cocktailMock = Cocktail(
        name = "155 Belmont",
        image = "https://www.thecocktaildb.com/images/media/drink/yqvvqs1475667388.jpg",
        glass = "White wine glass",
        instructions = "Blend with ice.",
        ingredients = listOf(
            Ingredient(
                name = "Dark rum",
                amount = "1 shot"
            ),
            Ingredient(
                name = "Light rum",
                amount = "2 shots"
            ),
            Ingredient(
                name = "Vodka",
                amount = "1 shot"
            ),
            Ingredient(
                name = "Orange juice",
                amount = "1 shot"
            ),
        )
    )
}
